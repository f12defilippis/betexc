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
import com.bonde.betbot.service.CrawlerService;
import com.bonde.betbot.util.DateUtil;

@Service
public class Livescore24Service extends CrawlerService{

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlResult(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		
		
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.LIVESCORE));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.livescore24.it/Calcio/risultati/" + strDate+ "");
				
		Elements competitions = document.select("table[class^=table table-primary table-livescore]");

		for(Element competitionMatches : competitions)
		{
			Elements matchRows = competitionMatches.select("tr");
			for(Element matchRow : matchRows)
			{
				try{
					ForecastMatchRowTO row = new ForecastMatchRowTO();
					Elements matchFields = matchRow.select("td");
					
					String hour = matchFields.get(1).text().trim();
					String homeTeam = matchFields.get(3).select("a").get(0).text().trim();
					String awayTeam = matchFields.get(5).select("a").not(".card").get(0).text().trim();
					
					String finalScoreHomeTeam = matchRow.select("div[class^=m_score]").select("span").get(0).text(); 
					String finalScoreAwayTeam = matchRow.select("div[class^=m_score]").select("span").get(1).text();
					
					String finalScore = finalScoreHomeTeam + " - " + finalScoreAwayTeam;
					String halftimeScore = matchFields.get(6).text().replace("(", "").replace(")", "").replace("-", ":");

					row.setDate(DateUtil.dateManagement(date, hour));
					row.setHomeTeam(homeTeam);
					row.setAwayTeam(awayTeam);
									
					row.setResult(finalScore);
					row.setResultHT(halftimeScore);
					
					ret.getRows().add(row);				
				}catch(IndexOutOfBoundsException ioex)
				{
					log.warn("IndexOutOfBoundsException during read match row: " + ioex.getMessage());
				}catch(NumberFormatException ioex)
				{
					log.warn("NumberFormatException during read match row: " + ioex.getMessage());
				}
			}
		}
		
		
		
		return ret;
	}
	
	
}
