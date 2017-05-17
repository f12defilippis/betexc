package com.bonde.betbot.model.dto;

import java.io.Serializable;
import java.util.Date;

public class OddMatchRowTO implements Serializable{
	
	private static final long serialVersionUID = -2910451445869546094L;
	
	private Date matchDate;
	private String homeTeam;
	private String awayTeam;
	private double odd1;
	private double oddX;
	private double odd2;
	public Date getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	public String getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}
	public double getOdd1() {
		return odd1;
	}
	public void setOdd1(double odd1) {
		this.odd1 = odd1;
	}
	public double getOddX() {
		return oddX;
	}
	public void setOddX(double oddX) {
		this.oddX = oddX;
	}
	public double getOdd2() {
		return odd2;
	}
	public void setOdd2(double odd2) {
		this.odd2 = odd2;
	}

	
	

}
