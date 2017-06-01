package com.bonde.betbot.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.Competition;
import com.bonde.betbot.model.domain.FatForecast;
import com.bonde.betbot.model.domain.FattestMatch;
import com.bonde.betbot.model.domain.ForecastSummary;
import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.ForecastValueGroup;
import com.bonde.betbot.model.domain.Result;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.ValueBet;
import com.bonde.betbot.model.domain.ValueGroup;
import com.bonde.betbot.model.dto.AdjustmentValueTO;
import com.bonde.betbot.model.dto.AdjustmentVariableKeyTO;
import com.bonde.betbot.repository.FattestMatchRepository;
import com.bonde.betbot.repository.ForecastSummaryRepository;
import com.bonde.betbot.repository.ForecastValueGroupRepository;
import com.bonde.betbot.util.DateUtil;
import com.bonde.betbot.util.Threshold;

@Service
public class BestValueFinderService {

	@Autowired
	private FattestMatchRepository fattestMatchRepository;

	@Autowired
	private ForecastValueGroupRepository forecastValueGroupRepository;
	
	@Autowired
	private ForecastSummaryRepository forecastSummaryRepository;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  
	
	@Transactional
	public void calculateAdjustment(Date date)
	{
		log.info("Calculating adjustment for date " + DateUtil.fromDateToString(date));
				
		List<FattestMatch> matchList = fattestMatchRepository.findByDateStartBetween(DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE * -1), date);

		if(matchList!=null && matchList.size()>0)
		{
			log.info("Found " + matchList.size() + " matches in the interval from " + DateUtil.fromDateToString(DateUtil.addDaysToDate(date, Threshold.DAYS_BEFORE * -1)) + " and " + DateUtil.fromDateToString(date));
		}
		
		Map<Integer,Map<AdjustmentVariableKeyTO,AdjustmentValueTO>> map = new HashMap<Integer,Map<AdjustmentVariableKeyTO,AdjustmentValueTO>>();
		
		log.info("Starting calculating summaries....");
		int matchCounter = 0;
		for(FattestMatch match : matchList)
		{
			if(matchCounter % 100 == 0)
			{
				log.info("Calculated summaries for " + matchCounter + " matches");
			}
			matchCounter++;
			
			if(match.getResults()!=null && match.getResults().size() > 0)
			{
				for(FatForecast forecast : match.getForecasts())
				{
					boolean verifiedForecast = verifyForecast(forecast, match);
					
					if(map.get(forecast.getSource().getId())==null)
					{
						Map<AdjustmentVariableKeyTO,AdjustmentValueTO> internalMap = new HashMap<AdjustmentVariableKeyTO,AdjustmentValueTO>();
						map.put(forecast.getSource().getId(), internalMap);
					}
					Map<AdjustmentVariableKeyTO,AdjustmentValueTO> internalMap = map.get(forecast.getSource().getId());

					//CALCULATE EVENTS BY FORECAST VALUE
					AdjustmentVariableKeyTO forecastKey = new AdjustmentVariableKeyTO();
					forecastKey.setForecastValue(forecast.getForecastValue().getId());
					updateMap(internalMap, forecastKey, verifiedForecast);
					
					generateCombination(forecast, internalMap, forecastKey, verifiedForecast, match);

					List<ForecastValueGroup> forecastValueGroups = forecastValueGroupRepository.findByForecastValue(forecast.getForecastValue());

					
					for(ForecastValueGroup fvg : forecastValueGroups)
					{
						AdjustmentVariableKeyTO forecastKeyGroup = new AdjustmentVariableKeyTO();
						forecastKeyGroup.setForecastValueGroup(fvg.getValueGroup().getId());
						updateMap(internalMap, forecastKeyGroup, verifiedForecast);
						
						generateCombination(forecast, internalMap, forecastKeyGroup, verifiedForecast, match);
					}
				}
			}
		}
		
		int saved = 0;
		for (Map.Entry<Integer,Map<AdjustmentVariableKeyTO,AdjustmentValueTO>> entry : map.entrySet()) {
		    Integer source = entry.getKey();
		    Map<AdjustmentVariableKeyTO,AdjustmentValueTO> internalMap = entry.getValue();
		    for(Map.Entry<AdjustmentVariableKeyTO,AdjustmentValueTO> internalEntry : internalMap.entrySet())
		    {
		    	AdjustmentVariableKeyTO key = internalEntry.getKey();
		    	AdjustmentValueTO value = internalEntry.getValue();
		    	if(value.getTotalEvents()>=Threshold.MINIMUM_EVENTS)
		    	{
			    	ForecastSummary fs = new ForecastSummary();
			    	fs.setFinalDate(date);
			    	fs.setDaysBefore(Threshold.DAYS_BEFORE);
			    	fs.setSource(new Source(source));
			    	if(key.getForecastValue()!=null)
			    	{
				    	fs.setForecastValue(new ForecastValue(key.getForecastValue()));
			    	}
			    	
			    	if(key.getForecastValueGroup()!=null)
			    	{
				    	fs.setValueGroup(new ValueGroup(key.getForecastValueGroup()));
			    	}
			    	
			    	if(key.getCompetition()!=null)
			    	{
				    	fs.setCompetition(new Competition(key.getCompetition()));
			    	}
			    	
			    	if(key.getValueBet()!=null)
			    	{
				    	fs.setValueBet(new ForecastValue(key.getValueBet()));
			    	}
			    	
			    	if(key.getValueBetGroup()!=null)
			    	{
			    		fs.setValueBetGroup(new ValueGroup(key.getValueBetGroup()));
			    	}
			    	
			    	if(key.getForecastTypeOccurrence()!=null)
			    	{
			    		fs.setForecastTypeOccurrence(new ForecastTypeOccurrence(key.getForecastTypeOccurrence()));
			    	}

			    	fs.setNumVerified(value.getEventsVerified());
			    	fs.setNumOccurrences(value.getTotalEvents());
			    	
			    	forecastSummaryRepository.save(fs);
			    	log.info("Saved : " + fs.toString());
			    	saved++;
		    	}else
		    	{
		    		log.debug("Skipping entry because there are only " + value.getTotalEvents() + " events");
		    	}
		    	
		    	
		    }
		    log.info("Finished calculating. Saved " + saved + " ForecastSummary entries");
		    
		}		
		

		
	}
	
	
	private void generateCombination(FatForecast forecast, Map<AdjustmentVariableKeyTO,AdjustmentValueTO> internalMap, AdjustmentVariableKeyTO forecastKey, boolean verifiedForecast, FattestMatch match)
	{
		AdjustmentVariableKeyTO forecastTypeKey = new AdjustmentVariableKeyTO(forecastKey);
		forecastTypeKey.setForecastTypeOccurrence(forecast.getForecastTypeOccurrence().getId());
		updateMap(internalMap, forecastTypeKey, verifiedForecast);


		
		//COMPETITION
		AdjustmentVariableKeyTO forecastCompetitionKey = new AdjustmentVariableKeyTO(forecastKey);
		AdjustmentVariableKeyTO forecastCompetitionTypeKey = new AdjustmentVariableKeyTO(forecastTypeKey);
		if(match.getCompetition()!=null)
		{
			forecastCompetitionKey.setCompetition(match.getCompetition().getId());
			updateMap(internalMap, forecastCompetitionKey, verifiedForecast);

			forecastCompetitionTypeKey.setCompetition(match.getCompetition().getId());
			updateMap(internalMap, forecastCompetitionTypeKey, verifiedForecast);
		}
		
		
		//VALUE BET
		for(ValueBet vb : forecast.getValueBets())
		{
			AdjustmentVariableKeyTO forecastValueBetKey = new AdjustmentVariableKeyTO(forecastKey);
			forecastValueBetKey.setValueBet(vb.getMargin().getId());
			updateMap(internalMap, forecastValueBetKey, verifiedForecast);

			AdjustmentVariableKeyTO forecastValueBetAndTypeKey = new AdjustmentVariableKeyTO(forecastTypeKey);
			forecastValueBetAndTypeKey.setValueBet(vb.getMargin().getId());
			updateMap(internalMap, forecastValueBetAndTypeKey, verifiedForecast);
			
			if(match.getCompetition()!=null)
			{
				AdjustmentVariableKeyTO forecastValueBetCompetitionKey = new AdjustmentVariableKeyTO(forecastCompetitionKey);
				forecastValueBetCompetitionKey.setValueBet(vb.getMargin().getId());
				updateMap(internalMap, forecastValueBetCompetitionKey, verifiedForecast);

				AdjustmentVariableKeyTO forecastValueBetAndTypeCompetitionKey = new AdjustmentVariableKeyTO(forecastCompetitionTypeKey);
				forecastValueBetAndTypeCompetitionKey.setValueBet(vb.getMargin().getId());
				updateMap(internalMap, forecastValueBetAndTypeCompetitionKey, verifiedForecast);
			}

			List<ForecastValueGroup> valueBetGroupList = forecastValueGroupRepository.findByForecastValue(vb.getMargin());
			for(ForecastValueGroup fvg : valueBetGroupList)
			{
				AdjustmentVariableKeyTO forecastValueBetKeyGroup = new AdjustmentVariableKeyTO(forecastKey);
				forecastValueBetKeyGroup.setValueBetGroup(fvg.getValueGroup().getId());
				updateMap(internalMap, forecastValueBetKeyGroup, verifiedForecast);

				AdjustmentVariableKeyTO forecastValueBetAndTypeKeyGroup = new AdjustmentVariableKeyTO(forecastTypeKey);
				forecastValueBetAndTypeKeyGroup.setValueBetGroup(fvg.getValueGroup().getId());
				updateMap(internalMap, forecastValueBetAndTypeKeyGroup, verifiedForecast);
				
				if(match.getCompetition()!=null)
				{
					AdjustmentVariableKeyTO forecastValueBetCompetitionKeyGroup = new AdjustmentVariableKeyTO(forecastCompetitionKey);
					forecastValueBetCompetitionKeyGroup.setValueBetGroup(fvg.getValueGroup().getId());
					updateMap(internalMap, forecastValueBetCompetitionKeyGroup, verifiedForecast);

					AdjustmentVariableKeyTO forecastValueBetAndTypeCompetitionKeyGroup = new AdjustmentVariableKeyTO(forecastCompetitionTypeKey);
					forecastValueBetAndTypeCompetitionKeyGroup.setValueBetGroup(fvg.getValueGroup().getId());
					updateMap(internalMap, forecastValueBetAndTypeCompetitionKeyGroup, verifiedForecast);
				}
			}
		
		
		
		}
		
	}
	
	
	
	
	
	private void updateMap(Map<AdjustmentVariableKeyTO,AdjustmentValueTO> internalMap, AdjustmentVariableKeyTO key, boolean verifiedForecast)
	{
		if(internalMap.get(key)==null)
		{
			internalMap.put(key, new AdjustmentValueTO());
		}

		internalMap.get(key).addEvents(verifiedForecast);
		log.debug("Added " + verifiedForecast + " to internalMapValue " + key.toString());
	}
	
	
	

	private boolean verifyForecast(FatForecast forecast, FattestMatch match)
	{
		boolean verifiedForecast = false;
		for(Result r : match.getResults())
		{
			if(r.getForecastTypeOccurrence().getId() == forecast.getForecastTypeOccurrence().getId())
			{
				verifiedForecast = true;
			}
		}
		return verifiedForecast;
	}
	
	




}
