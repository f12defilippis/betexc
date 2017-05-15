package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Sport implements Serializable{

	private static final long serialVersionUID = 5834354411116517401L;

	@Id
	private int id;
	private String description;
	
	
	public Sport(int i) {
		id = i;
	}
	
	public Sport() {
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
		Sport other = (Sport) obj;
		if (id != other.id)
			return false;
		return true;
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
	
}
