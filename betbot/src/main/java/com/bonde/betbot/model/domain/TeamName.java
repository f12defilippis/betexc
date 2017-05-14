package com.bonde.betbot.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TeamName implements Serializable{

	private static final long serialVersionUID = 5976186445122486489L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;

	@Column(name = "tname")
	private String name;
	
	@ManyToOne
    @JoinColumn(name = "team", referencedColumnName = "id", nullable = false)
	private Team team;
	
	@ManyToOne
    @JoinColumn(name = "source", referencedColumnName = "id", nullable = false)
	private Source source;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	
	
	
	
	
}
