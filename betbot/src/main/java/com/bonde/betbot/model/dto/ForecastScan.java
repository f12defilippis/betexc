package com.bonde.betbot.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastScan {

	private String source;
	private Date datetime;
	private String sport;
	private String season;
	
	private boolean calculatehour = true;
	
	private List<ForecastMatchRowTO> rows;

	public ForecastScan()
	{
		rows = new ArrayList<ForecastMatchRowTO>();
	}
	
	
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public List<ForecastMatchRowTO> getRows() {
		return rows;
	}

	public void setRows(List<ForecastMatchRowTO> rows) {
		this.rows = rows;
	}



	public String getSport() {
		return sport;
	}



	public void setSport(String sport) {
		this.sport = sport;
	}



	public String getSeason() {
		return season;
	}



	public void setSeason(String season) {
		this.season = season;
	}



	public boolean isCalculatehour() {
		return calculatehour;
	}



	public void setCalculatehour(boolean calculatehour) {
		this.calculatehour = calculatehour;
	}
	
	
	
	
}
