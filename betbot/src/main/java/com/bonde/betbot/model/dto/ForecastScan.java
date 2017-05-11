package com.bonde.betbot.model.dto;

import java.util.Date;
import java.util.List;

public class ForecastScan {

	private String source;
	private Date datetime;
	
	private List<ForecastMatchRowTO> rows;

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
	
	
	
	
}
