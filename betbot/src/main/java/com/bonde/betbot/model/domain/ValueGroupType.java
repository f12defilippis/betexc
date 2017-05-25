package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ValueGroupType implements Serializable{

	private static final long serialVersionUID = 2051575735003311361L;

	public static Integer T10 = 1;
	public static Integer T5 = 2;
	public static Integer T2 = 3;
	
	
	@Id
	private int id;
	private String description;
	
	
	public ValueGroupType()
	{
		
	}

	public ValueGroupType(int pid)
	{
		id=pid;
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
	
	
	
}
