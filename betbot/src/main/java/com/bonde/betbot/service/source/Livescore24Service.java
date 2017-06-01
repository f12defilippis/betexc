package com.bonde.betbot.service.source;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;

@Service
public class Livescore24Service extends CrawlerService{

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  

	public ForecastScan crawlResult(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.LIVESCORE24));
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("http://www.livescore24.it/Calcio/risultati/" + strDate+ "");
//		log.info(document.toString());		
		Elements competitions = document.select("table[class=table table-primary table-livescore]");
//		Elements competitions = document.select("div[class=box-tournament]");

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
	
	
	public ForecastScan crawlResultHtmlUnit(Date date) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		ForecastScan ret = new ForecastScan();

		
		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource(String.valueOf(Source.LIVESCORE24));
		ret.setSport("1");
		ret.setSeason("1");

		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52)) {
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setDownloadImages(false);
			webClient.getOptions().setGeolocationEnabled(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			final HtmlPage page = webClient.getPage("http://www.livescore24.it/Calcio/risultati/" + strDate);
	        
	        List<DomNode> competitions = page.querySelectorAll(".table-livescore");
	        for(DomNode competition : competitions)
	        {
	        	List<HtmlTableBody> bodies = ((HtmlTable)competition).getBodies();

	        	HtmlTableBody body = bodies.get(0);
	        	
	        	List<HtmlTableRow> rows = body.getRows();
	        	for(HtmlTableRow tr : rows)
	        	{
		        	try{
						ForecastMatchRowTO row = new ForecastMatchRowTO();
		        		
		        		List<HtmlTableCell> cells = tr.getCells();
		        		String hour = cells.get(1).getTextContent().trim();
		        		String homeTeam = cells.get(3).getElementsByTagName("a").get(0).getTextContent().trim();
		        		String awayTeam = "";
		        		if(cells.get(5).getElementsByTagName("a").size()>1)
		        		{
		        			awayTeam=cells.get(5).getElementsByTagName("a").get(1).getTextContent().trim();
		        		}else
		        		{
		        			awayTeam=cells.get(5).getElementsByTagName("a").get(0).getTextContent().trim();
		        		}
		        		
		        		DomNode node = tr.querySelector(".m_score");
		        		HtmlElement div = tr.getElementsByAttribute("div", "class", "m_score").get(0);
		        		
		        		
		        		String finalScoreHomeTeam = div.getElementsByTagName("span").get(0).getTextContent().trim(); 
	
		        		
						String finalScoreAwayTeam = div.getElementsByTagName("span").get(1).getTextContent().trim(); 
						
						String finalScore = finalScoreHomeTeam + " - " + finalScoreAwayTeam;
						String halftimeScore = cells.get(6).getTextContent().replace("(", "").replace(")", "").replace("-", ":");
	
//						row.setDate(DateUtil.dateManagement(date, hour));
						row.setDate(date);
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
	        
	    }
		return ret;
	}	


	
	
}
