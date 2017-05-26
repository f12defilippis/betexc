package com.bonde.betbot.service.source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.TeamName;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.repository.TeamNameRepository;
import com.bonde.betbot.repository.TeamRepository;
import com.bonde.betbot.service.datacollection.CrawlerService;
import com.bonde.betbot.util.DateUtil;

@Service
public class StatareaService extends CrawlerService{
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	TeamNameRepository teamNameRepository;


	public ForecastScan crawlResult(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		ret.setCalculatehour(false);
		ret.setDatetime(new Date());
		ret.setSource("1");
		ret.setSport("1");
		ret.setSeason("1");
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);

		Document document = executeCall("https://www.statarea.com/predictions/date/" + strDate+ "/competition");

		Elements competitions = document.select("div[class^=competition]");
		for(int j = 0 ; j < competitions.size() ; j++)
		{
			Element competition = competitions.get(j);
			Elements competitionNameElements = competition.select("div[class^=header]");
			Element competitionNameElement = competitionNameElements.get(0);

			String competitionName = competitionNameElement.select("div[class=name]").get(0).text();
			String countryName = (competitionName.split("-")[0]).trim();
			
			if(competitionName.equals("Advertisement"))
			{
				continue;
			}
			Element competitionMatchesElement = competition.select("div[class=body]").get(0);

			Elements matchElements = competitionMatchesElement.select("div[class=match]");
			
			for(int i = 0 ; i < matchElements.size() ; i++)
			{
				Element matchElement = matchElements.get(i);

				String hour = matchElement.select("div[class^=date]").get(0).text();
				String hostTeam = matchElement.select("div[class^=hostteam]").get(0).select("div[class^=name]").get(0).select("a[href]").get(0).text();
				String guestTeam = matchElement.select("div[class^=guestteam]").get(0).select("div[class^=name]").get(0).select("a[href]").get(0).text();

				String goalHost = matchElement.select("div[class^=hostteam]").get(0).select("a[class=goals]").get(0).text();
				String goalGuest = matchElement.select("div[class^=guestteam]").get(0).select("a[class=goals]").get(0).text();

				String htres = matchElement.select("div[class^=guestteam]").get(0).select("div[class^=htres]").get(0).text();
				
				
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				
				row.setDate(DateUtil.addHourToDateandAddHours(date, hour,2));
				row.setHomeTeam(hostTeam);
				row.setAwayTeam(guestTeam);
				
				row.setCompetition(competitionName);
				row.setCountry(countryName);
				
				row.setResult(goalHost + "-" + goalGuest);
				row.setResultHT(htres.replaceAll("HT", "").trim());
				
				
				ret.getRows().add(row);
				System.out.println(row.getHomeTeam() + " - " + row.getAwayTeam() + " Ris: " + row.getResult() + " HT: " + row.getResultHT());
			}
			
		}
		
		return ret;
	}
	
	
	public ForecastScan crawlForecast(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		
		
		ret.setDatetime(new Date());
		ret.setSource("1");
		ret.setSport("1");
		ret.setSeason("1");
		Document document = executeCall("https://www.statarea.com/predictions/date/" + strDate+ "/competition");

		Elements competitions = document.select("div[class^=competition]");
		for(int j = 0 ; j < competitions.size() ; j++)
		{
			Element competition = competitions.get(j);
			Elements competitionNameElements = competition.select("div[class^=header]");
			Element competitionNameElement = competitionNameElements.get(0);

			String competitionName = competitionNameElement.select("div[class=name]").get(0).text();
			String countryName = (competitionName.split("-")[0]).trim();
			
			if(competitionName.equals("Advertisement"))
			{
				continue;
			}
			Element competitionMatchesElement = competition.select("div[class=body]").get(0);

			Elements matchElements = competitionMatchesElement.select("div[class=match]");
			
			for(int i = 0 ; i < matchElements.size() ; i++)
			{
				Element matchElement = matchElements.get(i);

				String hour = matchElement.select("div[class^=date]").get(0).text();
				String hostTeam = matchElement.select("div[class^=hostteam]").get(0).select("div[class^=name]").get(0).select("a[href]").get(0).text();
				String guestTeam = matchElement.select("div[class^=guestteam]").get(0).select("div[class^=name]").get(0).select("a[href]").get(0).text();

				String pred1 = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(11).select("div[class^=value]").get(0).text();
				String predX = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(12).select("div[class^=value]").get(0).text();
				String pred2 = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(13).select("div[class^=value]").get(0).text();
				
				String pred1ht = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(14).select("div[class^=value]").get(0).text();
				String predXht = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(15).select("div[class^=value]").get(0).text();
				String pred2ht = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(16).select("div[class^=value]").get(0).text();
				
				String over15 = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(17).select("div[class^=value]").get(0).text();
				String over25 = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(18).select("div[class^=value]").get(0).text();
				String over35 = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(19).select("div[class^=value]").get(0).text();
				
				String goal = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(20).select("div[class^=value]").get(0).text();
				String nogoal = matchElement.select("div[class^=inforow]").get(0).select("div[class^=coefbox]").get(21).select("div[class^=value]").get(0).text();

				
				ForecastMatchRowTO row = new ForecastMatchRowTO();
				row.setPred1(pred1);
				row.setPredX(predX);
				row.setPred2(pred2);
				
				row.setPred1ht(pred1ht);
				row.setPredXht(predXht);
				row.setPred2ht(pred2ht);
				
				row.setPredOver15(over15);
				row.setPredOver25(over25);
				row.setPredOver35(over35);
				
				row.setPredBTS(goal);
				row.setPredOTS(nogoal);
				
				row.setDate(DateUtil.addHourToDateandAddHours(date, hour,2));
				row.setHomeTeam(hostTeam);
				row.setAwayTeam(guestTeam);
				
//				row.setCompetition(competitionName);
				row.setCountry(countryName);
				
				ret.getRows().add(row);
			}
			
		}
		
		return ret;
	}
	
	@Transactional
	public void saveTeams(Date date) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = format.format(date);
		List<String> teamsNotSaved = new ArrayList<String>();

		Document document = executeCall("https://www.statarea.com/predictions/date/" + strDate+ "/competition");

		Elements competitions = document.select("div[class^=competition]");
		for(int j = 0 ; j < competitions.size() ; j++)
		{
			Element competition = competitions.get(j);
			Elements competitionNameElements = competition.select("div[class^=header]");
			Element competitionNameElement = competitionNameElements.get(0);

			String competitionName = competitionNameElement.select("div[class=name]").get(0).text();
			
			if(competitionName.equals("Advertisement"))
			{
				continue;
			}
			Element competitionMatchesElement = competition.select("div[class=body]").get(0);

			Elements matchElements = competitionMatchesElement.select("div[class=match]");
			
			for(int i = 0 ; i < matchElements.size() ; i++)
			{
				Element matchElement = matchElements.get(i);

				String hostTeam = matchElement.select("div[class^=hostteam]").get(0).select("div[class^=name]").get(0).select("a[href]").get(0).text();
				String guestTeam = matchElement.select("div[class^=guestteam]").get(0).select("div[class^=name]").get(0).select("a[href]").get(0).text();
				if(!saveTeam(hostTeam))
				{
					teamsNotSaved.add(hostTeam);
				}
				if(!saveTeam(guestTeam))
				{
					teamsNotSaved.add(guestTeam);
				}
			}
		}
		
		System.out.println("TEAMS NOT SAVED:");
		for(String s : teamsNotSaved)
		{
			System.out.println(s);
		}
		
	}
	
	public boolean saveTeam(String team)
	{
		List<TeamName> saved = teamNameRepository.findByNameAndSource(team, new Source(Source.STATAREA));
		if(saved==null || saved.size()==0)
		{
			saved = teamNameRepository.findByName(team);
			if(saved!=null && saved.size()>0)
			{
				TeamName other = saved.get(0);
				TeamName newTeamName = new TeamName();
				newTeamName.setName(team);
				newTeamName.setTeam(other.getTeam());
				newTeamName.setSource(new Source(Source.STATAREA));
				
				teamNameRepository.save(newTeamName);
			}else
			{
				return false;
			}
		}
		return true;
		
	}	
	
	
}
