package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.ForecastValueGroup;

public interface ForecastValueGroupRepository extends CrudRepository<ForecastValueGroup, Integer>{

	List<ForecastValueGroup> findByForecastValue(ForecastValue forecastValue);

}
