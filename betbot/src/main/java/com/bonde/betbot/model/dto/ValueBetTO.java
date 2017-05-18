package com.bonde.betbot.model.dto;

import java.io.Serializable;

import com.bonde.betbot.model.domain.Forecast;
import com.bonde.betbot.model.domain.Odd;

public class ValueBetTO implements Serializable{

	private static final long serialVersionUID = 575493468157112889L;

	private Forecast forecast;
	
	private Odd odd;

	public Forecast getForecast() {
		return forecast;
	}

	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}

	public Odd getOdd() {
		return odd;
	}

	public void setOdd(Odd odd) {
		this.odd = odd;
	}
	
	
	
	
	
}
