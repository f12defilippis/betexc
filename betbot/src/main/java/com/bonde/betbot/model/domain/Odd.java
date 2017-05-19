package com.bonde.betbot.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
public class Odd implements Serializable{

	private static final long serialVersionUID = 3524473376908102188L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;
	
	
	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name = "match_id", referencedColumnName = "id")
	private Match match;
	


	@ManyToOne
    @JoinColumn(name = "forecast_type_occurrence", referencedColumnName = "id", nullable = false)
	private ForecastTypeOccurrence forecastTypeOccurrence;
	
	private Date dateCreated;
	private Date dateUpdated;
	
	
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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	private double firstValue;

	public double getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(double firstValue) {
		this.firstValue = firstValue;
	}
	
	
	
}
