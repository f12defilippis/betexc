package com.bonde.betbot.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.Forecast;
import com.bonde.betbot.model.domain.ForecastTypeOccurrence;
import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.Match;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.model.dto.ForecastMatchRowTO;
import com.bonde.betbot.model.dto.ForecastScan;

@Service
public class ForecastService extends ForecastResultService{


	
	@Transactional
	public String getStatareaForecast(Date date)
	{
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = format.format(date);
			
			System.out.println("Starting crawling matches from statarea for date " + strDate);
			ForecastScan scan = statareaService.crawlForecast(date);
			System.out.println("Finished crawling matches from statarea. " + scan.getRows().size() + " matches found");
			System.out.println("Starting saving forecast");
			saveData(scan, date);
			System.out.println("Finished saving forecast");
		} catch (Exception e) {
			e.printStackTrace();
			return "KO";
		}
		
		return "OK";
	}

	public void saveData(ForecastScan scan, Date date)
	{
		for(ForecastMatchRowTO row : scan.getRows())
		{
			Source source = sourceRepository.findOne(Integer.parseInt(scan.getSource()));

			Match match = saveMatchData(row, scan, date, source);
			
			//FORECAST
			forecastManagement(row.getPred1(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1));
			forecastManagement(row.getPredX(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDX));
			forecastManagement(row.getPred2(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED2));
			
			forecastManagement(row.getPred1ht(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1HT));
			forecastManagement(row.getPredXht(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDXHT));
			forecastManagement(row.getPred2ht(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED2HT));
			
			forecastManagement(row.getPredOver15(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.OVER15));
			forecastManagement(row.getPredOver25(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.OVER25));
			forecastManagement(row.getPredOver35(), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.OVER35));

			if(row.getPredOver15() != null && row.getPredOver15().length()>0)
			{
				int under15 = 100 - Integer.parseInt(row.getPredOver15());
				forecastManagement(String.valueOf(under15), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.UNDER15));
			}

			if(row.getPredOver25() != null && row.getPredOver25().length()>0)
			{
				int under25 = 100 - Integer.parseInt(row.getPredOver25());
				forecastManagement(String.valueOf(under25), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.UNDER25));
			}

			if(row.getPredOver35() != null && row.getPredOver35().length()>0)
			{
				int under35 = 100 - Integer.parseInt(row.getPredOver35());
				forecastManagement(String.valueOf(under35), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.UNDER35));
			}

			int pred1X = 100 - Integer.parseInt(row.getPred2());
			forecastManagement(String.valueOf(pred1X), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1X));
			
			int predX2 = 100 - Integer.parseInt(row.getPred1());
			forecastManagement(String.valueOf(predX2), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDX2));
			
			int pred12 = 100 - Integer.parseInt(row.getPredX());
			forecastManagement(String.valueOf(pred12), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED12));
			
			if(row.getPred2ht() != null && row.getPred2ht().length()>0)
			{
				int pred1Xht = 100 - Integer.parseInt(row.getPred2ht());
				forecastManagement(String.valueOf(pred1Xht), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED1XHT));
			}
			
			if(row.getPred1ht() != null && row.getPred1ht().length()>0)
			{
				int predX2ht = 100 - Integer.parseInt(row.getPred1ht());
				forecastManagement(String.valueOf(predX2ht), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PREDX2HT));
			}
			
			if(row.getPredXht() != null && row.getPredXht().length()>0)
			{
				int pred12ht = 100 - Integer.parseInt(row.getPredXht());
				forecastManagement(String.valueOf(pred12ht), match, source, new ForecastTypeOccurrence(ForecastTypeOccurrence.PRED12HT));
			}
			
		}
	}

	
	private void forecastManagement(String input, Match match, Source source, ForecastTypeOccurrence forecastTypeOccurrence)
	{
		String strForecast = input.length() == 1 ? "0" + input : input;
		strForecast = "0." + strForecast;
		
		double dblForecast = new Double(strForecast).doubleValue();

		ForecastValue forecastValue = new ForecastValue();
		
		List<ForecastValue> listFV = forecastValueRepository.findByValue(dblForecast);
		if(listFV!=null && listFV.size()>0)
		{
			forecastValue = listFV.get(0);
		}else
		{
			forecastValue.setValue(dblForecast);
			forecastValueRepository.save(forecastValue);
		}
				
		List<Forecast> listForecast = forecastRepository.findByMatchAndSourceAndForecastTypeOccurrence(match, source, forecastTypeOccurrence);

		Forecast forecast = new Forecast();
		if(listForecast!=null && listForecast.size()>0)
		{
			forecast = listForecast.get(0);
		}else
		{
			forecast.setForecastTypeOccurrence(forecastTypeOccurrence);
			forecast.setMatch(match);
			forecast.setSource(source);
		}
		forecast.setForecastValue(forecastValue);
		forecastRepository.save(forecast);
		
	}
	
	

	
	
	
}
