package com.bonde.betbot.service.datacollection;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.FatMatch;
import com.bonde.betbot.model.domain.Forecast;
import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.Odd;
import com.bonde.betbot.model.domain.ValueBet;
import com.bonde.betbot.model.dto.ValueBetKeyTO;
import com.bonde.betbot.model.dto.ValueBetTO;
import com.bonde.betbot.repository.FatMatchRepository;
import com.bonde.betbot.repository.ForecastValueRepository;
import com.bonde.betbot.repository.ValueBetRepository;
import com.bonde.betbot.util.DateUtil;

@Service
public class ValueBetService {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass()); 	
	
	@Autowired
	private ForecastValueRepository forecastValueRepository;
	
	@Autowired
	private ValueBetRepository valueBetRepository;

	@Autowired
	private FatMatchRepository fatMatchRepository;
	
	
	@Transactional
	public int calculateValueBet(Date date)
	{		
		
		int ret = 0;
		
		List<FatMatch> fatMatches = fatMatchRepository.findByDateStartBetween(date,DateUtil.addDaysToDate(date, 1));
		
		
		Map<ValueBetKeyTO,ValueBetTO> map = new HashMap<ValueBetKeyTO,ValueBetTO>();
		
		for(FatMatch fm : fatMatches)
		{
			for(Forecast forecast : fm.getForecasts())
			{
				ValueBetKeyTO key = new ValueBetKeyTO(fm.getId(),forecast.getSource().getId(),forecast.getForecastTypeOccurrence().getId());
				if(map.get(key)!=null)
				{
					map.get(key).setForecast(forecast);
				}else
				{
					ValueBetTO to = new ValueBetTO();
					to.setForecast(forecast);
					map.put(key, to);
				}
				log.debug("NEW FORECAST: " + key.toString());
			}
			for(Odd odd : fm.getOdds())
			{
				for (Map.Entry<ValueBetKeyTO,ValueBetTO> entry : map.entrySet()) {
					ValueBetKeyTO key = entry.getKey();
					if(key.getMatchId().equals(fm.getId()) && key.getForecastTypeOccurrenceId().equals(odd.getForecastTypeOccurrence().getId()))
					{
						map.get(key).setOdd(odd);
					}
					log.debug("MATCH:" + key.getMatchId().equals(fm.getId()) + " FOR: " + key.getForecastTypeOccurrenceId().equals(odd.getForecastTypeOccurrence().getId()));
					log.debug("MATCH:" + key.getMatchId() + " FM: " + fm.getId());
				}
			}
		}

		
		
		
		for (Map.Entry<ValueBetKeyTO,ValueBetTO> entry : map.entrySet()) {
//			Match match = entry.getKey();

			try
			{
				ValueBetTO to = entry.getValue();
				if(to.getForecast()!=null && to.getOdd()!=null)
				{
					Forecast forecast = to.getForecast();
					Odd odd = to.getOdd();
					
					double forecastValue = forecast.getForecastValue().getValue();
					
					double oddValue = odd.getFirstValue();

					DecimalFormat df = new DecimalFormat("#.##");
					df.setRoundingMode(RoundingMode.CEILING);			
					
					double forecastInOdd = Double.valueOf(df.format(forecastValue).replace(",", "."));
					
					
//					double oddForecast = 1/oddValue;
					
					
					
//					double oddForecast = Double.valueOf(df.format(1/oddValue).replace(",", "."));
					double oddMargin = Double.valueOf(df.format((oddValue-forecastInOdd)/oddValue).replace(",", "."));
					List<ForecastValue> fvlist = forecastValueRepository.findByValue(oddMargin);			

					ForecastValue margin = new ForecastValue();
					if(fvlist!=null && fvlist.size()>0)
					{
						margin = fvlist.get(0);
					}else
					{
						margin.setValue(oddMargin);
						forecastValueRepository.save(margin);
					}
					
					ValueBet valueBet = new ValueBet();
					
					List<ValueBet> listValueBet = valueBetRepository.findByOddAndForecast(odd, forecast);
					
					if(listValueBet!=null && listValueBet.size()>0)
					{
						valueBet = listValueBet.get(0);
					}else
					{
						valueBet.setForecast(forecast);
						valueBet.setOdd(odd);
						valueBet.setDateCreated(new Date());
					}
					
					double expectedOdd = Double.valueOf(df.format(1/forecastValue).replace(",", "."));
					
					valueBet.setExpectedOdd(expectedOdd);
					valueBet.setMargin(margin);
					
					valueBet.setDateUpdated(new Date());
					
					valueBetRepository.save(valueBet);
					ret++;
				}
			}catch(NumberFormatException ex)
			{
				log.warn("Exception: " + ex.getMessage());
			}
			
		}
		
		return ret;
		
		
		
	}
	
	
	
}
