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

import com.bonde.betbot.service.AsyncService;
import com.bonde.betbot.service.ValueGroupService;
import com.bonde.betbot.service.datacollection.ForecastService;
import com.bonde.betbot.service.datacollection.OddService;
import com.bonde.betbot.service.datacollection.ResultService;
import com.bonde.betbot.service.datacollection.TeamDiscoveryService;
import com.bonde.betbot.service.datacollection.ValueBetService;

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

	@Autowired
	private AsyncService asyncService;

	@Autowired
	private ValueGroupService valueGroupService;

	
	
	@RequestMapping("/fillValueGroup")
    private String fillValueGroup() throws Exception{

		
		valueGroupService.fillValueGroup();
		
		return "OK!";
    }	

	@RequestMapping("/forsummary")
    private String forsummary(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		asyncService.forsummary(strdate);
		
		return "OK!";
    }	
	
	@RequestMapping("/calculateFinal")
    private String calculateFinal(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		String forecastSummaryParameter[] = {
				"FV0-VB0-FTOY",
				"FV0-VB2-FTOY",
				"FV0-VB5-FTOY",
				"FV2-VB2-FTOY",
				"FV2-VB5-FTOY",
				"FV2-VB10-FTOY",
				"FV5-VB2-FTOY",
				"FV5-VB5-FTOY",
				"FV5-VB10-FTOY",
				"FV10-VB2-FTOY",
				"FV10-VB5-FTOY",
				"FV10-VB10-FTOY",
				"FV0-VB0-FTON",
				"FV0-VB2-FTON",
				"FV0-VB5-FTON",
				"FV2-VB2-FTON",
				"FV2-VB5-FTON",
				"FV2-VB10-FTON",
				"FV5-VB2-FTON",
				"FV5-VB5-FTON",
				"FV5-VB10-FTON",
				"FV10-VB2-FTON",
				"FV10-VB5-FTON",
				"FV10-VB10-FTON"
		};
		
		
		for(int i = 0 ; i< forecastSummaryParameter.length ; i++)
		{
			asyncService.calculateFinal(strdate,forecastSummaryParameter[i]);
		}
		
		return "OK!";
    }	
	
	
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
	
	@RequestMapping("/scanpick")
    private String scanpick(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		oddService.getBettingTips1X2Odds(date);
		forecastService.getPickForWinForecast(date);
		
		return "OK!";
    }
	
	@RequestMapping("/scanvitibet")
    private String scanvitibet(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = formatDateHour.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}		
		
		oddService.getBettingTips1X2Odds(date);
		forecastService.getVitibetForecast(date);
		
		return "OK!";
    }

	
	@RequestMapping("/allfromyearsc")
    private String allfromyear(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		asyncService.allfromyear(strdate);
		
		return "OK!";
    }
	
	@RequestMapping("/prosoccerbulk")
    private String prosoccerbulk(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		asyncService.prosoccerbulk(strdate);
		
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
		
		resultService.getLivescore24Results(date);
		
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
	
	@RequestMapping("/scanvaluebetbulk")
    private String scanvaluebetbulk(HttpServletRequest req, @RequestParam(value="date", defaultValue="World") String strdate) throws Exception{

		asyncService.valuebetbulk(strdate);
		
		return "OK!";
    }		
	
	
	
	@RequestMapping("/csv")
	public String csv()
	{
		teamDiscoveryService.saveTeamFromXls();
		return "OK";
	}
	
	
	
}
