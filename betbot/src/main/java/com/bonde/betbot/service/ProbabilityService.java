package com.bonde.betbot.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.ForecastFinal;
import com.bonde.betbot.model.domain.ForecastSummary;
import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.ValueBet;
import com.bonde.betbot.model.domain.ValueGroup;
import com.bonde.betbot.model.domain.ValueGroupType;
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
	
	
	@Transactional
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

		ValueGroup fv2 = null;
		ValueGroup fv5 = null;
		for(ValueGroup vg : forecastValueGroupList)
		{
			if(vg.getValueGroupType().getId() == ValueGroupType.T2)
			{
				fv2 = vg;
			}else if(vg.getValueGroupType().getId() == ValueGroupType.T5)
			{
				fv5 = vg;
			}
		}
		
		ForecastValue marginValue = vb.getMargin();
		List<ValueGroup> marginValueGroupList = valueGroupRepository.findValueGroupByValueInRange(marginValue.getValue()); 

		ValueGroup vb2 = null;
		ValueGroup vb5 = null;
		for(ValueGroup vg : marginValueGroupList)
		{
			if(vg.getValueGroupType().getId() == ValueGroupType.T2)
			{
				vb2 = vg;
			}else if(vg.getValueGroupType().getId() == ValueGroupType.T5)
			{
				vb5 = vg;
			}
		}
		
		
		boolean found = false;
		List<ForecastSummary> forecastSummaries = null;
		
		forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetAndForecastTypeOccurrenceAndFinalDateBetween(vb.getForecast().getSource(), forecastValue, marginValue, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
		String forecastSummaryParameter = "FV0-VB0-FTOY";
		
		found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		
		//FV0-VB2-FTOY
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), forecastValue, vb2, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV0-VB2-FTOY";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}

		//FV2-VB2-FTOY
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), fv2, vb2, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV2-VB2-FTOY";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}

		//FV0-VB5-FTOY
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), forecastValue, vb5, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV0-VB5-FTOY";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}

		//FV2-VB5-FTOY
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), fv2, vb5, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV2-VB5-FTOY";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}

		
		//FV0-VB0-FTON
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetAndForecastTypeOccurrenceAndFinalDateBetween(vb.getForecast().getSource(), forecastValue, marginValue, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV0-VB0-FTON";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}
		
		//FV0-VB2-FTON
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), forecastValue, vb2, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV0-VB2-FTON";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}
		
		//FV2-VB2-FTON
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), fv2, vb2, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV2-VB2-FTON";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}
		
		//FV0-VB5-FTON
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndForecastValueAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), forecastValue, vb5, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV0-VB5-FTON";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}		

		//FV2-VB5-FTON
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), fv2, vb5, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV2-VB5-FTON";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}
		
		//FV5-VB5-FTOY
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), fv5, vb5, vb.getForecast().getForecastTypeOccurrence(), DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV5-VB5-FTOY";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}
		
		//FV5-VB5-FTON
		if(!found)
		{
			forecastSummaries = forecastSummaryRepository.findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween
					(vb.getForecast().getSource(), fv5, vb5, null, DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE*(-1)), date);
			forecastSummaryParameter = "FV5-VB5-FTON";
			found = checkSummaries(forecastSummaries,forecastSummaryParameter,vb);
		}

		if(found)
		{
			forecastFinalSave(forecastSummaries, vb, forecastSummaryParameter);
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
		double realProbability = Double.valueOf(df.format(Double.valueOf(verifiedOccurreces) / Double.valueOf(totalOccurrences)).replace(",", "."));
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
		Map<Integer,Map<Integer,List<ForecastFinal>>> forecastFtoSourceMap = new HashMap<Integer,Map<Integer,List<ForecastFinal>>>();
		
		for(ForecastFinal ff : forecastFinalList)
		{
			if(forecastFinalsMap.get(ff.getMatch().getId())==null)
			{
				Map<Integer,Map<Integer,List<ForecastFinal>>> forecastFinalsInternalMap = new HashMap<Integer, Map<Integer,List<ForecastFinal>>>();
				forecastFinalsMap.put(ff.getMatch().getId(), forecastFinalsInternalMap);
			}

			if(forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId())==null)
			{
				Map<Integer,List<ForecastFinal>> forecastFinalsInternalMap = new HashMap<Integer,List<ForecastFinal>>();
				forecastFinalsMap.get(ff.getMatch().getId()).put(ff.getSource().getId(), forecastFinalsInternalMap);
			}
			
			if(forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId()).get(ff.getForecastTypeOccurrence().getForecastType().getId())==null)
			{
				List<ForecastFinal> ffList = new ArrayList<ForecastFinal>();
				forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId()).put(ff.getForecastTypeOccurrence().getForecastType().getId(), ffList);
			}

			forecastFinalsMap.get(ff.getMatch().getId()).get(ff.getSource().getId()).get(ff.getForecastTypeOccurrence().getForecastType().getId()).add(ff);

		

			
			
			if(forecastFtoSourceMap.get(ff.getMatch().getId())==null)
			{
				Map<Integer,List<ForecastFinal>> forecastFinalsInternalMap = new HashMap<Integer, List<ForecastFinal>>();
				forecastFtoSourceMap.put(ff.getMatch().getId(), forecastFinalsInternalMap);
			}

			if(forecastFtoSourceMap.get(ff.getMatch().getId()).get(ff.getForecastTypeOccurrence().getId())==null)
			{
				List<ForecastFinal> forecastFinalsList = new ArrayList<ForecastFinal>();
				forecastFtoSourceMap.get(ff.getMatch().getId()).put(ff.getForecastTypeOccurrence().getId(), forecastFinalsList);
			}
			

			forecastFtoSourceMap.get(ff.getMatch().getId()).get(ff.getForecastTypeOccurrence().getId()).add(ff);
		
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
		for(Map.Entry<Integer,Map<Integer,List<ForecastFinal>>> entry  : forecastFtoSourceMap.entrySet())
		{
			//FTO
			for(Map.Entry<Integer,List<ForecastFinal>> internalEntry  : entry.getValue().entrySet())
			{
				//SOURCE
					List<ForecastFinal> listToOrder = internalEntry.getValue();
					if(listToOrder.size()>1)
					{
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
//						forecastFinal.setSource(vb.getForecast().getSource());
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
						log.info(forecastFinal.toString());
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
				log.info("Final Forecast FOUND! Source: " + vb.getForecast().getSource().getDescription() + " Match: " + vb.getForecast().getMatch().getHomeTeam().getName() + " - " + vb.getForecast().getMatch().getAwayTeam().getName()
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
			double verifiedPerc = Double.valueOf(fs.getNumVerified())/Double.valueOf(fs.getNumOccurrences());
			sum = sum + verifiedPerc;
		}
		double average = sum/forecastSummaries.size();

		double powerValueSum = 0.0;
		for(ForecastSummary fs : forecastSummaries)
		{
			double verifiedPerc = Double.valueOf(fs.getNumVerified())/Double.valueOf(fs.getNumOccurrences());
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
		
		Double sqmaverage = 0.0;
		try
		{
			sqmaverage = Double.valueOf(df.format(sqm/average).replace(",", "."));
			
		}catch(NumberFormatException ex)
		{
			log.warn("NFE" + ex.getMessage());
		}
		return sqmaverage;
	}	
	
	
	
	
}
