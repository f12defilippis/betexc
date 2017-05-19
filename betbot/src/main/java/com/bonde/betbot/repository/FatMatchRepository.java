package com.bonde.betbot.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.FatMatch;

public interface FatMatchRepository extends CrudRepository<FatMatch, Integer>{

	List<FatMatch> findByDateStartBetween(Date dateStart, Date dateEnd);



}
