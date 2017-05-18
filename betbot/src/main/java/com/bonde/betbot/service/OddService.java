package com.bonde.betbot.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Odd;
import com.bonde.betbot.model.domain.ScanType;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.Team;
import com.bonde.betbot.model.domain.TeamName;
import com.bonde.betbot.model.dto.OddMatchRowTO;
import com.bonde.betbot.repository.MatchRepository;
import com.bonde.betbot.repository.OddRepository;
import com.bonde.betbot.repository.SourceRepository;
import com.bonde.betbot.repository.TeamNameRepository;
import com.bonde.betbot.service.source.BettingTips1X2Service;

@Service
public class OddService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	BettingTips1X2Service bettingTips1X2Service;
	
	@Autowired
	TeamNameRepository teamNameRepository;
	
	@Autowired
	SourceRepository sourceRepository;
	
	@Autowired
	MatchRepository matchRepository;
	
	@Autowired
	OddRepository oddRepository;
	
	@Autowired
	SummaryService summaryService;	
	
	
	@Transactional
	public String getBettingTips1X2Odds(Date date)
	{
		try {
			Date start = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(date);
			
			log.info("Starting crawling matches odds from bettingtips1x2 for date " + strDate);
			List<OddMatchRowTO> matchList = bettingTips1X2Service.crawlOdds(date);
			log.info("Finished crawling matches from bettingtips1x2. " + matchList.size() + " matches found");
			log.info("Starting saving odds");
			int matchSkipped = saveData(matchList, date);
			log.info("Finished saving odds." + (matchList.size() - matchSkipped) + " Matches Saved. " + matchSkipped + " Matches Skipped");
			Date end = new Date();

			summaryService.saveSummary(new Source(Source.BETTING1X2), new ScanType(ScanType.ODD), start, end, matchList.size() - matchSkipped, matchList.size(),date);
		
		} catch (Exception e) {
			e.printStackTrace();
			return "KO";
		}
		
		return "OK";
	}
	
	
	private int saveData(List<OddMatchRowTO> matchList, Date date) {

		int matchSkipped = 0;
		for(OddMatchRowTO row : matchList)
		{
			Source source = sourceRepository.findOne(Source.BETTING1X2);
			
			Match match = saveMatchData(row, date, source);

			if(match!=null)
			{
				saveOdd(ForecastTypeOccurrence.PRED1, match, row.getOdd1());
				saveOdd(ForecastTypeOccurrence.PREDX, match, row.getOddX());
				saveOdd(ForecastTypeOccurrence.PRED2, match, row.getOdd2());
			}else{
				matchSkipped++;
			}

			
		}
		
		return matchSkipped;
	}

	public void saveOdd(int forecastTypeOccurrence, Match match, double value)
	{
		ForecastTypeOccurrence fto = new ForecastTypeOccurrence(forecastTypeOccurrence);
		
		Date now = new Date();
		
		List<Odd> odds = oddRepository.findByMatchAndForecastTypeOccurrence(match, fto);
		Odd odd = new Odd();
		if(odds!=null && odds.size()>0)
		{
			odd = odds.get(0);
		}else
		{
			odd.setForecastTypeOccurrence(fto);
			odd.setMatch(match);
			odd.setDateCreated(now);
		}
		
		odd.setFirstValue(value);
		odd.setDateUpdated(now);
		oddRepository.save(odd);
	}
	
	
		
	public Match saveMatchData(OddMatchRowTO row, Date date, Source source)
	{

		//TEAM
		Team homeTeam = teamManagement(row.getHomeTeam(), source);
		Team awayTeam = teamManagement(row.getAwayTeam(), source);
		
		if(homeTeam == null || awayTeam == null)
		{
			log.debug("MATCH SKIPPED: " + row.getHomeTeam() + " - " + row.getAwayTeam());
			return null;
		}
		
		
		//MATCH
		List<Match> matchList = matchRepository.findByDateStartAndHomeTeamAndAwayTeam(date,homeTeam,awayTeam);

		Date now = new Date();
		
		Match match = new Match();
		if(matchList!=null && matchList.size()>0)
		{
			match = matchList.get(0);
		}else
		{
			match.setDateStart(date);
			match.setHomeTeam(homeTeam);
			match.setAwayTeam(awayTeam);

			match.setDateCreated(now);
			match.setDateUpdated(now);
			
			
			matchRepository.save(match);
		}
		return match;
	}		

	
	
	
	
	
	
	
	public Team teamManagement(String t, Source source)
	{
		//TEAM
		List<TeamName> listtn = teamNameRepository.findByNameAndSource(t, source);
		Team team = new Team();
		TeamName teamName = new TeamName();
		if(listtn!=null && listtn.size()>0)
		{
			teamName = listtn.get(0);
			team = teamName.getTeam();
		}else
		{
			//TODO teamnamemanagement
			
			List<TeamName> listtname = teamNameRepository.findByName(t);
			
			if(listtname == null || listtname.size()==0)
			{
			}
			
			if(listtname != null && listtname.size()>0)
			{
				teamName = listtname.get(0);
				team = teamName.getTeam();
				
				
				Date now = new Date();
				
				TeamName newTeamName = new TeamName();
				newTeamName.setName(t);
				newTeamName.setSource(source);
				newTeamName.setTeam(team);
				newTeamName.setDateCreated(now);
				newTeamName.setDateUpdated(now);
				
				
				teamNameRepository.save(newTeamName);

				log.debug("NEW TEAM SAVED: " + t + " - ORIGINAL NAME: " + teamName.getName());
			}
			else
			{
				return null;
			}
			
		}
		return team;
	}	
	
	
	
}
