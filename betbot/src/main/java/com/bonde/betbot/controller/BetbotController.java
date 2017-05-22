package com.bonde.betbot.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonde.betbot.service.ForecastService;
import com.bonde.betbot.service.OddService;
import com.bonde.betbot.service.ResultService;
import com.bonde.betbot.service.TeamDiscoveryService;
import com.bonde.betbot.service.ValueBetService;
import com.bonde.betbot.util.DateUtil;

@RestController
public class BetbotController {

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
	private TeamDiscoveryService teamDiscoveryService;

	
	@RequestMapping("/scanprosoccer")
    private String scanprosoccer(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		forecastService.getProSoccerForecast(date);
		
		return "OK!";
    }	
	
	@RequestMapping("/scanstatarea")
    private String test(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		forecastService.getStatareaForecast(date);
		
		return "OK!";
    }
	
	@RequestMapping("/scanscibet")
    private String scanscibet(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		forecastService.getSciBetForecast(date);
		
		return "OK!";
    }
	
	@RequestMapping("/scanzulubet")
    private String scanzulubet(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		forecastService.getZulubetForecast(date);
		
		return "OK!";
    }

	
	@RequestMapping("/allfromyearsc")
    private String allfromyear(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

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
	
	
	
	@RequestMapping("/scanresults")
    private String scanresults(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		resultService.getLivescoreResults(date);
		
		return "OK!";
    }
	
	@RequestMapping("/scanodds")
    private String scanodds(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		oddService.getBettingTips1X2Odds(date);
		
		return "OK!";
    }	
	
	@RequestMapping("/scanvaluebet")
    private String scanvaluebet(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		int ret = valueBetService.calculateValueBet(date);
		
		log.info("Calculated " + ret + " ValueBets for date: " + strdate);
		
		return "OK!";
    }		
	
	
	
	
	@RequestMapping("/csv")
	public String csv()
	{
		teamDiscoveryService.saveTeamFromXls();
		return "OK";
	}
	
	
	
}
