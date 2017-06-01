package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Source implements Serializable{

	public static final int STATAREA = 1;
	public static final int LIVESCORE24 = 2;
	public static final int BETTING1X2 = 3;
	public static final int VITIBET = 4;
	public static final int FOREBET = 5;
	public static final int MYBET = 6;
	public static final int ZULUBET = 7;
	public static final int PICKFORWIN = 8;
	public static final int IAMBETTOR = 9;
	public static final int PROSOCCER = 10;
	public static final int BETSTUDY = 11;
	public static final int SCIBET = 12;

	
	
	private static final long serialVersionUID = -4040298735765578929L;

	private String url;
	
	@Id
	private int id;
	private String description;
	
	
	public Source(int source) {
		id = source;
	}

	public Source() {
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


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		Source other = (Source) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}
