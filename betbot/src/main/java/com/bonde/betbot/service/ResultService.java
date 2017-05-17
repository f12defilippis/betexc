package com.bonde.betbot.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.bonde.betbot.model.domain.ForecastType;
import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Result;
import com.bonde.betbot.model.domain.ScanType;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.repository.ResultRepository;

public class ResultService extends ForecastResultService{
	
	@Autowired
	ResultRepository resultRepository;
	
	
	@Transactional
	public String getStatareaResults(Date date)
	{
		try {
			Date start = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(date);
			
			log.info("Starting crawling match result from statarea for date " + strDate);
			ForecastScan scan = statareaService.crawlResult(date);
			log.info("Finished crawling matches from statarea. " + scan.getRows().size() + " matches found");
			log.info("Starting saving forecast");
			int matchSkipped = saveData(scan, date);
			saveData(scan, date);
			log.info("Finished saving results for date " + strDate + "." + (scan.getRows().size() - matchSkipped) + " Matches Saved. " + matchSkipped + " Matches Skipped");

			Date end = new Date();

			summaryService.saveSummary(new Source(Source.STATAREA), new ScanType(ScanType.RESULT), start, end, scan.getRows().size() - matchSkipped, scan.getRows().size());
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
			return "KO";
		}
		
		return "OK";
	}	
	
	@Transactional
	public String getLivescoreResults(Date date)
	{
		try {
			Date start = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(date);
			
			log.info("Starting crawling match result from livescore24 for date " + strDate);
			ForecastScan scan = livescore24Service.crawlResult(date);
			log.info("Finished crawling matches from livescore24. " + scan.getRows().size() + " matches found");
			log.info("Starting saving results");
			int matchSkipped = saveData(scan, date);
			saveData(scan, date);
			log.info("Finished saving results for date " + strDate + "." + (scan.getRows().size() - matchSkipped) + " Matches Saved. " + matchSkipped + " Matches Skipped");
			Date end = new Date();
			summaryService.saveSummary(new Source(Source.LIVESCORE), new ScanType(ScanType.RESULT), start, end, scan.getRows().size() - matchSkipped, scan.getRows().size());

		} catch (Exception e) {
			e.printStackTrace();
			return "KO";
		}
		
		return "OK";
	}		
	
	
	public int saveData(ForecastScan scan, Date date)
	{
		int matchSkipped = 0;
		for(ForecastMatchRowTO row : scan.getRows())
		{
			Source source = sourceRepository.findOne(Integer.parseInt(scan.getSource()));
			Match match = saveMatchData(row, scan, date, source);
			if(match!=null)
			{
				saveResult(ForecastType.PRED1X2, row.getResult(), row.getResultHT(), match);
				saveResult(ForecastType.PRED1X2HT, row.getResult(), row.getResultHT(), match);
				saveResult(ForecastType.UO15, row.getResult(), row.getResultHT(), match);
				saveResult(ForecastType.UO25, row.getResult(), row.getResultHT(), match);
				saveResult(ForecastType.UO35, row.getResult(), row.getResultHT(), match);
				saveResult(ForecastType.DC, row.getResult(), row.getResultHT(), match);
				saveResult(ForecastType.DCHT, row.getResult(), row.getResultHT(), match);
			}else
			{
				matchSkipped++;
			}
		}
		return matchSkipped;

	}

	public void saveResult(int forecastType, String result, String resultht, Match match)
	{
		ForecastType ft = new ForecastType();
		ForecastTypeOccurrence fto = new ForecastTypeOccurrence();
		int homeScore = Integer.valueOf(result.split("-")[0].trim());
		int awayScore = Integer.valueOf(result.split("-")[1].trim());
		int homeScoreHt = Integer.valueOf(result.split(":")[0].trim());
		int awayScoreHt = Integer.valueOf(result.split(":")[1].trim());
		switch(forecastType)
		{
			case ForecastType.PRED1X2:
				ft.setId(ForecastType.PRED1X2);
				if(homeScore>awayScore)
				{
					fto.setId(ForecastTypeOccurrence.PRED1);
				}else if(homeScore==awayScore)
				{
					fto.setId(ForecastTypeOccurrence.PREDX);
				}else
				{
					fto.setId(ForecastTypeOccurrence.PRED2);
				}
				break;
			case ForecastType.PRED1X2HT:
				ft.setId(ForecastType.PRED1X2HT);
				if(homeScoreHt>awayScoreHt)
				{
					fto.setId(ForecastTypeOccurrence.PRED1HT);
				}else if(homeScoreHt==awayScoreHt)
				{
					fto.setId(ForecastTypeOccurrence.PREDXHT);
				}else
				{
					fto.setId(ForecastTypeOccurrence.PRED2HT);
				}
				break;
			case ForecastType.UO15:
				if(homeScore+awayScore>1)
				{
					fto.setId(ForecastTypeOccurrence.OVER15);
				}else
				{
					fto.setId(ForecastTypeOccurrence.UNDER15);
				}
				break;
			case ForecastType.UO25:
				if(homeScore+awayScore>2)
				{
					fto.setId(ForecastTypeOccurrence.OVER25);
				}else
				{
					fto.setId(ForecastTypeOccurrence.UNDER25);
				}
				break;
			case ForecastType.UO35:
				if(homeScore+awayScore>3)
				{
					fto.setId(ForecastTypeOccurrence.OVER35);
				}else
				{
					fto.setId(ForecastTypeOccurrence.UNDER35);
				}
				break;
			case ForecastType.DC:
				ForecastTypeOccurrence fto2 = new ForecastTypeOccurrence();

				if(homeScore>awayScore)
				{
					fto.setId(ForecastTypeOccurrence.PRED1X);
					fto2.setId(ForecastTypeOccurrence.PRED12);
				}else if(homeScore==awayScore)
				{
					fto.setId(ForecastTypeOccurrence.PREDX2);
					fto2.setId(ForecastTypeOccurrence.PRED1X);
				}else
				{
					fto.setId(ForecastTypeOccurrence.PREDX2);
					fto2.setId(ForecastTypeOccurrence.PRED12);
				}

				
				Result res2 = new Result();
				res2.setForecastTypeOccurrence(fto2);
				res2.setMatch(match);
				resultRepository.save(res2);
				
				break;
			case ForecastType.DCHT:
				
				ForecastTypeOccurrence fto3 = new ForecastTypeOccurrence();

				if(homeScoreHt>awayScoreHt)
				{
					fto.setId(ForecastTypeOccurrence.PRED1XHT);
					fto3.setId(ForecastTypeOccurrence.PRED12HT);
				}else if(homeScoreHt==awayScoreHt)
				{
					fto.setId(ForecastTypeOccurrence.PREDX2HT);
					fto3.setId(ForecastTypeOccurrence.PRED1XHT);
				}else
				{
					fto.setId(ForecastTypeOccurrence.PREDX2HT);
					fto3.setId(ForecastTypeOccurrence.PRED12HT);
				}

				
				Result res3 = new Result();
				res3.setForecastTypeOccurrence(fto3);
				res3.setMatch(match);
				resultRepository.save(res3);				
				
				break;
		}
		
		Result res = new Result();
		res.setForecastTypeOccurrence(fto);
		res.setMatch(match);
		
		resultRepository.save(res);
		
		
		
	}




}
