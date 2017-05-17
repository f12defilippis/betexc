package com.bonde.betbot.model.dto;

import java.util.Date;

public class ForecastMatchRowTO {

//	private String date;
//	private String hour;

	private Date date;
	
	private String country;
	private String competition;
	private String homeTeam;
	private String awayTeam;

	private String pred1;
	private String predX;
	private String pred2;

	private String pred1ht;
	private String predXht;
	private String pred2ht;
	
	private String predOver15;
	private String predOver25;
	private String predOver35;
	
	private String predBTS;
	private String predOTS;
	
	private String result;
	private String resultHT;
	
	
	public String getResultHT() {
		return resultHT;
	}
	public void setResultHT(String resultHT) {
		this.resultHT = resultHT;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCompetition() {
		return competition;
	}
	public void setCompetition(String competition) {
		this.competition = competition;
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
	public String getPred1() {
		return pred1;
	}
	public void setPred1(String pred1) {
		this.pred1 = pred1;
	}
	public String getPredX() {
		return predX;
	}
	public void setPredX(String predX) {
		this.predX = predX;
	}
	public String getPred2() {
		return pred2;
	}
	public void setPred2(String pred2) {
		this.pred2 = pred2;
	}
	public String getPred1ht() {
		return pred1ht;
	}
	public void setPred1ht(String pred1ht) {
		this.pred1ht = pred1ht;
	}
	public String getPredXht() {
		return predXht;
	}
	public void setPredXht(String predXht) {
		this.predXht = predXht;
	}
	public String getPred2ht() {
		return pred2ht;
	}
	public void setPred2ht(String pred2ht) {
		this.pred2ht = pred2ht;
	}
	public String getPredOver15() {
		return predOver15;
	}
	public void setPredOver15(String predOver15) {
		this.predOver15 = predOver15;
	}
	public String getPredOver25() {
		return predOver25;
	}
	public void setPredOver25(String predOver25) {
		this.predOver25 = predOver25;
	}
	public String getPredOver35() {
		return predOver35;
	}
	public void setPredOver35(String predOver35) {
		this.predOver35 = predOver35;
	}
	public String getPredBTS() {
		return predBTS;
	}
	public void setPredBTS(String predBTS) {
		this.predBTS = predBTS;
	}
	public String getPredOTS() {
		return predOTS;
	}
	public void setPredOTS(String predOTS) {
		this.predOTS = predOTS;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	
	
	
	
	
}
