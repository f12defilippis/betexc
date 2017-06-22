package com.bonde.betbot.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.ForecastFinal;
import com.bonde.betbot.model.domain.ForecastSummary;
import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.ValueBet;
import com.bonde.betbot.model.domain.ValueGroup;
import com.bonde.betbot.repository.ForecastFinalRepository;
import com.bonde.betbot.repository.ForecastSummaryRepository;
import com.bonde.betbot.repository.ValueBetRepository;
import com.bonde.betbot.repository.ValueGroupRepository;
import com.bonde.betbot.util.DateUtil;
import com.bonde.betbot.util.Threshold;

@Service
public class ProbabilityService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	@Autowired
	private ValueBetRepository valueBetRepository;
	
	@Autowired
	private ForecastSummaryRepository forecastSummaryRepository;
	
	@Autowired
	private ValueGroupRepository valueGroupRepository;
	
	@Autowired
	private ForecastFinalRepository forecastFinalRepository;
	
	public void findProbabilities(Date date)
	{

		List<ValueBet> listValueBets = valueBetRepository.findByForecastMatchDateStartBetween(date, DateUtil.addDaysToDate(date, 1));
		if(listValueBets!=null)
		{
			log.info("Found " + listValueBets.size() + " valuebets to work. Start Saving Forecast Finals.......");
		}
		
		for(ValueBet vb : listValueBets)
		{
			try {
				singleForecastFinal(vb, date);
				log.debug("SAVED VB ID: " + vb.getId());
			} catch (Exception e) {
				log.warn("Exception in VB ID: " + vb.getId() + " EX: " + e.getMessage());
			}
		}

		log.info("Forecast Finals saved. Now I m starting to update the fto order.......");

		forecastTypeOrderManagement(date);
		
		log.info("Fto order saved. Finish to save data for date " + DateUtil.fromDateToString(date));
		
	}

	
	private void singleForecastFinal(ValueBet vb, Date date) throws Exception
	{
		ForecastValue forecastValue = vb.getForecast().getForecastValue();
		List<ValueGroup> forecastValueGroupList = valueGroupRepository.findValueGroupByValueInRange(forecastValue.getValue()); 

		ForecastValue marginValue = vb.getMargin();
		List<ValueGroup> marginValueGroupList = valueGroupRepository.findValueGroupByValueInRange(marginValue.getValue()); 

		List<ForecastSummary> forecastSummaries = null;
		
		forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetAndForecastTypeOccurrenceAndFinalDateBetween(vb.getForecast().getSource(), forecastValue, marginValue, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
		String forecastSummaryParameter = "FV0-VB0-FTOY";
		if(!checkSummaries(forecastSummaries,forecastSummaryParameter,vb))
		{
			// NO FTO
			forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetAndForecastTypeOccurrenceAndFinalDateBetween(vb.getForecast().getSource(), forecastValue, marginValue, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV0-VB0-FTON";
		}


		
		int valueGroupTypes [] = {1,2,3};
		
		
		for(int i = 3 ; i >= 1 ; i--)
		{
			int vgType = valueGroupTypes[i];

		
			if(!checkSummaries(forecastSummaries,forecastSummaryParameter,vb))
			{
				ValueGroup value2vb = null;
				for(ValueGroup vg : marginValueGroupList)
				{
					if(vg.getValueGroupType().getId() == vgType)
					{
						value2vb = vg;
					}
				}
				
				if(value2vb!=null)
				{
					// vb2
					forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
							(vb.getForecast().getSource(), forecastValue, value2vb, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
					forecastSummaryParameter = "FV0-VB" + i + "-FTOY";
				}
			}

			
			
			if(!checkSummaries(forecastSummaries,forecastSummaryParameter,vb))
			{
				ValueGroup value2vb = null;
				for(ValueGroup vg : marginValueGroupList)
				{
					if(vg.getValueGroupType().getId() == vgType)
					{
						value2vb = vg;
					}
				}
				
				// vb2 NO FTO
				forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
						(vb.getForecast().getSource(), forecastValue, value2vb, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
				forecastSummaryParameter = "FV0-VB" + i + "-FTON";
			}
			
			if(!checkSummaries(forecastSummaries,forecastSummaryParameter,vb))
			{
				ValueGroup value2vb = null;
				for(ValueGroup vg : marginValueGroupList)
				{
					if(vg.getValueGroupType().getId() == vgType)
					{
						value2vb = vg;
					}
				}

				ValueGroup value2fv = null;
				for(ValueGroup vg : forecastValueGroupList)
				{
					if(vg.getValueGroupType().getId() == vgType)
					{
						value2fv = vg;
					}
				}
				
				
				if(value2vb!=null && value2fv!=null)
				{
					// vb2 fv2
					forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
							(vb.getForecast().getSource(), value2fv, value2vb, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
					forecastSummaryParameter = "FV" + i + "-VB" + i + "-FTOY";
				}
			}

			if(!checkSummaries(forecastSummaries,forecastSummaryParameter,vb))
			{
				ValueGroup value2vb = null;
				for(ValueGroup vg : marginValueGroupList)
				{
					if(vg.getValueGroupType().getId() == vgType)
					{
						value2vb = vg;
					}
				}

				ValueGroup value2fv = null;
				for(ValueGroup vg : forecastValueGroupList)
				{
					if(vg.getValueGroupType().getId() == vgType)
					{
						value2fv = vg;
					}
				}
				
				
				if(value2vb!=null && value2fv!=null)
				{
					// vb2 fv2 NO FTO
					forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
							(vb.getForecast().getSource(), value2fv, value2vb, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
					forecastSummaryParameter = "FV" + i + "-VB" + i + "-FTON";
				}
			}			

			if(checkSummaries(forecastSummaries,forecastSummaryParameter,vb))
			{
				forecastFinalSave(forecastSummaries, vb, forecastSummaryParameter);
			}
		}
		
	}
	
	
	private void forecastFinalSave(List<ForecastSummary> forecastSummaries, ValueBet vb, String forecastSummaryParameter)
	{
		int totalOccurrences = 0;
		int verifiedOccurreces = 0;
		for(ForecastSummary fs : forecastSummaries)
		{
			totalOccurrences = totalOccurrences + fs.getNumOccurrences();
			verifiedOccurreces = verifiedOccurreces + fs.getNumVerified();
		}
		

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);			
		double realProbability = Double.valueOf(df.format(verifiedOccurreces / totalOccurrences).replace(",", "."));
		double oddForecast = 1/vb.getOdd().getFirstValue();

		ForecastFinal forecastFinal = new ForecastFinal();
		forecastFinal.setSource(vb.getForecast().getSource());
		forecastFinal.setMatch(vb.getOdd().getMatch());
		forecastFinal.setOdd(vb.getOdd());
		forecastFinal.setForecastTypeOccurrence(vb.getForecast().getForecastTypeOccurrence());
		forecastFinal.setInitialProbability(vb.getForecast().getForecastValue().getValue());
		forecastFinal.setInitialMargin(vb.getMargin().getValue());
		forecastFinal.setAdjustedProbability(realProbability);
		forecastFinal.setAdjustedMargin(Double.valueOf(df.format((realProbability-oddForecast)/oddForecast).replace(",", ".")));
		forecastFinal.setProbabilityVariation(Double.valueOf(df.format((realProbability-vb.getForecast().getForecastValue().getValue())/vb.getForecast().getForecastValue().getValue()).replace(",", ".")));
		forecastFinal.setForecastSummaryParameter(forecastSummaryParameter);
		forecastFinal.setSqmaverage(calculateSQM(forecastSummaries));
		
		forecastFinalRepository.save(forecastFinal);		
	}
	
	
	
	private void forecastTypeOrderManagement(Date date)
	{
		List<ForecastFinal> forecastFinalList = forecastFinalRepository.findByMatchDateStartBetween(date, DateUtil.addDaysToDate(date, 1));
		
		// MATCH - SOURCE - FTO
		Map<Integer,Map<Integer,Map<Integer,List<ForecastFinal>>>> forecastFinalsMap = new HashMap<Integer,Map<Integer,Map<Integer,List<ForecastFinal>>>>();

		// MATCH - FTO - SOURCE
		Map<Integer,Map<Integer,Map<Integer,List<ForecastFinal>>>> forecastFtoSourceMap = new HashMap<Integer,Map<Integer,Map<Integer,List<ForecastFinal>>>>();
		
		for(ForecastFinal ff : forecastFinalList)
		{
			if(forecastFinalsMap.get(ff.getMatch().getId())==null)
			{
				Map<Integer,List<ForecastFinal>> ffList = new HashMap<Integer,List<ForecastFinal>>();
				Map<Integer,Map<Integer,List<ForecastFinal>>> forecastFinalsInternalMap = new HashMap<Integer, Map<Integer,List<ForecastFinal>>>();
				forecastFinalsInternalMap.put(ff.getSource().getId(), ffList);
				forecastFinalsMap.put(ff.getMatch().getId(), forecastFinalsInternalMap);
			}

			if(forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId())==null)
			{
				Map<Integer,List<ForecastFinal>> ffList = new HashMap<Integer,List<ForecastFinal>>();
				Map<Integer,Map<Integer,List<ForecastFinal>>> forecastFinalsInternalMap = new HashMap<Integer, Map<Integer,List<ForecastFinal>>>();
				forecastFinalsInternalMap.put(ff.getSource().getId(), ffList);
				forecastFinalsMap.put(ff.getMatch().getId(), forecastFinalsInternalMap);
			}
			
			if(forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId()).get(ff.getForecastTypeOccurrence().getForecastType().getId())==null)
			{
				List<ForecastFinal> ffList = new ArrayList<ForecastFinal>();
				forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId()).put(ff.getForecastTypeOccurrence().getForecastType().getId(), ffList);
			}

			forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId()).get(ff.getForecastTypeOccurrence().getForecastType().getId()).add(ff);

		
		
			if(forecastFtoSourceMap.get(ff.getMatch().getId())==null)
			{
				Map<Integer,List<ForecastFinal>> ffList = new HashMap<Integer,List<ForecastFinal>>();
				Map<Integer,Map<Integer,List<ForecastFinal>>> forecastFinalsInternalMap = new HashMap<Integer, Map<Integer,List<ForecastFinal>>>();
				forecastFinalsInternalMap.put(ff.getForecastTypeOccurrence().getId(), ffList);
				forecastFtoSourceMap.put(ff.getMatch().getId(), forecastFinalsInternalMap);
			}
		
			if(forecastFtoSourceMap.get(ff.getMatch().getId()).get(ff.getForecastTypeOccurrence().getId())==null)
			{
				Map<Integer,List<ForecastFinal>> ffList = new HashMap<Integer,List<ForecastFinal>>();
				Map<Integer,Map<Integer,List<ForecastFinal>>> forecastFinalsInternalMap = new HashMap<Integer, Map<Integer,List<ForecastFinal>>>();
				forecastFinalsInternalMap.put(ff.getForecastTypeOccurrence().getId(), ffList);
				forecastFtoSourceMap.put(ff.getMatch().getId(), forecastFinalsInternalMap);
			}
			
			if(forecastFtoSourceMap.get(ff.getMatch().getId()).get(ff.getForecastTypeOccurrence().getForecastType().getId()).get(ff.getSource().getId())==null)
			{
				List<ForecastFinal> ffList = new ArrayList<ForecastFinal>();
				forecastFtoSourceMap.get(ff.getMatch().getId()).get(ff.getForecastTypeOccurrence().getForecastType().getId()).put(ff.getSource().getId(), ffList);
			}
			
			forecastFtoSourceMap.get(ff.getMatch().getId()).get(ff.getForecastTypeOccurrence().getForecastType().getId()).get(ff.getSource().getId()).add(ff);
		
		}

		//MATCH
		for(Map.Entry<Integer,Map<Integer,Map<Integer,List<ForecastFinal>>>> entry  : forecastFinalsMap.entrySet())
		{
			//SOURCE
			for(Map.Entry<Integer,Map<Integer,List<ForecastFinal>>> internalEntry  : entry.getValue().entrySet())
			{
				//FTO
				for(Map.Entry<Integer,List<ForecastFinal>> internalEntry2  : internalEntry.getValue().entrySet())
				{
					List<ForecastFinal> listToOrder = internalEntry2.getValue();
					Collections.sort(listToOrder, ForecastFinal.sortByAdjustedProbabilityDesc());
					
					int i = 1;
					for(ForecastFinal ff : listToOrder)
					{
						ff.setForecastTypeOccurrenceOrder(i);
						forecastFinalRepository.save(ff);
						i++;
					}
				}				
			}
		}
		
		//MATCH
		for(Map.Entry<Integer,Map<Integer,Map<Integer,List<ForecastFinal>>>> entry  : forecastFtoSourceMap.entrySet())
		{
			//FTO
			for(Map.Entry<Integer,Map<Integer,List<ForecastFinal>>> internalEntry  : entry.getValue().entrySet())
			{
				//SOURCE
				for(Map.Entry<Integer,List<ForecastFinal>> internalEntry2  : internalEntry.getValue().entrySet())
				{
					List<ForecastFinal> listToOrder = internalEntry2.getValue();
					Collections.sort(listToOrder, ForecastFinal.sortByProbabilityVariation());

					double weightSum = 0.0;
					double adjustedProbabilitySum = 0.0;
					double initialProbabilitySum = 0.0;
					String forecastParameter = "";
					for(int i = 0 ; i < 3 && i < listToOrder.size() ; i++)
					{
						ForecastFinal ff = listToOrder.get(i);
						double weight = 1 - ff.getProbabilityVariation();
						weightSum = weightSum + weight;
						adjustedProbabilitySum = adjustedProbabilitySum + (ff.getAdjustedProbability() * weight);
						initialProbabilitySum = initialProbabilitySum + (ff.getInitialProbability() * weight);
						forecastParameter = forecastParameter + "-" + ff.getSource().getDescription();
					}
					
					double finalAdjustedProbability = adjustedProbabilitySum/weightSum;
					double finalInitialProbability = initialProbabilitySum/weightSum;
					
					double oddForecast = 1/listToOrder.get(0).getOdd().getFirstValue();
					
					ForecastFinal forecastFinal = new ForecastFinal();
//					forecastFinal.setSource(vb.getForecast().getSource());
					forecastFinal.setMatch(listToOrder.get(0).getMatch());
					forecastFinal.setOdd(listToOrder.get(0).getOdd());
					forecastFinal.setForecastTypeOccurrence(listToOrder.get(0).getForecastTypeOccurrence());
					forecastFinal.setInitialProbability(finalInitialProbability);
					forecastFinal.setInitialMargin((finalInitialProbability-oddForecast)/oddForecast);
					forecastFinal.setAdjustedProbability(finalAdjustedProbability);
					forecastFinal.setAdjustedMargin((finalAdjustedProbability-oddForecast)/oddForecast);
					forecastFinal.setProbabilityVariation((finalAdjustedProbability-finalInitialProbability)/finalInitialProbability);
					forecastFinal.setForecastSummaryParameter(forecastParameter);
					forecastFinal.setSqmaverage(calculateSQMFF(listToOrder));					
					
					forecastFinal.setForecastTypeOccurrenceOrder(listToOrder.get(0).getForecastTypeOccurrenceOrder());
					
					forecastFinalRepository.save(forecastFinal);
					
				}				
			}
		}		
		
		
		
	}
	
	
	
	
	private boolean checkSummaries(List<ForecastSummary> forecastSummaries, String forecastSummaryParameter, ValueBet vb)
	{
		if(forecastSummaries!=null && forecastSummaries.size()>= Threshold.MINIMUM_SUMMARIES)
		{
			double sqmaverage = calculateSQM(forecastSummaries);
			
			if(sqmaverage <= Threshold.MAX_VARIANCE)
			{
				log.info("Final Forecast FOUND! Match: " + vb.getForecast().getMatch().getHomeTeam().getName() + " - " + vb.getForecast().getMatch().getAwayTeam().getName()
						+ " FT: " + vb.getForecast().getForecastTypeOccurrence().getForecastType().getDescription() + " FTO: " + vb.getForecast().getForecastTypeOccurrence().getDescription()
						+ " Parameters: " + forecastSummaryParameter);
				return true;
			}
		}
		return false;
	}
	
	private Double calculateSQM(List<ForecastSummary> forecastSummaries)
	{
		double sum = 0.0;
		for(ForecastSummary fs : forecastSummaries)
		{
			double verifiedPerc = fs.getNumVerified()/fs.getNumOccurrences();
			sum = sum + verifiedPerc;
		}
		double average = sum/forecastSummaries.size();

		double powerValueSum = 0.0;
		for(ForecastSummary fs : forecastSummaries)
		{
			double verifiedPerc = fs.getNumVerified()/fs.getNumOccurrences();
			double powerValue = Math.pow(verifiedPerc-average, 2);
			powerValueSum = powerValueSum + powerValue;
		}
		double variance = powerValueSum/(forecastSummaries.size()-1);
		double sqm = Math.sqrt(variance);

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);			
		
		Double sqmaverage = Double.valueOf(df.format(sqm/average).replace(",", "."));
		return sqmaverage;
	}
	
	private Double calculateSQMFF(List<ForecastFinal> forecastSummaries)
	{
		double sum = 0.0;
		for(ForecastFinal fs : forecastSummaries)
		{
			sum = sum + fs.getAdjustedProbability();
		}
		double average = sum/forecastSummaries.size();

		double powerValueSum = 0.0;
		for(ForecastFinal fs : forecastSummaries)
		{
			double powerValue = Math.pow(fs.getAdjustedProbability()-average, 2);
			powerValueSum = powerValueSum + powerValue;
		}
		double variance = powerValueSum/(forecastSummaries.size()-1);
		double sqm = Math.sqrt(variance);

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);			
		
		Double sqmaverage = Double.valueOf(df.format(sqm/average).replace(",", "."));
		return sqmaverage;
	}	
	
	
	
	
}
