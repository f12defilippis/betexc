package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Competition;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Team;

public interface MatchRepository extends CrudRepository<Match, Integer>{

	List<Match> findByDateStartAndHomeTeamAndAwayTeamAndCompetition(Date dateStart, Team homeTeam, Team awayTeam, Competition competition);


}
