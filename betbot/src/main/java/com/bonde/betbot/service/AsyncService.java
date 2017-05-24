package com.bonde.betbot.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bonde.betbot.util.DateUtil;

@Service
public class AsyncService {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	
	@Autowired
	private ForecastService forecastService;

	@Autowired
	private ResultService resultService;

	@Autowired
	private OddService oddService;	

	@Async
    public String allfromyear(String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDatePickForWin = "2016/09/01";
		String strDateProsoccer = "2016/09/02";
		
		Date datePickForWin = format.parse(strDatePickForWin);
		Date dateProsoccer = format.parse(strDateProsoccer);
		
		while(date.before(new Date()))
		{
			
			log.info("*****************************");
			log.info("*****************************");
			log.info("*****NEW DAY*****************");
			
			
			oddService.getBettingTips1X2Odds(date);
			
			forecastService.getSciBetForecast(date);
			forecastService.getZulubetForecast(date);
			forecastService.getMyBetForecast(date);

			if(date.after(datePickForWin))
			{
				forecastService.getPickForWinForecast(date);
			}
			if(date.after(dateProsoccer))
			{
				forecastService.getProSoccerForecast(dateProsoccer);
			}
			
			resultService.getLivescoreResults(date);

			log.info("*****************************");
			log.info("*****************************");
			
			
			date = DateUtil.addDaysToDate(date, 1);
			
		}
		

		
		
		
		return "OK!";
    }
	
	
	
}
