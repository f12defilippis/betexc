package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Forecast;
import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Source;

public interface ForecastRepository extends CrudRepository<Forecast, Integer>{

	List<Forecast> findByMatchAndSourceAndForecastTypeOccurrence(Match match, Source source, ForecastTypeOccurrence forecastTypeOccurrence);

	List<Forecast> findByMatchDateStartBetween(Date dateStart, Date dateEnd);


}
