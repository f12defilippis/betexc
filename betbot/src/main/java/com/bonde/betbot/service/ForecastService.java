package com.bonde.betbot.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.Competition;
import com.bonde.betbot.model.domain.Forecast;
import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.ForecastValue;
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

@Service
public class ForecastService {

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

	@Transactional
	public String getStatareaForecast(Date date)
	{
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(date);
			
			System.out.println("Starting crawling matches from statarea for date " + strDate);
			ForecastScan scan = statareaService.crawlForecast(date);
			System.out.println("Finished crawling matches from statarea. " + scan.getRows().size() + " matches found");
			System.out.println("Starting saving forecast");
			saveData(scan, date);
			System.out.println("Finished saving forecast");
		} catch (Exception e) {
			e.printStackTrace();
			return "KO";
		}
		
		return "OK";
	}

	public void saveData(ForecastScan scan, Date date)
	{
		for(ForecastMatchRowTO row : scan.getRows())
		{
			Sport sport = sportRepository.findOne(Integer.parseInt(scan.getSport()));
			Season season = seasonRepository.findOne(Integer.parseInt(scan.getSeason()));
			
			//COMPETITION
			List<Competition> complist = competitionRepository.findByDescriptionAndSportAndSeason(row.getCompetition(), sport, season);
			Competition competition = new Competition();
			if(complist!=null && complist.size()>0)
			{
				competition = complist.get(0);
			}else
			{
				competition.setDescription(row.getCompetition());
				competition.setSeason(season);
				competition.setSport(sport);
				
				competitionRepository.save(competition);
			}

			//TEAM
			Source source = sourceRepository.findOne(Integer.parseInt(scan.getSource()));
			Team homeTeam = teamManagement(row.getHomeTeam(), source, sport);
			Team awayTeam = teamManagement(row.getAwayTeam(), source, sport);
			
			List<Match> matchList = matchRepository.findByDateStartAndHomeTeamAndAwayTeamAndCompetition(dateManagement(date, row.getHour()),homeTeam,awayTeam,competition);
			Match match = new Match();
			
			if(matchList!=null && matchList.size()>0)
			{
				match = matchList.get(0);
			}else
			{
				match.setDateStart(dateManagement(date, row.getHour()));
				match.setHomeTeam(homeTeam);
				match.setAwayTeam(awayTeam);
				match.setCompetition(competition);

				matchRepository.save(match);
			}
			
			forecastManagement(row.getPred1(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1));
			forecastManagement(row.getPredX(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDX));
			forecastManagement(row.getPred2(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED2));
			
			forecastManagement(row.getPred1ht(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1HT));
			forecastManagement(row.getPredXht(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDXHT));
			forecastManagement(row.getPred2ht(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED2HT));
			
			forecastManagement(row.getPredOver15(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.OVER15));
			forecastManagement(row.getPredOver25(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.OVER25));
			forecastManagement(row.getPredOver35(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.OVER35));

			if(row.getPredOver15() != null && row.getPredOver15().length()>0)
			{
				int under15 = 100 - Integer.parseInt(row.getPredOver15());
				forecastManagement(String.valueOf(under15), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.UNDER15));
			}

			if(row.getPredOver25() != null && row.getPredOver25().length()>0)
			{
				int under25 = 100 - Integer.parseInt(row.getPredOver25());
				forecastManagement(String.valueOf(under25), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.UNDER25));
			}

			if(row.getPredOver35() != null && row.getPredOver35().length()>0)
			{
				int under35 = 100 - Integer.parseInt(row.getPredOver35());
				forecastManagement(String.valueOf(under35), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.UNDER35));
			}

			int pred1X = 100 - Integer.parseInt(row.getPred2());
			forecastManagement(String.valueOf(pred1X), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1X));
			
			int predX2 = 100 - Integer.parseInt(row.getPred1());
			forecastManagement(String.valueOf(predX2), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDX2));
			
			int pred12 = 100 - Integer.parseInt(row.getPredX());
			forecastManagement(String.valueOf(pred12), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED12));
			
			if(row.getPred2ht() != null && row.getPred2ht().length()>0)
			{
				int pred1Xht = 100 - Integer.parseInt(row.getPred2ht());
				forecastManagement(String.valueOf(pred1Xht), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1XHT));
			}
			
			if(row.getPred1ht() != null && row.getPred1ht().length()>0)
			{
				int predX2ht = 100 - Integer.parseInt(row.getPred1ht());
				forecastManagement(String.valueOf(predX2ht), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDX2HT));
			}
			
			if(row.getPredXht() != null && row.getPredXht().length()>0)
			{
				int pred12ht = 100 - Integer.parseInt(row.getPredXht());
				forecastManagement(String.valueOf(pred12ht), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED12HT));
			}
			
			
		}
	}

	
	private void forecastManagement(String input, Match match, Source source, ForecastTypeOccurrence forecastTypeOccurrence)
	{
		String strForecast = input.length() == 1 ? "0" + input : input;
		strForecast = "0." + strForecast;
		
		double dblForecast = new Double(strForecast).doubleValue();

		ForecastValue forecastValue = new ForecastValue();
		
		List<ForecastValue> listFV = forecastValueRepository.findByValue(dblForecast);
		if(listFV!=null && listFV.size()>0)
		{
			forecastValue = listFV.get(0);
		}else
		{
			forecastValue.setValue(dblForecast);
			forecastValueRepository.save(forecastValue);
		}
				
		List<Forecast> listForecast = forecastRepository.findByMatchAndSourceAndForecastTypeOccurrence(match, source, forecastTypeOccurrence);

		Forecast forecast = new Forecast();
		if(listForecast!=null && listForecast.size()>0)
		{
			forecast = listForecast.get(0);
		}else
		{
			forecast.setForecastTypeOccurrence(forecastTypeOccurrence);
			forecast.setMatch(match);
			forecast.setSource(source);
		}
		forecast.setForecastValue(forecastValue);
		forecastRepository.save(forecast);
		
	}
	
	
	private Team teamManagement(String t, Source source, Sport sport)
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
			team.setName(t);
			team.setSport(sport);
			teamRepository.save(team);
			
			teamName.setName(t);
			teamName.setTeam(team);
			teamName.setSource(source);
			teamNameRepository.save(teamName);
		}
		return team;
	}
	
	private Date dateManagement(Date date, String hour)
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
	
	
	
}
