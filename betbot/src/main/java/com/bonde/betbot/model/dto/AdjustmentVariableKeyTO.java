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



	@Override
	public String toString() {
		return "AdjustmentVariableKeyTO [forecastValue=" + forecastValue
				+ ", forecastValueGroup=" + forecastValueGroup
				+ ", forecastTypeOccurrence=" + forecastTypeOccurrence
				+ ", valueBet=" + valueBet + ", valueBetGroup=" + valueBetGroup
				+ ", competition=" + competition + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((competition == null) ? 0 : competition.hashCode());
		result = prime
				* result
				+ ((forecastTypeOccurrence == null) ? 0
						: forecastTypeOccurrence.hashCode());
		result = prime * result
				+ ((forecastValue == null) ? 0 : forecastValue.hashCode());
		result = prime
				* result
				+ ((forecastValueGroup == null) ? 0 : forecastValueGroup
						.hashCode());
		result = prime * result
				+ ((valueBet == null) ? 0 : valueBet.hashCode());
		result = prime * result
				+ ((valueBetGroup == null) ? 0 : valueBetGroup.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdjustmentVariableKeyTO other = (AdjustmentVariableKeyTO) obj;
		if (competition == null) {
			if (other.competition != null)
				return false;
		} else if (!competition.equals(other.competition))
			return false;
		if (forecastTypeOccurrence == null) {
			if (other.forecastTypeOccurrence != null)
				return false;
		} else if (!forecastTypeOccurrence.equals(other.forecastTypeOccurrence))
			return false;
		if (forecastValue == null) {
			if (other.forecastValue != null)
				return false;
		} else if (!forecastValue.equals(other.forecastValue))
			return false;
		if (forecastValueGroup == null) {
			if (other.forecastValueGroup != null)
				return false;
		} else if (!forecastValueGroup.equals(other.forecastValueGroup))
			return false;
		if (valueBet == null) {
			if (other.valueBet != null)
				return false;
		} else if (!valueBet.equals(other.valueBet))
			return false;
		if (valueBetGroup == null) {
			if (other.valueBetGroup != null)
				return false;
		} else if (!valueBetGroup.equals(other.valueBetGroup))
			return false;
		return true;
	}

	
	
	
}
