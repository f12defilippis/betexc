package com.bonde.betbot.model.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ForecastFinal implements Serializable{

	private static final long serialVersionUID = -4490621535790189849L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;

	@ManyToOne
    @JoinColumn(name = "odd", referencedColumnName = "id", nullable = false)
	private Odd odd;

	@ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id", nullable = false)
	private Source source;
	
	@ManyToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id", nullable = false)
	private Match match;

	@ManyToOne
    @JoinColumn(name = "forecast_type_occerrence", referencedColumnName = "id", nullable = false)
	private ForecastTypeOccurrence forecastTypeOccurrence;;
	
	private double initialProbability;
	private double adjustedProbability;

	private double initialMargin;
	private double adjustedMargin;
	
	private double probabilityVariation;
	
	private double sqmaverage;
	
	private int forecastTypeOccurrenceOrder;
	
	private String forecastSummaryParameter;
	
	
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

	public double getInitialProbability() {
		return initialProbability;
	}

	public void setInitialProbability(double initialProbability) {
		this.initialProbability = initialProbability;
	}

	public double getAdjustedProbability() {
		return adjustedProbability;
	}

	public void setAdjustedProbability(double adjustedProbability) {
		this.adjustedProbability = adjustedProbability;
	}

	public double getInitialMargin() {
		return initialMargin;
	}

	public void setInitialMargin(double initialMargin) {
		this.initialMargin = initialMargin;
	}

	public double getAdjustedMargin() {
		return adjustedMargin;
	}

	public void setAdjustedMargin(double adjustedMargin) {
		this.adjustedMargin = adjustedMargin;
	}

	public double getProbabilityVariation() {
		return probabilityVariation;
	}

	public void setProbabilityVariation(double probabilityVariation) {
		this.probabilityVariation = probabilityVariation;
	}

	public int getForecastTypeOccurrenceOrder() {
		return forecastTypeOccurrenceOrder;
	}

	public void setForecastTypeOccurrenceOrder(int forecastTypeOccurrenceOrder) {
		this.forecastTypeOccurrenceOrder = forecastTypeOccurrenceOrder;
	}

	public String getForecastSummaryParameter() {
		return forecastSummaryParameter;
	}

	public void setForecastSummaryParameter(String forecastSummaryParameter) {
		this.forecastSummaryParameter = forecastSummaryParameter;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public static Comparator<ForecastFinal> sortByAdjustedProbability() {
		Comparator<ForecastFinal> comp = new Comparator<ForecastFinal>() {
			@Override
			public int compare(ForecastFinal s1, ForecastFinal s2) {
				if (s1.adjustedProbability < s2.adjustedProbability)
					return -1;
				if (s1.adjustedProbability > s2.adjustedProbability)
					return 1;
				return 0;
			}
		};
		return comp;
	}
	
	public static Comparator<ForecastFinal> sortByAdjustedProbabilityDesc() {
		Comparator<ForecastFinal> comp = new Comparator<ForecastFinal>() {
			@Override
			public int compare(ForecastFinal s1, ForecastFinal s2) {
				if (s1.adjustedProbability > s2.adjustedProbability)
					return -1;
				if (s1.adjustedProbability < s2.adjustedProbability)
					return 1;
				return 0;
			}
		};
		return comp;
	}

	public double getSqmaverage() {
		return sqmaverage;
	}

	public void setSqmaverage(double sqmaverage) {
		this.sqmaverage = sqmaverage;
	}
	
	
	
	
}
