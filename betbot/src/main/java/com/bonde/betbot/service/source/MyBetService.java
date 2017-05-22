package com.bonde.betbot.service.source;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.service.CrawlerService;
import com.bonde.betbot.util.DateUtil;

public class MyBetService  extends CrawlerService{


	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlForecast(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String strDate = format.format(date);
		
		String month = DateUtil.getMonth(Integer.valueOf(strDate.split("-")[0]));
		
		String finalDate = month + "-" + strDate.split("-")[1] + "-" + strDate.split("-")[2];
		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.MYBET));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("https://mybet.tips/soccer/predictions/" + finalDate + "/");
		
//		Elements competitions = document.select("table[class^=content_table]");

		Elements matchRows = document.select("tr[class^=link]");
		for(Element matchRow : matchRows)
		{
			try
			{
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				Elements matchFields = matchRow.select("td");
				
//				String hour = matchFields.get(0).select("noscript").text().split(",")[1].trim();
				String homeTeam = matchFields.get(1).text().trim();
				String awayTeam = matchFields.get(3).text().trim();
				String pred1 = matchFields.get(4).text().replace("%", "");
				String predX = matchFields.get(5).text().replace("%", "");
				String pred2 = matchFields.get(6).text().replace("%", "");

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
