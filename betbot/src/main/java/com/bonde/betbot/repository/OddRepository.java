package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Odd;

public interface OddRepository extends CrudRepository<Odd, Integer>{


	List<Odd> findByMatchAndForecastTypeOccurrence(Match match, ForecastTypeOccurrence forecastTypeOccurrence);

	
	List<Odd> findByMatchlazyDateStartBetween(Date dateStart,Date dateEnd);
	
	
}
