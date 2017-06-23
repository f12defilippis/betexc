package com.bonde.betbot.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bonde.betbot.service.datacollection.ForecastService;
import com.bonde.betbot.service.datacollection.OddService;
import com.bonde.betbot.service.datacollection.ResultService;
import com.bonde.betbot.service.datacollection.ValueBetService;
import com.bonde.betbot.util.DateUtil;
import com.bonde.betbot.util.Threshold;

@Service
public class AsyncService {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	
	@Autowired
	private ForecastService forecastService;

	@Autowired
	private ResultService resultService;

	@Autowired
	private OddService oddService;	

	@Autowired
	private ValueBetService valueBetService;	

	@Autowired
	private BestValueFinderService bestValueFinderService;	
	
	@Autowired
	private ProbabilityService probabilityService;	

	@Async
    public String calculateFinal(String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		while(date.before(new Date()))
		{
			
			log.info("*****************************");
			log.info("*****************************");
			log.info("*****NEW DAY*****************");
			
			
			probabilityService.findProbabilities(date);
			

			log.info("*****************************");
			log.info("*****************************");
			
			
			date = DateUtil.addDaysToDate(date, 1);
			
		}		
		
		
		
		
		return "OK!";
    }		
	
	
	
	
	
	
	
	@Async
    public String forsummary(String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		while(date.before(new Date()))
		{
			
			log.info("*****************************");
			log.info("*****************************");
			log.info("*****NEW DAY*****************");
			
			
			bestValueFinderService.calculateAdjustment(date);
			

			log.info("*****************************");
			log.info("*****************************");
			
			
			date = DateUtil.addDaysToDate(date, Threshold.SUMMARY_FREQUENCY);
			
		}		
		
		
		
		
		return "OK!";
    }		
	
	
	
	@Async
    public String allfromyear(String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date today;
		try {
			today = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		Date yesterday = DateUtil.addDaysToDate(today, -1);
		Date tomorrow = DateUtil.addDaysToDate(today, 1);
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDatePickForWin = "2016/09/01";
		String strDateProsoccer = "2016/09/02";
		
		Date datePickForWin = format.parse(strDatePickForWin);
		Date dateProsoccer = format.parse(strDateProsoccer);
		
		Date now = new Date();
		Date dateStatarea = DateUtil.addDaysToDate(now, -4);
		Date dateVitibet = DateUtil.addDaysToDate(now, -1);
		
		
		while(today.before(new Date()))
		{
			
			log.info("*****************************");
			log.info("*****************************");
			log.info("*****NEW DAY*****************");
			
			
			oddService.getBettingTips1X2Odds(today);
			oddService.getBettingTips1X2Odds(tomorrow);
			
			forecastService.getSciBetForecast(today);
			forecastService.getZulubetForecast(today);
			forecastService.getMyBetForecast(today);

			if(today.after(datePickForWin))
			{
				forecastService.getPickForWinForecast(today);
			}
			if(today.after(dateProsoccer))
			{
				forecastService.getProSoccerForecast(today);
			}
			if(today.after(dateStatarea))
			{
				forecastService.getStatareaForecast(today);
			}
			if(today.after(dateVitibet))
			{
				forecastService.getVitibetForecast(today);
			}

			log.info("Starting calculating value bets for date " + DateUtil.fromDateToString(today));
			
			int ret = valueBetService.calculateValueBet(today);

			log.info("Calculated " + ret + " ValueBets for date: " + DateUtil.fromDateToString(today));			
			
			resultService.getLivescore24Results(yesterday);

			log.info("*****************************");
			log.info("*****************************");
			
			today = DateUtil.addDaysToDate(today, 1);
			yesterday = DateUtil.addDaysToDate(today, -1);
			tomorrow = DateUtil.addDaysToDate(today, 1);

		}
		

		
		
		
		return "OK!";
    }
	

	
	@Async
    public String prosoccerbulk(String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDateProsoccer = "2016/09/02";
		
		Date dateProsoccer = format.parse(strDateProsoccer);
		
		while(date.before(new Date()))
		{
			
			log.info("*****************************");
			log.info("*****************************");
			log.info("*****NEW DAY*****************");
			
			
			if(date.after(dateProsoccer))
			{
				forecastService.getProSoccerForecast(date);
			}
			

			log.info("*****************************");
			log.info("*****************************");
			
			
			date = DateUtil.addDaysToDate(date, 1);
			
		}
		

		
		
		
		return "OK!";
    }
	
	@Async
    public String valuebetbulk(String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		while(date.before(new Date()))
		{
			log.info("*****************************");
			log.info("*****************************");
			log.info("*****NEW DAY*****************");

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(date);
			try{
				
				log.info("Starting calculating value bets for date " + strDate);
				
				int ret = valueBetService.calculateValueBet(date);

				log.info("Calculated " + ret + " ValueBets for date: " + strdate);			
				log.info("*****************************");
				log.info("*****************************");
			}catch(Exception e)
			{
				log.warn("Exception occurred calculating value bets for date " + strDate + ":" + e.getMessage());
			}
			
			date = DateUtil.addDaysToDate(date, 1);
		}
		
		return "OK!";
    }	
	
	
	
	
	
}
