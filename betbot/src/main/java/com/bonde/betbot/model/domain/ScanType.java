package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ScanType implements Serializable{

	public static final int FORECAST = 1;
	public static final int ODD = 2;
	public static final int RESULT = 3;
	
	private static final long serialVersionUID = 1139274885169007637L;

	@Id
	private int id;
	private String description;
	
	
	public ScanType(int forecast2) {
		id=forecast2;
	}
	
	public ScanType() {
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScanType other = (ScanType) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
}
