package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ForecastType implements Serializable{

	private static final long serialVersionUID = -3450975300683550829L;

	public static final int PRED1X2 = 1;
	public static final int PRED1X2HT = 2;
	public static final int UO15 = 3;
	public static final int UO25 = 4;
	public static final int UO35 = 5;
	public static final int DC = 6;
	public static final int DCHT = 7;
	
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
