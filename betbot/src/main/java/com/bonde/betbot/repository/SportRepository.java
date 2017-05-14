package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.Sport;

public interface SportRepository extends CrudRepository<Sport, Integer>{

	List<Sport> findByDescription(String description);

}
