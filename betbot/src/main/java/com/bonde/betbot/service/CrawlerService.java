package com.bonde.betbot.service;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class CrawlerService {

	protected Document executeCall(String requestString) throws Exception
	{
		Response doc = Jsoup
				.connect(requestString)
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
				.header("Accept-Encoding", "gzip, deflate")
				.header("Connection", "keep-alive")
				.header("cache-control", "no-cache")
				.header("pragma", "no-cache")
				.timeout(60000).execute();
		
		Document document = Jsoup.parse(new String(doc.bodyAsBytes()));
		
		return document;
	}	
	
	
	
	
}
