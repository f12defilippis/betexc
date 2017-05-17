package com.bonde.betbot.service.source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.TeamName;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.repository.TeamNameRepository;
import com.bonde.betbot.repository.TeamRepository;
import com.bonde.betbot.service.CrawlerService;

public class BetstudyService extends CrawlerService{

	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	TeamNameRepository teamNameRepository;
	
	public ForecastScan crawlResult(Date date) throws Exception
	{
		ForecastScan ret = new ForecastScan();

		ret.setDatetime(new Date());
		ret.setSource("1");
		ret.setSport("1");
		ret.setSeason("1");

		Document document = executeCall("https://www.statarea.com/predictions/date/2017-05-12/competition");

		Elements rows = document.select("table[class^=soccer-table]").get(0).select("tr");
		for(int i = 1 ; i < rows.size() ; i++)
		{
			ForecastMatchRowTO match = new ForecastMatchRowTO();
			Element row = rows.get(i);
			Elements cells = row.select("td");
			
			//TODO format date
//			String strDate = cells.get(0).select("span").get(0).text();
//			match.setDate(strDate);
			
			String teams = cells.get(1).select("a").get(1).text();
			match.setHomeTeam(teams.split("-")[0].trim());
			match.setAwayTeam(teams.split("-")[1].trim());
			
			String pred1 = cells.get(2).text().replace("%", "");
			String predX = cells.get(3).text().replace("%", "");
			String pred2 = cells.get(4).text().replace("%", "");
			String over25 = cells.get(5).text().replace("%", "");
//			String under25 = cells.get(6).text().replace("%", "");

			match.setPred1(pred1);
			match.setPredX(predX);
			match.setPred2(pred2);
			
			match.setPredOver25(over25);
			
			ret.getRows().add(match);
		}
		
		return ret;		
	}

	
	public void saveTeams(String url) throws Exception
	{
		ForecastScan ret = new ForecastScan();
		List<String> teamsNotSaved = new ArrayList<String>();

		ret.setDatetime(new Date());
		ret.setSource("1");
		ret.setSport("1");
		ret.setSeason("1");

		Document document = executeCall(url);
		
		Elements rows = document.select("table[class^=soccer-table]").get(0).select("tr");
		for(int i = 1 ; i < rows.size() ; i++)
		{
			Element row = rows.get(i);
			Elements cells = row.select("td");
						
			String teams = cells.get(1).select("a").get(1).text();
			if(!saveTeam(teams.split("-")[0].trim()))
			{
				teamsNotSaved.add(teams.split("-")[0].trim());
			}
			if(!saveTeam(teams.split("-")[1].trim()))
			{
				teamsNotSaved.add(teams.split("-")[1].trim());
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
		List<TeamName> saved = teamNameRepository.findByNameAndSource(team, new Source(Source.BETSTUDY));
		if(saved==null || saved.size()==0)
		{
			saved = teamNameRepository.findByName(team);
			if(saved!=null && saved.size()>0)
			{
				TeamName other = saved.get(0);
				TeamName newTeamName = new TeamName();
				newTeamName.setName(team);
				newTeamName.setTeam(other.getTeam());
				newTeamName.setSource(new Source(Source.BETSTUDY));
				
				teamNameRepository.save(newTeamName);
			}else
			{
				return false;
			}
		}
		return true;
		
		
		
	}
	
	
	
}
