package com.bonde.betbot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date dateManagement(Date date, String hour)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = format.format(date);
		
		String datehour = strDate + " " + hour;
		
		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date ret;
		try {
			ret = formatDateHour.parse(datehour);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}		
	
	
	
}
