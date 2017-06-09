package com.bonde.betbot.service.datacollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bonde.betbot.model.domain.CompetitionExclusion;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.Team;
import com.bonde.betbot.model.domain.TeamName;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.repository.CompetitionExclusionRepository;
import com.bonde.betbot.repository.CompetitionRepository;
import com.bonde.betbot.repository.ForecastRepository;
import com.bonde.betbot.repository.ForecastValueRepository;
import com.bonde.betbot.repository.MatchRepository;
import com.bonde.betbot.repository.SeasonRepository;
import com.bonde.betbot.repository.SourceRepository;
import com.bonde.betbot.repository.SportRepository;
import com.bonde.betbot.repository.TeamNameRepository;
import com.bonde.betbot.repository.TeamRepository;
import com.bonde.betbot.service.source.Livescore24Service;
import com.bonde.betbot.service.source.StatareaService;
import com.bonde.betbot.util.DateUtil;

public abstract class ForecastResultService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());  
	
	
	@Autowired
	StatareaService statareaService;
	
	@Autowired
	SportRepository sportRepository;
	
	@Autowired
	SeasonRepository seasonRepository;

	@Autowired
	CompetitionRepository competitionRepository;
	
	@Autowired
	TeamNameRepository teamNameRepository;
	
	@Autowired
	SourceRepository sourceRepository;
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	MatchRepository matchRepository;
	
	@Autowired
	ForecastValueRepository forecastValueRepository;

	@Autowired
	ForecastRepository forecastRepository;	

	@Autowired
	CompetitionExclusionRepository competitionExclusionRepository;
	
	@Autowired
	SummaryService summaryService;
	
	@Autowired
	Livescore24Service livescore24Service;	
	
	public Team teamManagement(String t, Source source)
	{
		Team team = new Team();
		try
		{
			//TEAM
			List<TeamName> listtn = teamNameRepository.findByNameAndSource(t, source);
			TeamName teamName = new TeamName();
			if(listtn!=null && listtn.size()>0)
			{
				teamName = listtn.get(0);
				team = teamName.getTeam();
			}else
			{
				
				List<TeamName> listtname = teamNameRepository.findByName(t);
				
				
				if(listtname != null && listtname.size()>0)
				{
					teamName = listtname.get(0);
					team = teamName.getTeam();
					
					
					TeamName newTeamName = new TeamName();
					newTeamName.setName(t);
					newTeamName.setSource(source);
					newTeamName.setTeam(team);
					newTeamName.setDateCreated(new Date());
					newTeamName.setDateUpdated(new Date());
					
					teamNameRepository.save(newTeamName);

					log.debug("NEW TEAM SAVED: " + t + " - ORIGINAL NAME: " + teamName.getName());
				}
				else
				{
					return null;
				}
				
			}
			
		}catch(Exception e)
		{
			return null;
		}
		return team;
	}	
	
	public Match saveMatchData(ForecastMatchRowTO row, ForecastScan scan, Date date, Source source)
	{
		
		if(row.getCompetition()!=null && !row.getCompetition().equals(""))
		{
			List<CompetitionExclusion> excluded = competitionExclusionRepository.findByDescription(row.getCompetition());
			if(excluded!=null && excluded.size()>0)
			{
				log.debug("COMPETITION SKIPPED : " + row.getCompetition());
				return null;
			}
		}


		//TEAM
		Team homeTeam = teamManagement(row.getHomeTeam(), source);
		Team awayTeam = teamManagement(row.getAwayTeam(), source);
		
		if(homeTeam == null || awayTeam == null)
		{
			log.debug("MATCH SKIPPED TNF: " + row.getHomeTeam() + " - " + row.getAwayTeam());
			return null;
		}
		
		
		//MATCH
		List<Match> matchList = new ArrayList<Match>();
		matchList = matchRepository.findByHomeTeamAndAwayTeamAndDateStartBetween(homeTeam,awayTeam,row.getDate(),DateUtil.addDaysToDate(row.getDate(), 2));
		
		
		
		Match match = new Match();
		if(matchList!=null && matchList.size()>0)
		{
			match = matchList.get(0);
			if(row.getResult()!=null)
				match.setFinalScore(row.getResult());
			if(row.getResultHT()!=null)
				match.setHalftimeScore(row.getResultHT());
			match.setDateUpdated(new Date());
			matchRepository.save(match);
			log.debug("MATCH UPDATED: " + row.getHomeTeam() + " - " + row.getAwayTeam());
		}else
		{
			log.debug("MATCH SKIPPED MNF: " + row.getHomeTeam() + " - " + row.getAwayTeam());
			return null;
		}
		return match;
	}
	
	
}
