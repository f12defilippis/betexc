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
public class PickForWinService  extends CrawlerService{

	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlForecast(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		String strDate = format.format(date);
		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.PICKFORWIN));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.pickforwin.com/it/pronostici-sportivi-scientifici.html?sport=calcio&data_pronostici=" + strDate);
		

		Elements matchRows = document.select("tr[class^=rigadispari uk-hidden-small]");
		matchRows.addAll(document.select("tr[class^=rigapari uk-hidden-small]"));
		for(Element matchRow : matchRows)
		{
			try
			{
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				Elements matchFields = matchRow.select("td");
				
//				String hour = matchFields.get(0).select("noscript").text().split(",")[1].trim();
				String homeTeam = matchFields.get(1).text().split(" - ")[0].trim();
				String awayTeam = matchFields.get(1).text().split(" - ")[1].trim();
				String pred1 = matchFields.get(2).text().replace("%", "");
				String predX = matchFields.get(3).text().replace("%", "");
				String pred2 = matchFields.get(4).text().replace("%", "");

//				String under = matchFields.get(5).text().replace("%", "");
				String over = matchFields.get(6).text().replace("%", "");
				String goal = matchFields.get(7).text().replace("%", "");
				String nogoal = matchFields.get(8).text().replace("%", "");
				
				
				
				row.setDate(date);
				row.setPred1(pred1);
				row.setPredX(predX);
				row.setPred2(pred2);
				row.setHomeTeam(homeTeam);
				row.setAwayTeam(awayTeam);
				
				row.setPredOver25(over);
				row.setPredOTS(nogoal);
				row.setPredBTS(goal);
				
				ret.getRows().add(row);
				
				log.debug("Row Added: " + row.toString());
				
			}catch(StringIndexOutOfBoundsException ex)
			{
				log.warn("NumberFormatException during read match row: " + ex.getMessage());
			}catch(IndexOutOfBoundsException ex)
			{
				log.warn("IndexOutOfBoundsException during read match row: " + ex.getMessage());
			}
		}
		return ret;
	}
	
	
	
}
