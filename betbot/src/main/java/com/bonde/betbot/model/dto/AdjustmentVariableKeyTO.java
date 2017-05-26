package com.bonde.betbot.model.dto;

import java.io.Serializable;

public class AdjustmentVariableKeyTO implements Serializable{

	private static final long serialVersionUID = 6226910799494598130L;

	private Integer forecastValue;
	private Integer forecastValueGroup;
	private Integer forecastTypeOccurrence;
	private Integer valueBet;
	private Integer valueBetGroup;
	private Integer competition;
	
	
	
	public AdjustmentVariableKeyTO(){}
	
	
	
	public AdjustmentVariableKeyTO(AdjustmentVariableKeyTO other) {
		super();
		this.forecastValue = other.forecastValue;
		this.forecastTypeOccurrence = other.forecastTypeOccurrence;
		this.valueBet = other.valueBet;
		this.competition = other.competition;
		this.forecastValueGroup = other.forecastValueGroup;
		this.valueBetGroup = other.valueBetGroup;
	}
	
	
	public Integer getForecastValue() {
		return forecastValue;
	}
	public void setForecastValue(Integer forecastValue) {
		this.forecastValue = forecastValue;
	}
	public Integer getForecastTypeOccurrence() {
		return forecastTypeOccurrence;
	}
	public void setForecastTypeOccurrence(Integer forecastTypeOccurrence) {
		this.forecastTypeOccurrence = forecastTypeOccurrence;
	}
	public Integer getValueBet() {
		return valueBet;
	}
	
	public void setValueBet(Integer valueBet) {
		this.valueBet = valueBet;
	}
	
	public Integer getCompetition() {
		return competition;
	}
	
	public void setCompetition(Integer competition) {
		this.competition = competition;
	}



	public Integer getForecastValueGroup() {
		return forecastValueGroup;
	}



	public void setForecastValueGroup(Integer forecastValueGroup) {
		this.forecastValueGroup = forecastValueGroup;
	}



	public Integer getValueBetGroup() {
		return valueBetGroup;
	}



	public void setValueBetGroup(Integer valueBetGroup) {
		this.valueBetGroup = valueBetGroup;
	}

	
	
	
}
