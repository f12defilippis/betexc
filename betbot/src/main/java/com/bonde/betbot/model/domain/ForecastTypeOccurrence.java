package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ForecastTypeOccurrence implements Serializable
{

	private static final long serialVersionUID = -3450975300683550829L;

	public static final int PRED1 = 1;
	public static final int PREDX = 2;
	public static final int PRED2 = 3;
	
	public static final int PRED1HT = 4;
	public static final int PREDXHT = 5;
	public static final int PRED2HT = 6;

	public static final int UNDER15 = 7;
	public static final int OVER15 = 8;
	
	public static final int UNDER25 = 9;
	public static final int OVER25 = 10;

	public static final int UNDER35 = 11;
	public static final int OVER35 = 12;

	public static final int PRED1X = 13;
	public static final int PREDX2 = 14;
	public static final int PRED12 = 15;

	public static final int PRED1XHT = 16;
	public static final int PREDX2HT = 17;
	public static final int PRED12HT = 18;

	public static final int GOAL = 19;
	public static final int NOGOAL = 20;
	
	
	@ManyToOne
    @JoinColumn(name = "forecast_type", referencedColumnName = "id", nullable = false)
	private ForecastType forecastType;

	@Id
	private int id;
	private String description;

	public ForecastTypeOccurrence(int pId)
	{
		id = pId;
	}
	
	public ForecastTypeOccurrence()
	{
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

	public ForecastType getForecastType() {
		return forecastType;
	}

	public void setForecastType(ForecastType forecastType) {
		this.forecastType = forecastType;
	}	
	
	
	
}
