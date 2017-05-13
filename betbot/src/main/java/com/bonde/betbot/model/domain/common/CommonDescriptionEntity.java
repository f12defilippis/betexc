package com.bonde.betbot.model.domain.common;

import java.io.Serializable;

import javax.persistence.Id;

public abstract class CommonDescriptionEntity implements Serializable{

	private static final long serialVersionUID = -2882394170939532160L;

	@Id
	private int id;
	private String description;
	
	
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
	
	
	
	
}
