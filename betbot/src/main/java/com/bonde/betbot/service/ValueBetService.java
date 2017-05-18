package com.bonde.betbot.service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.Forecast;
import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Odd;
import com.bonde.betbot.model.domain.ValueBet;
import com.bonde.betbot.model.dto.ValueBetTO;
import com.bonde.betbot.repository.ForecastRepository;
import com.bonde.betbot.repository.ForecastValueRepository;
import com.bonde.betbot.repository.OddRepository;
import com.bonde.betbot.repository.ValueBetRepository;
import com.bonde.betbot.util.DateUtil;

@Service
public class ValueBetService {

	@Autowired
	private OddRepository oddRepository;

	@Autowired
	private ForecastRepository forecastRepository;
	
	@Autowired
	private ForecastValueRepository forecastValueRepository;
	
	@Autowired
	private ValueBetRepository valueBetRepository;

	@Transactional
	public int calculateValueBet(Date date)
	{
		int ret = 0;
		List<Forecast> forecasts = forecastRepository.findByMatchlazyDateStartBetween(date,DateUtil.addDaysToDate(date, 1));
		List<Odd> odds = oddRepository.findByMatchlazyDateStartBetween(date,DateUtil.addDaysToDate(date, 1));
		
		
		
		Map<Match,ValueBetTO> map = new HashMap<Match,ValueBetTO>();
		
		
		for(Odd odd : odds)
		{
			odd = oddRepository.findOne(odd.getId());
			ValueBetTO to = new ValueBetTO();
			to.setOdd(odd);
			
			map.put(odd.getMatch(), to);
		}

		for(Forecast forecast : forecasts)
		{
			forecast = forecastRepository.findOne(forecast.getId());
			if(map.get(forecast.getMatch())!=null)
			{
				map.get(forecast.getMatch()).setForecast(forecast);
			}
		}

		for (Map.Entry<Match,ValueBetTO> entry : map.entrySet()) {
//			Match match = entry.getKey();
			ValueBetTO to = entry.getValue();
			Forecast forecast = to.getForecast();
			Odd odd = to.getOdd();
			
			double forecastValue = forecast.getForecastValue().getValue();
			
			double oddValue = odd.getFirstValue();
			
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);			
			
//			double oddForecast = Double.valueOf(df.format(1/oddValue));
			List<ForecastValue> fvlist = forecastValueRepository.findByValue(forecastValue-oddValue);			

			ForecastValue margin = new ForecastValue();
			if(fvlist!=null && fvlist.size()>0)
			{
				margin = fvlist.get(0);
			}else
			{
				margin.setValue(forecastValue-oddValue);
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
			
			valueBet.setExpectedOdd(1/forecastValue);
			valueBet.setMargin(margin);
			
			valueBet.setDateUpdated(new Date());
			
			valueBetRepository.save(valueBet);
			ret++;
		}
		
		return ret;
		
		
		
	}
	
	
	
}
