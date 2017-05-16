package com.bonde.betbot.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bonde.betbot.service.ForecastService;
import com.bonde.betbot.service.TeamDiscoveryService;
import com.bonde.betbot.service.source.StatareaService;

@RestController
public class BetbotController {

	
	@Autowired
	private ForecastService forecastService;
	
	@Autowired
	private TeamDiscoveryService teamDiscoveryService;
	
	@Autowired
	private StatareaService statareaService;
	
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
	
	@RequestMapping("/csv")
	public String csv()
	{
		teamDiscoveryService.saveTeamFromXls();
		return "OK";
	}

	@RequestMapping("/teamstatarea")
    private String teamstatarea(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		statareaService.saveTeams(date);
		
		return "OK!";
    }

	
	
	
}
