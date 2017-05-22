package com.bonde.betbot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {

	public static Map<Integer,String> monthMap = new HashMap<Integer,String>();
	static
	{
		monthMap.put(1, "January");
		monthMap.put(2, "February");
		monthMap.put(3, "March");
		monthMap.put(4, "April");
		monthMap.put(5, "May");
		monthMap.put(6, "June");
		monthMap.put(7, "July");
		monthMap.put(8, "August");
		monthMap.put(9, "September");
		monthMap.put(10, "October");
		monthMap.put(11, "November");
		monthMap.put(12, "December");
	};
		
	public static String getMonth(Integer month)
	{
		return monthMap.get(month);
	}
	
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
