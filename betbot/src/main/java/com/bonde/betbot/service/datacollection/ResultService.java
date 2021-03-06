package com.bonde.betbot.service.datacollection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.ForecastType;
import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Result;
import com.bonde.betbot.model.domain.ScanType;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;
import com.bonde.betbot.repository.ResultRepository;

@Service
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

			summaryService.saveSummary(new Source(Source.STATAREA), new ScanType(ScanType.RESULT), start, end, scan.getRows().size() - matchSkipped, scan.getRows().size(),date);
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
			return "KO";
		}
		
		return "OK";
	}	
	
	@Transactional
	public String getLivescore24Results(Date date)
	{
		try {
			Date start = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(date);
			
			log.info("Starting crawling match result from livescore24 for date " + strDate);
			ForecastScan scan = livescore24Service.crawlResultHtmlUnit(date);
			log.info("Finished crawling matches from livescore24. " + scan.getRows().size() + " matches found");
			log.info("Starting saving results");
			int matchSkipped = saveData(scan, date);
			log.info("Finished saving results for date " + strDate + "." + (scan.getRows().size() - matchSkipped) + " Matches Saved. " + matchSkipped + " Matches Skipped");
			Date end = new Date();
			summaryService.saveSummary(new Source(Source.LIVESCORE24), new ScanType(ScanType.RESULT), start, end, scan.getRows().size() - matchSkipped, scan.getRows().size(), date);

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
			try
			{
				Source source = sourceRepository.findOne(Integer.parseInt(scan.getSource()));
				Match match = saveMatchData(row, scan, date, source);
				List<Result> matchResults = resultRepository.findByMatch(match);
				Map<Integer,Integer> forecastTypeMap = new HashMap<Integer,Integer>();

				if(matchResults!=null && matchResults.size()>0)
				{
					for(Result res : matchResults)
					{
						forecastTypeMap.put(Integer.valueOf(res.getForecastTypeOccurrence().getForecastType().getId()), Integer.valueOf(res.getForecastTypeOccurrence().getForecastType().getId()));
					}
				}
				
				
				if(match!=null && row.getResult()!=null && !row.getResult().trim().equals("-"))
				{
					saveResult(ForecastType.PRED1X2, row.getResult(), row.getResultHT(), match,forecastTypeMap);
					saveResult(ForecastType.UO15, row.getResult(), row.getResultHT(), match,forecastTypeMap);
					saveResult(ForecastType.UO25, row.getResult(), row.getResultHT(), match,forecastTypeMap);
					saveResult(ForecastType.UO35, row.getResult(), row.getResultHT(), match,forecastTypeMap);
					saveResult(ForecastType.DC, row.getResult(), row.getResultHT(), match,forecastTypeMap);
					if(row.getResultHT()!=null && !row.getResultHT().equals(""))
					{
						saveResult(ForecastType.PRED1X2HT, row.getResult(), row.getResultHT(), match,forecastTypeMap);
						saveResult(ForecastType.DCHT, row.getResult(), row.getResultHT(), match,forecastTypeMap);
					}
				}else
				{
					matchSkipped++;
				}
			}catch(NumberFormatException ioex)
			{
				log.warn("NumberFormatException during saving match row: " + ioex.getMessage());
				matchSkipped++;
			}catch(Exception ioex)
			{
				log.warn("Exception during saving match row: " + ioex.getMessage());
				matchSkipped++;
			}

		}
		return matchSkipped;

	}

	public void saveResult(int forecastType, String result, String resultht, Match match, Map<Integer,Integer> forecastTypeMap)
	{
		
		if(forecastTypeMap.get(Integer.valueOf(forecastType))!=null)
		{
			log.debug("Result yet saved... Skipping record. FT: " + forecastType + " Match: " + match.getHomeTeam().getName() + " - " + match.getAwayTeam().getName() + " Result:" + result);
			return;
		}
		
		ForecastType ft = new ForecastType();
		ForecastTypeOccurrence fto = new ForecastTypeOccurrence();
		int homeScore = Integer.valueOf(result.split("-")[0].trim());
		int awayScore = Integer.valueOf(result.split("-")[1].trim());
		
		Date now = new Date();
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
				int homeScoreHt = Integer.valueOf(resultht.split(":")[0].trim());
				int awayScoreHt = Integer.valueOf(resultht.split(":")[1].trim());
				
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
				res2.setDateCreated(now);
				res2.setDateUpdated(now);
				resultRepository.save(res2);
				
				break;
			case ForecastType.DCHT:
				homeScoreHt = Integer.valueOf(resultht.split(":")[0].trim());
				awayScoreHt = Integer.valueOf(resultht.split(":")[1].trim());
				
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
				res3.setDateCreated(now);
				res3.setDateUpdated(now);
				resultRepository.save(res3);				
				
				break;
		}
		
		Result res = new Result();
		res.setForecastTypeOccurrence(fto);
		res.setMatch(match);
		res.setDateCreated(now);
		res.setDateUpdated(now);
		
		resultRepository.save(res);
		
		
		
	}




}
