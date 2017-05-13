package com.bonde.betbot.model.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bonde.betbot.model.domain.common.CommonDescriptionEntity;

@Entity
public class ValueGroup extends CommonDescriptionEntity{

	private static final long serialVersionUID = 2051575735003311361L;

	@ManyToOne
    @JoinColumn(name = "value_group_type", referencedColumnName = "id", nullable = false)
	private ValueGroupType valueGroupType;

	public ValueGroupType getValueGroupType() {
		return valueGroupType;
	}

	public void setValueGroupType(ValueGroupType valueGroupType) {
		this.valueGroupType = valueGroupType;
	}
	

	
	

}
