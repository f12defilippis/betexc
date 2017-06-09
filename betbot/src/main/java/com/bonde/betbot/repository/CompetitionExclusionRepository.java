package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bonde.betbot.model.domain.CompetitionExclusion;

public interface CompetitionExclusionRepository extends CrudRepository<CompetitionExclusion, Integer>{

	List<CompetitionExclusion> findByDescription(String description);

}
