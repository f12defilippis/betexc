package com.bonde.betbot.model.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bonde.betbot.model.domain.common.CommonDescriptionEntity;

@Entity
public class ForecastTypeOccurrence extends CommonDescriptionEntity{

	private static final long serialVersionUID = -3450975300683550829L;

	@ManyToOne
    @JoinColumn(name = "forecast_type", referencedColumnName = "id", nullable = false)
	private ForecastType forecastType;

	public ForecastType getForecastType() {
		return forecastType;
	}

	public void setForecastType(ForecastType forecastType) {
		this.forecastType = forecastType;
	}	
	
	
	
}
