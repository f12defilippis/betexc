package com.bonde.betbot.model.domain;

import javax.persistence.Entity;

import com.bonde.betbot.model.domain.common.CommonDescriptionEntity;

@Entity
public class Source extends CommonDescriptionEntity{

	private static final long serialVersionUID = -4040298735765578929L;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
