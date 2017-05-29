package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Result;

public interface ResultRepository extends CrudRepository<Result, Integer>{

	List<Result> findByMatch(Match match);

}
