package com.bonde.betbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bonde.betbot.model.domain.ValueGroup;

public interface ValueGroupRepository extends CrudRepository<ValueGroup, Integer>{

	@Query("from ValueGroup vg where vg.minimumValue <= :value and maximumValue >= :value")
	List<ValueGroup> findValueGroupByValueInRange(@Param("value") double value);	

	

}
