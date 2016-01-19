package com.catenoid.dashbd.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	
	private static final String ISO8601_FORMAT_PATERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final SimpleDateFormat ISO8601_FORMAT = new SimpleDateFormat(ISO8601_FORMAT_PATERN);
	
	public static String parseISO8601(Date d) {	
		return ISO8601_FORMAT.format(d);
	}
	
	public static Date convertISO8601ToDate(String str) {
		return null;
	}
	
    public static String getFileDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(System.currentTimeMillis());
    }
    
	public static String getFormatDateTime(Date date, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new SimpleDateFormat(format).format(cal.getTime());
	}
}
