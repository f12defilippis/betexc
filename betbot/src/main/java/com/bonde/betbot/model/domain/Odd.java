package com.bonde.betbot.model.domain;

import javax.persistence.Entity;

@Entity
public class Odd extends Result{

	private static final long serialVersionUID = 3524473376908102188L;

	private double firstValue;

	public double getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(double firstValue) {
		this.firstValue = firstValue;
	}
	
	
	
}
