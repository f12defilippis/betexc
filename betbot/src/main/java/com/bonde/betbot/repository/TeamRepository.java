package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Sport;
import com.bonde.betbot.model.domain.Team;

public interface TeamRepository extends CrudRepository<Team, Integer>{

	List<Team> findByNameAndSport(String name, Sport sport);


}
