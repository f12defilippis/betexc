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

@Service
public class ProSoccerService  extends CrawlerService{


	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlForecast(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		
		String year = strDate.split("-")[0];
		String month = strDate.split("-")[1];
		
		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.PROSOCCER));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.prosoccer.gr/en/" + year + "/" + month + "/soccer-predictions-" + strDate + ".html");
		
		Elements competitions = document.select("tbody");

		Elements matchRows = competitions.select("tr");
		for(Element matchRow : matchRows)
		{
			try
			{
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				Elements matchFields = matchRow.select("td");
				
//				String hour = matchFields.get(0).select("noscript").text().split(",")[1].trim();
				String homeTeam = matchFields.get(2).text().substring(0, matchFields.get(2).text().indexOf("-")-1);
				String awayTeam = matchFields.get(2).text().substring(matchFields.get(2).text().indexOf("-")+2, matchFields.get(2).text().length());
				String pred1 = matchFields.get(3).text().replace("\"", "").replace("::before", "").trim();
				String predX = matchFields.get(4).text().replace("\"", "").replace("::before", "").trim();
				String pred2 = matchFields.get(5).text().replace("\"", "").replace("::before", "").trim();

				
				
				
				
				row.setDate(date);
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
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
}
