package com.tfl.api.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static final DateFormat SDF_YYYYMMDD_HYPEN = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static String getWeekendDate(int dayOfWeek) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return convertDateToString(SDF_YYYYMMDD_HYPEN, c.getTime());
	}

    public static Date getSaturday(int dayOfWeek) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return c.getTime();
    }

    public static String getEndDateForTheWeekend(int dayOfWeek) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return getFormattedEndDateToString(SDF_YYYYMMDD_HYPEN, c.getTime());
    }

	public static String convertDateToString(DateFormat dateFormat, Date date) {
		return dateFormat.format(date) + "T00:00:00";
	}

    public static String getFormattedEndDateToString(DateFormat dateFormat, Date date) {
		return dateFormat.format(date) + "T23:59:59";
	}
	
	public static String getDateString(DateFormat dateFormat, Date date) {
		return dateFormat.format(date);
	}

}
