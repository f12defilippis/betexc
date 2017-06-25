package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.ForecastFinal;

public interface ForecastFinalRepository extends CrudRepository<ForecastFinal, Integer>{

	List<ForecastFinal> findByMatchDateStartBetween(Date dateStart, Date dateEnd);

	List<ForecastFinal> findByForecastSummaryParameterAndMatchDateStartBetweend(
			String forecastSummaryParameter, Date date, Date addDaysToDate);

}
