package com.bonde.betbot.service.source;

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
import com.bonde.betbot.service.CrawlerService;

@Service
public class VitibetService extends CrawlerService{

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlForecast(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

//		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//		String strDate = format.format(date);
		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.VITIBET));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.vitibet.com/index.php?clanek=quicktips&sekce=fotbal&lang=en");
		
		Elements competitions = document.select("table[class^=tabulkaquick]");

		Elements matchRows = competitions.select("tr");
		for(int i = 3 ; i < matchRows.size() ; i++)
		{
			Element matchRow = matchRows.get(i);
			try
			{
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				Elements matchFields = matchRow.select("td");
				if(matchFields.size()==12)
				{
//					String hour = matchFields.get(0).select("noscript").text().split(",")[1].trim();
					String homeTeam = matchFields.get(1).select("a").get(0).text().trim();
					String awayTeam = matchFields.get(2).select("a").get(0).text().trim();
					String pred1 = matchFields.get(6).text().replace(" %", "");
					String predX = matchFields.get(7).text().replace(" %", "");
					String pred2 = matchFields.get(8).text().replace(" %", "");

					row.setDate(date);
					row.setPred1(pred1);
					row.setPredX(predX);
					row.setPred2(pred2);
					row.setHomeTeam(homeTeam);
					row.setAwayTeam(awayTeam);		
					ret.getRows().add(row);
				}
			}catch(StringIndexOutOfBoundsException ex)
			{
				log.warn("NumberFormatException during read match row: " + ex.getMessage());
			}
		}
		return ret;
	}
	
	
	
	
	
}
