package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.ForecastValue;

public interface ForecastValueRepository extends CrudRepository<ForecastValue, Integer>{

	List<ForecastValue> findByValue(double value);

	List<ForecastValue> findAllByOrderByValueAsc();


}
