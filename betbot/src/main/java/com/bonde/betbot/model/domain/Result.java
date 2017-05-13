package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Result implements Serializable{
	
	private static final long serialVersionUID = 8831804847883821760L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name = "match", referencedColumnName = "id", nullable = false)
	private Match match;

	@ManyToOne
    @JoinColumn(name = "forecast_type_occurrence", referencedColumnName = "id", nullable = false)
	private ForecastTypeOccurrence forecastTypeOccurrence;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public ForecastTypeOccurrence getForecastTypeOccurrence() {
		return forecastTypeOccurrence;
	}

	public void setForecastTypeOccurrence(ForecastTypeOccurrence forecastTypeOccurrence) {
		this.forecastTypeOccurrence = forecastTypeOccurrence;
	}

	
	
	
}
