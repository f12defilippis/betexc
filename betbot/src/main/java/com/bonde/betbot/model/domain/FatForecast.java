package com.bonde.betbot.model.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="forecast")
public class FatForecast implements Serializable{

	private static final long serialVersionUID = -2070622765707630996L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;
	
	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name = "table_match", referencedColumnName = "id", nullable = false)
	private Match match;

	@ManyToOne
    @JoinColumn(name = "source", referencedColumnName = "id", nullable = false)
	private Source source;

	@ManyToOne
    @JoinColumn(name = "forecast_type_occurrence", referencedColumnName = "id", nullable = false)
	private ForecastTypeOccurrence forecastTypeOccurrence;

	@ManyToOne
    @JoinColumn(name = "forecast_value", referencedColumnName = "id", nullable = false)
	private ForecastValue forecastValue;
	
	@OneToMany(mappedBy = "forecast", cascade = CascadeType.ALL)
	private Set<ValueBet> valueBets;	
	
	
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

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public ForecastTypeOccurrence getForecastTypeOccurrence() {
		return forecastTypeOccurrence;
	}

	public void setForecastTypeOccurrence(ForecastTypeOccurrence forecastTypeOccurrence) {
		this.forecastTypeOccurrence = forecastTypeOccurrence;
	}

	public ForecastValue getForecastValue() {
		return forecastValue;
	}

	public void setForecastValue(ForecastValue forecastValue) {
		this.forecastValue = forecastValue;
	}

	public Set<ValueBet> getValueBets() {
		return valueBets;
	}

	public void setValueBets(Set<ValueBet> valueBets) {
		this.valueBets = valueBets;
	}

	
	

}
