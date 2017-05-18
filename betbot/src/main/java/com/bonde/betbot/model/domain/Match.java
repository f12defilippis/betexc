package com.bonde.betbot.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="table_match")
public class Match implements Serializable{

	private static final long serialVersionUID = 1869459056351802363L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;
	
	private Date dateStart;
	
	@ManyToOne
    @JoinColumn(name = "homeTeam", referencedColumnName = "id", nullable = false)
	private Team homeTeam;
	
	@ManyToOne
    @JoinColumn(name = "awayTeam", referencedColumnName = "id", nullable = false)
	private Team awayTeam;

	@ManyToOne
    @JoinColumn(name = "competition", referencedColumnName = "id", nullable = false)
	private Competition competition;
	
	private String finalScore;
	private String halftimeScore;
	
	private Date dateCreated;
	private Date dateUpdated;
	
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Team getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}
	public Team getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}
	public Competition getCompetition() {
		return competition;
	}
	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	public String getFinalScore() {
		return finalScore;
	}
	public void setFinalScore(String finalScore) {
		this.finalScore = finalScore;
	}
	public String getHalftimeScore() {
		return halftimeScore;
	}
	public void setHalftimeScore(String halftimeScore) {
		this.halftimeScore = halftimeScore;
	}
	
	
	

	
}
