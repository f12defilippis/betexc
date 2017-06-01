package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.ForecastSummary;
import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.ValueGroup;

public interface ForecastSummaryRepository extends CrudRepository<ForecastSummary, Integer>{

	List<ForecastSummary> findBySourceAndForecastValueAndValueBetAndForecastTypeOccurrenceAndFinalDateBetween(Source source, ForecastValue forecastValue, ForecastValue valueBet, ForecastTypeOccurrence forecastTypeOccurrence, Date initialDate, Date finalDate);
	
	List<ForecastSummary> findBySourceAndForecastValueAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween(Source source, ForecastValue forecastValue, ValueGroup valueBetGroup, ForecastTypeOccurrence forecastTypeOccurrence, Date initialDate, Date finalDate);

	List<ForecastSummary> findBySourceAndValueGroupAndValueBetGroupAndForecastTypeOccurrenceAndFinalDateBetween(Source source, ValueGroup forecastValueGroup, ValueGroup valueBetGroup, ForecastTypeOccurrence forecastTypeOccurrence, Date initialDate, Date finalDate);

}
