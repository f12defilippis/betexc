package com.bonde.betbot.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ValueBet implements Serializable{

	private static final long serialVersionUID = -4490621535790189849L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;

	@ManyToOne
    @JoinColumn(name = "odd_id", referencedColumnName = "id", nullable = false)
	private Odd odd;

	@ManyToOne
    @JoinColumn(name = "forecast_id", referencedColumnName = "id", nullable = false)
	private Forecast forecast;
	
	@ManyToOne
    @JoinColumn(name = "margin", referencedColumnName = "id", nullable = false)
	private ForecastValue margin;

	private double expectedOdd;
	
	private Date dateCreated;
	private Date dateUpdated;
	
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
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Odd getOdd() {
		return odd;
	}

	public void setOdd(Odd odd) {
		this.odd = odd;
	}

	public Forecast getForecast() {
		return forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}

	public ForecastValue getMargin() {
		return margin;
	}

	public void setMargin(ForecastValue margin) {
		this.margin = margin;
	}

	public double getExpectedOdd() {
		return expectedOdd;
	}

	public void setExpectedOdd(double expectedOdd) {
		this.expectedOdd = expectedOdd;
	}
	
	
	
	
	
}
