package com.bonde.betbot.service.datacollection;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.ScanSummary;
import com.bonde.betbot.model.domain.ScanType;
import com.bonde.betbot.model.domain.Source;
import com.bonde.betbot.repository.ScanSummaryRepository;

@Service
public class SummaryService {

	
	@Autowired
	ScanSummaryRepository scanSummaryRepository;
	
	
	public void saveSummary(Source source, ScanType scanType, Date start, Date end, int recordWorked, int totalRecord, Date date)
	{
		
		ScanSummary ss = new ScanSummary();
		ss.setDateEnd(end);
		ss.setDateStart(start);
		ss.setDate(date);
		ss.setRecordWorked(recordWorked);
		ss.setScanType(scanType);
		ss.setSource(source);
		ss.setTotalRecord(totalRecord);
		
		scanSummaryRepository.save(ss);
		
	}
	
	
	
}
