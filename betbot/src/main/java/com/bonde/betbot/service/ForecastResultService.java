package com.bonde.betbot.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bonde.betbot.model.domain.Competition;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Season;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.Sport;
import com.bonde.betbot.model.domain.Team;
import com.bonde.betbot.model.domain.TeamName;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.repository.CompetitionRepository;
import com.bonde.betbot.repository.ForecastRepository;
import com.bonde.betbot.repository.ForecastValueRepository;
import com.bonde.betbot.repository.MatchRepository;
import com.bonde.betbot.repository.SeasonRepository;
import com.bonde.betbot.repository.SourceRepository;
import com.bonde.betbot.repository.SportRepository;
import com.bonde.betbot.repository.TeamNameRepository;
import com.bonde.betbot.repository.TeamRepository;
import com.bonde.betbot.service.source.StatareaService;

public abstract class ForecastResultService {

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
	
	public Team teamManagement(String t, Source source, Sport sport)
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
//				String name = t;
//				boolean found = false;
				
//				//se il nome della squadra è composta da due parole faccio una like sulla parola più lunga
//				String [] splitted = name.split(" ");
//				if(splitted!=null && splitted.length>1)
//				{
//					String longer = splitted[0].length() >= splitted[1].length() ? splitted[0] : splitted[1];
//					listtname = teamNameRepository.findByNameContaining(longer);
//					if(listtname != null && listtname.size()>0)
//					{
//						found = true;
//					}
//				}
//
//				//Ciclo togliendo una lettera alla volta
//				boolean head = true;
//				while(name.length()>=TEAMNAME_DISCOVER_THRESHOLD && !found)
//				{
//					listtname = teamNameRepository.findByNameContaining(name);
//					if(listtname != null && listtname.size()>0)
//					{
//						found = true;
//					}
//					if(head)
//					{
//						name = name.substring(1, name.length());
//						head = false;
//					}else
//					{
//						name = name.substring(0, name.length()-1);
//						head = true;
//					}
//				}
//				if(!found) return null;
			}
			
			if(listtname != null && listtname.size()>0)
			{
				teamName = listtname.get(0);
				team = teamName.getTeam();
				
				
				TeamName newTeamName = new TeamName();
				newTeamName.setName(t);
				newTeamName.setSource(source);
				newTeamName.setTeam(team);
				
				teamNameRepository.save(newTeamName);

				System.out.println("NEW TEAM SAVED: " + t + " - ORIGINAL NAME: " + teamName.getName());
			}
			else
			{
				return null;
			}
//			else
//			{
//				team.setName(t);
//				team.setSport(sport);
//				teamRepository.save(team);
//				
//				teamName.setName(t);
//				teamName.setTeam(team);
//				teamName.setSource(source);
//				teamNameRepository.save(teamName);
//			}
			
		}
		return team;
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
	
	public Match saveMatchData(ForecastMatchRowTO row, ForecastScan scan, Date date, Source source)
	{
		Sport sport = sportRepository.findOne(Integer.parseInt(scan.getSport()));
		Season season = seasonRepository.findOne(Integer.parseInt(scan.getSeason()));
		
		//COMPETITION
		List<Competition> complist = competitionRepository.findByDescriptionAndSportAndSeason(row.getCompetition(), sport, season);
		Competition competition = new Competition();
		if(complist!=null && complist.size()>0)
		{
			competition = complist.get(0);
		}
		else
		{
			competition.setDescription(row.getCompetition());
			competition.setSeason(season);
			competition.setSport(sport);
			
			competitionRepository.save(competition);
		}

		//TEAM
		Team homeTeam = teamManagement(row.getHomeTeam(), source, sport);
		Team awayTeam = teamManagement(row.getAwayTeam(), source, sport);
		
		if(homeTeam == null || awayTeam == null)
		{
			System.out.println("MATCH SKIPPED: " + row.getHomeTeam() + " - " + row.getAwayTeam());
			return null;
		}
		
		
		//MATCH
		List<Match> matchList = new ArrayList<Match>();
		if(competition!=null)
		{
			matchRepository.findByDateStartAndHomeTeamAndAwayTeamAndCompetition(dateManagement(date, row.getHour()),homeTeam,awayTeam,competition);
		}else
		{
			matchRepository.findByDateStartAndHomeTeamAndAwayTeam(dateManagement(date, row.getHour()),homeTeam,awayTeam);
		}

		Match match = new Match();
		if(matchList!=null && matchList.size()>0)
		{
			match = matchList.get(0);
			if(match.getCompetition()==null && competition!=null)
			{
				match.setCompetition(competition);
				matchRepository.save(match);
			}
		}else
		{
			match.setDateStart(dateManagement(date, row.getHour()));
			match.setHomeTeam(homeTeam);
			match.setAwayTeam(awayTeam);
			match.setCompetition(competition);

			matchRepository.save(match);
		}
		return match;
	}
	
	
}
