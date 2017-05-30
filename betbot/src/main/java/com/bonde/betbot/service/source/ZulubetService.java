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
public class ZulubetService extends CrawlerService{

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlForecast(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = format.format(date);
		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.ZULUBET));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.zulubet.com/tips-" + strDate+ ".html");
		
		Elements competitions = document.select("table[class^=content_table]");

		Elements matchRows = competitions.select("tr[bgcolor^=#EFEFEF]");
		matchRows.addAll(competitions.select("tr[bgcolor^=#FFFFFF]"));
		
		
		for(Element matchRow : matchRows)
		{
			try
			{
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				Elements matchFields = matchRow.select("td");
				
				String hour = matchFields.get(0).select("noscript").text().split(",")[1].trim();
				String homeTeam = matchFields.get(1).text().split("-")[0].trim();
				String awayTeam = matchFields.get(1).text().split("-")[1].trim();
				String pred1 = matchFields.get(3).text().replace("%", "").replace("1: ", "");
				String predX = matchFields.get(4).text().replace("%", "").replace("X: ", "");
				String pred2 = matchFields.get(5).text().replace("%", "").replace("2: ", "");

				row.setDate(DateUtil.addHourToDateandAddHours(date, hour, 1));
				row.setPred1(pred1);
				row.setPredX(predX);
				row.setPred2(pred2);
				row.setHomeTeam(homeTeam);
				row.setAwayTeam(awayTeam);		
				ret.getRows().add(row);
			}catch(StringIndexOutOfBoundsException ex)
			{
				log.warn("StringIndexOutOfBoundsException during read match row: " + ex.getMessage());
			}catch(ArrayIndexOutOfBoundsException ex)
			{
				log.warn("ArrayIndexOutOfBoundsException during read match row: " + ex.getMessage());
			}
		}
		return ret;
	}
	
	
	
	
	
}
