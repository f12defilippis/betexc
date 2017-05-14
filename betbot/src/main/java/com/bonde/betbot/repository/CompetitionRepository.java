package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Competition;
import com.bonde.betbot.model.domain.Season;
import com.bonde.betbot.model.domain.Sport;

public interface CompetitionRepository extends CrudRepository<Competition, Integer>{

	List<Competition> findByDescriptionAndSportAndSeason(String description, Sport sport, Season season);

}
