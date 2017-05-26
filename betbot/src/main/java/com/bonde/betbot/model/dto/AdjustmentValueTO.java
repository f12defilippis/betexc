package com.bonde.betbot.model.dto;

import java.io.Serializable;

public class AdjustmentValueTO implements Serializable{

	private static final long serialVersionUID = 4020155325547592743L;

	private int eventsVerified;
	private int eventsNotVerified;
	
	
	public int getEventsVerified() {
		return eventsVerified;
	}
	public void setEventsVerified(int eventsVerified) {
		this.eventsVerified = eventsVerified;
	}
	public int getEventsNotVerified() {
		return eventsNotVerified;
	}
	public void setEventsNotVerified(int eventsNotVerified) {
		this.eventsNotVerified = eventsNotVerified;
	}
	
	public int getTotalEvents()
	{
		return eventsVerified+eventsNotVerified;
	}
	
	public void addVerified()
	{
		eventsVerified++;
	}
	
	public void addNotVerified()
	{
		eventsNotVerified++;
	}
	
	public void addEvents(boolean verified)
	{
		if(verified)
		{
			eventsVerified++;
		}else
		{
			eventsNotVerified++;
		}
	}
	
	
	
}
