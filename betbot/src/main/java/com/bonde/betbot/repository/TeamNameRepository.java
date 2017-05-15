package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.domain.TeamName;

public interface TeamNameRepository extends CrudRepository<TeamName, Integer>{

	List<TeamName> findByNameAndSource(String name, Source source);

	List<TeamName> findByName(String name);

}
