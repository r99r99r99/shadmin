package com.sdocean.frame.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {

	public static String fomatDate(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
	
	public static Date toDate(String str, String format) {
		if (StringUtils.isNotBlank(str)) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				Date day = sdf.parse(str);
				return day;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

}
