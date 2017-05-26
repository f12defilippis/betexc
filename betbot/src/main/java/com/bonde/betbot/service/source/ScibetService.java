package com.bonde.betbot.service.source;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.service.datacollection.CrawlerService;
import com.bonde.betbot.util.DateUtil;

@Service
public class ScibetService extends CrawlerService{

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlForecast(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.SCIBET));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.scibet.com/football/" + strDate+ "/");
		
		Elements competitions = document.select("div[class^=content np]");

		for(Element competitionMatches : competitions)
		{
			Elements matchRows = competitionMatches.select("tr");
			for(Element matchRow : matchRows)
			{
				try
				{
					ForecastMatchRowTO row = new ForecastMatchRowTO();
					Elements matchFields = matchRow.select("td");
					
					String hour = matchFields.get(1).text().split(" ")[1];
					String homeTeam = matchFields.get(2).select("a").get(0).text();
					String awayTeam = matchFields.get(4).select("a").get(0).text();
					String pred1 = adjustBarValue(matchRow.select("div[class^=bar bar-success]").get(0).attr("style"));
					String predX = adjustBarValue(matchRow.select("div[class^=bar bar-warning]").get(0).attr("style"));
					String pred2 = adjustBarValue(matchRow.select("div[class^=bar bar-danger]").get(0).attr("style"));

					row.setDate(DateUtil.addHourToDateandAddHours(date, hour, 2));
					row.setPred1(pred1);
					row.setPredX(predX);
					row.setPred2(pred2);
					row.setHomeTeam(homeTeam);
					row.setAwayTeam(awayTeam);		
					ret.getRows().add(row);
				}catch(StringIndexOutOfBoundsException ex)
				{
					log.warn("NumberFormatException during read match row: " + ex.getMessage());
				}
				
			}
		}
		return ret;
	}
	
	
	public ForecastScan crawlResult(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		
		
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.SCIBET));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.scibet.com/football/" + strDate+ "/");
		
		Elements competitions = document.select("div[class^=content np]");

		for(Element competitionMatches : competitions)
		{
			Elements matchRows = competitionMatches.select("tr");
			for(Element matchRow : matchRows)
			{
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				Elements matchFields = matchRow.select("td");
				
				String hour = matchFields.get(1).text().split(" ")[1];
				String homeTeam = matchFields.get(2).select("a").get(0).text();
				String awayTeam = matchFields.get(4).select("a").get(0).text();

				row.setDate(DateUtil.dateManagement(date, hour));
				row.setHomeTeam(homeTeam);
				row.setAwayTeam(awayTeam);		
				ret.getRows().add(row);
				
			}
		}
		return ret;
	}	
	
	
	private String adjustBarValue(String barValue)
	{
		barValue = barValue.replaceAll("width:", "");
		barValue = barValue.substring(0, barValue.indexOf("."));
		return barValue;
	}
	
	
	
	
	
}
