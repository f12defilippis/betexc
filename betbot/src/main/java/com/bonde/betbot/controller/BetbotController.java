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
import com.bonde.betbot.service.ResultService;
import com.bonde.betbot.service.TeamDiscoveryService;
import com.bonde.betbot.util.DateUtil;

@RestController
public class BetbotController {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	private ForecastService forecastService;

	@Autowired
	private ResultService resultService;

	@Autowired
	private TeamDiscoveryService teamDiscoveryService;
	
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
		
		while(date.before(new Date()))
		{
			
			log.info("*****************************");
			log.info("*****************************");
			log.info("*****NEW DAY*****************");
			
			
			forecastService.getSciBetForecast(date);
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
	
	
	@RequestMapping("/csv")
	public String csv()
	{
		teamDiscoveryService.saveTeamFromXls();
		return "OK";
	}
	
	
	
}
