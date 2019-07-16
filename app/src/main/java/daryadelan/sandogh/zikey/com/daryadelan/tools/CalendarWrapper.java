package daryadelan.sandogh.zikey.com.daryadelan.tools;

import android.os.SystemClock;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zikey on 04/07/2017.
 */

public class CalendarWrapper {


    public static String getCurrentPersianDate() {

        PersianCalendar calendar = new PersianCalendar();
        int year = calendar.getPersianYear();
        int monthOfYear = calendar.getPersianMonth();
        int dayOfMonth = calendar.getPersianDay();

        return  PersianDateConverter.toPersianFormat(year,monthOfYear,dayOfMonth);

    }

    public static String getFirstDayOfThisMonth() {

        PersianCalendar calendar = new PersianCalendar();
        int year = calendar.getPersianYear();
        int monthOfYear = calendar.getPersianMonth();

        String month;
        if (monthOfYear < 9) month = "0" + (monthOfYear + 1);
        else month = "" + (monthOfYear + 1);

        return (year + "/" + month + "/" + "01");
    }

    public static String getCurrentTime() {

        try {
            Calendar c = Calendar.getInstance();
            int minutes = c.get(Calendar.MINUTE);
            int hour = c.get(Calendar.HOUR_OF_DAY);

            String min = String.valueOf(minutes);
            String h = String.valueOf(hour);

            if (minutes < 10) {
                min = "0" + min;
            }

            if (hour < 10) {
                h = "0" + h;
            }
            return h + ":" + min;
        } catch (Exception e) {
            return "";
        }

    }


    public static long getCurrentLongTime() {

        try {
            Calendar c = Calendar.getInstance();
            long time = c.getTimeInMillis();
            return time;
        } catch (Exception e) {
            return 0;
        }


    }

    public static long getTimeFromBoot() {

        return SystemClock.elapsedRealtime();

    }

    public static long millisecondsToMinutes(long miliSecounds) {

        return TimeUnit.MILLISECONDS.toMinutes(miliSecounds);
    }



}
