package com.bonde.betbot.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bonde.betbot.util.DateUtil;

@Entity
public class ForecastSummary implements Serializable{

	private static final long serialVersionUID = 2051575735003311361L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;

	
	private Date finalDate;
	private Integer daysBefore;
	
	@ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id", nullable = false)
	private Source source;

	@ManyToOne
    @JoinColumn(name = "forecast_value", referencedColumnName = "id", nullable = true)
	private ForecastValue forecastValue;

	@ManyToOne
    @JoinColumn(name = "value_group", referencedColumnName = "id", nullable = true)
	private ValueGroup valueGroup;

	@ManyToOne
    @JoinColumn(name = "value_bet", referencedColumnName = "id", nullable = true)
	private ForecastValue valueBet;

	@ManyToOne
    @JoinColumn(name = "value_bet_group", referencedColumnName = "id", nullable = true)
	private ValueGroup valueBetGroup;

	@ManyToOne
    @JoinColumn(name = "competition", referencedColumnName = "id", nullable = true)
	private Competition competition;
	
	@ManyToOne
    @JoinColumn(name = "forecast_type_occurrence", referencedColumnName = "id", nullable = true)
	private ForecastTypeOccurrence forecastTypeOccurrence;	

	private Integer numVerified;
	private Integer numOccurrences;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFinalDate() {
		return finalDate;
	}
	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}
	public Integer getDaysBefore() {
		return daysBefore;
	}
	public void setDaysBefore(Integer daysBefore) {
		this.daysBefore = daysBefore;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	public ForecastValue getForecastValue() {
		return forecastValue;
	}
	public void setForecastValue(ForecastValue forecastValue) {
		this.forecastValue = forecastValue;
	}
	public ValueGroup getValueGroup() {
		return valueGroup;
	}
	public void setValueGroup(ValueGroup valueGroup) {
		this.valueGroup = valueGroup;
	}
	public ForecastValue getValueBet() {
		return valueBet;
	}
	public void setValueBet(ForecastValue valueBet) {
		this.valueBet = valueBet;
	}
	public ValueGroup getValueBetGroup() {
		return valueBetGroup;
	}
	public void setValueBetGroup(ValueGroup valueBetGroup) {
		this.valueBetGroup = valueBetGroup;
	}
	public Competition getCompetition() {
		return competition;
	}
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	public Integer getNumVerified() {
		return numVerified;
	}
	public void setNumVerified(Integer numVerified) {
		this.numVerified = numVerified;
	}
	public Integer getNumOccurrences() {
		return numOccurrences;
	}
	public void setNumOccurrences(Integer numOccurrences) {
		this.numOccurrences = numOccurrences;
	}
	public ForecastTypeOccurrence getForecastTypeOccurrence() {
		return forecastTypeOccurrence;
	}
	public void setForecastTypeOccurrence(
			ForecastTypeOccurrence forecastTypeOccurrence) {
		this.forecastTypeOccurrence = forecastTypeOccurrence;
	}
	@Override
	public String toString() {
		return "ForecastSummary [id=" + id + ", finalDate=" + DateUtil.fromDateToString(finalDate)
				+ ", daysBefore=" + daysBefore + ", source=" + source.getDescription()
				+ ", forecastValue=" + getForecastValueDescription(forecastValue) + ", valueGroup="
				+ getValueGroupDescription(valueGroup) + ", valueBet=" + getForecastValueDescription(valueBet) + ", valueBetGroup="
				+ getValueGroupDescription(valueBetGroup) + ", competition=" + getCompetitionDescription(competition)
				+ ", forecastTypeOccurrence=" + getForecastTypeOccurrenceDescription(forecastTypeOccurrence)
				+ ", numVerified=" + numVerified + ", numOccurrences="
				+ numOccurrences + "]";
	}
	
	private String getForecastValueDescription(ForecastValue fv)
	{
		if(fv!=null)
			return String.valueOf(fv.getValue());
		return null;
	}
	
	private String getValueGroupDescription(ValueGroup fv)
	{
		if(fv!=null)
			return fv.getDescription();
		return null;
	}	
	
	private String getCompetitionDescription(Competition fv)
	{
		if(fv!=null)
			return fv.getDescription();
		return null;
	}
	
	private String getForecastTypeOccurrenceDescription(ForecastTypeOccurrence fv)
	{
		if(fv!=null)
			return fv.getDescription();
		return null;
	}
	
	

	
	

}
