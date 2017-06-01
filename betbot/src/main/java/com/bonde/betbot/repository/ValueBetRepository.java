package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Forecast;
import com.bonde.betbot.model.domain.Odd;
import com.bonde.betbot.model.domain.ValueBet;

public interface ValueBetRepository extends CrudRepository<ValueBet, Integer>{

	List<ValueBet> findByOddAndForecast(Odd odd, Forecast forecast);

	List<ValueBet> findByForecastMatchDateStartBetween(Date dateStart, Date dateEnd);

	
	
	

}
