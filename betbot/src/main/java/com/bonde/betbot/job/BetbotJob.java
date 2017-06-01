package com.bonde.betbot.job;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bonde.betbot.service.AsyncService;
import com.bonde.betbot.util.DateUtil;

@Component
public class BetbotJob {

	@Autowired
	private AsyncService asyncService;
	
	@Scheduled(cron="${scheduled.inaday1}")
	public void goOddForecastValueBetAndResult() throws Exception
	{
		Date now = new Date();
		
		String strdate = DateUtil.fromDateToString(now);
		
		asyncService.allfromyear(strdate);
	}
	
	
	
	
	
}
