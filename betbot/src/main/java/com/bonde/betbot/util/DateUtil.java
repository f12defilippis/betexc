package com.bonde.betbot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	
	public static Date addHourToDateandAddHours(Date date, String hour, int howManyHours)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = format.format(date);
		
		String datehour = strDate + " " + hour;
		
		SimpleDateFormat formatDateHour = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date ret;
		Calendar cal = Calendar.getInstance(); // creates calendar
		try {
			ret = formatDateHour.parse(datehour);
		    cal.setTime(ret); // sets calendar time/date
		    cal.add(Calendar.HOUR_OF_DAY, howManyHours); // adds one hour
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return cal.getTime(); // returns new date object, one hour in the future		

	}	
	
	
	public static Date addDaysToDate(Date date, int howManydays)
	{
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(date); // sets calendar time/date
	    cal.add(Calendar.DATE, howManydays); // adds one hour
		return cal.getTime(); // returns new date object, one hour in the future		

	}		
	
	
	
	
}
