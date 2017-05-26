package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.FattestMatch;

public interface FattestMatchRepository extends CrudRepository<FattestMatch, Integer>{

	List<FattestMatch> findByDateStartBetween(Date dateStart, Date dateEnd);



}
