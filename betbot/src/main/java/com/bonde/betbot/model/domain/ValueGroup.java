package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ValueGroup implements Serializable{

	private static final long serialVersionUID = 2051575735003311361L;

	@ManyToOne
    @JoinColumn(name = "value_group_type", referencedColumnName = "id", nullable = false)
	private ValueGroupType valueGroupType;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int id;
	private String description;
	
	private Double minimumValue;
	private Double maximumValue;

	public ValueGroup(){}
	
	public ValueGroup(Integer pid){
		id = pid;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	public ValueGroupType getValueGroupType() {
		return valueGroupType;
	}

	public void setValueGroupType(ValueGroupType valueGroupType) {
		this.valueGroupType = valueGroupType;
	}

	public Double getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(Double minimumValue) {
		this.minimumValue = minimumValue;
	}

	public Double getMaximumValue() {
		return maximumValue;
	}

	public void setMaximumValue(Double maximumValue) {
		this.maximumValue = maximumValue;
	}



	

	
	

}
