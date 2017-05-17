package com.bonde.betbot.service.source;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.dto.OddMatchRowTO;
import com.bonde.betbot.service.CrawlerService;

@Service
public class BettingTips1X2Service extends CrawlerService{

	
	public List<OddMatchRowTO> crawlOdds(Date date) throws Exception
	{


		List<OddMatchRowTO> ret = new ArrayList<OddMatchRowTO>();
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		
		Document document = executeCall("http://bettingtips1x2.com/tips/" + strDate+ ".html");
				
		Elements matches = document.select("table[class^=results]").get(0).select("tr");

		for(int i = 1 ; i < matches.size() ; i++)
		{
			Element matchRow = matches.get(i);
			Elements matchFields = matchRow.select("td");
			
			Date matchDate = calculateDate(date, matchFields.get(2).text());
			String homeTeam = matchFields.get(3).text().split(" v ")[0].trim();
			String awayTeam = matchFields.get(3).text().split(" v ")[1].trim();
			double odd1 = Double.parseDouble(matchFields.get(4).text());
			double oddX = Double.parseDouble(matchFields.get(5).text());
			double odd2 = Double.parseDouble(matchFields.get(6).text());

			OddMatchRowTO row = new OddMatchRowTO();
			row.setAwayTeam(awayTeam);
			row.setHomeTeam(homeTeam);
			row.setMatchDate(matchDate);
			row.setOdd1(odd1);
			row.setOdd2(odd2);
			row.setOddX(oddX);
			
			ret.add(row);
		}
		
		return ret;
		
	}

	
	public Date dateManagement(Date date, String hour)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = format.format(date);
		
		String datehour = strDate + " " + hour;
		
		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date ret;
		try {
			ret = formatDateHour.parse(datehour);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}		
	
	private Date calculateDate(Date date, String hour)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = format.format(date);
		
		String datehour = strDate + " " + hour;
		
		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date ret;
		Calendar cal = Calendar.getInstance(); // creates calendar
		try {
			ret = formatDateHour.parse(datehour);
		    cal.setTime(ret); // sets calendar time/date
		    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return cal.getTime(); // returns new date object, one hour in the future		

	}
	
	
	
	
	
}
