package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ForecastValueGroup implements Serializable{

	private static final long serialVersionUID = 2946114932571313644L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;

	@ManyToOne
    @JoinColumn(name = "forecast_value", referencedColumnName = "id", nullable = false)
	private ForecastValue forecastValue;

	@ManyToOne
    @JoinColumn(name = "value_group", referencedColumnName = "id", nullable = false)
	private ValueGroup valueGroup;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	
	
}
