package com.bonde.betbot.model.dto;

import java.io.Serializable;

public class ValueBetKeyTO implements Serializable{

	private static final long serialVersionUID = 5192359353117641362L;

	private Integer matchId;
	private Integer sourceId;
	private Integer forecastTypeOccurrenceId;
	

	public ValueBetKeyTO(Integer matchId, Integer sourceId, Integer forecastTypeOccurrenceId) {
		super();
		this.matchId = matchId;
		this.sourceId = sourceId;
		this.forecastTypeOccurrenceId = forecastTypeOccurrenceId;
	}
	public Integer getMatchId() {
		return matchId;
	}
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	public Integer getForecastTypeOccurrenceId() {
		return forecastTypeOccurrenceId;
	}
	public void setForecastTypeOccurrenceId(Integer forecastTypeOccurrenceId) {
		this.forecastTypeOccurrenceId = forecastTypeOccurrenceId;
	}
	@Override
	public String toString() {
		return "ValueBetKeyTO [matchId=" + matchId + ", sourceId=" + sourceId + ", forecastTypeOccurrenceId="
				+ forecastTypeOccurrenceId + "]";
	}
	
	
	
	
}
