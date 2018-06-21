package com.example.cub11.ccpltimesheet;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by cub05 on 6/21/2018.
 */

public class Utils {

    public static String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("hh:mm:ss a");
        String localTime = date.format(currentTime);
        return localTime;
    }

    public static String makeTimeDiff(long timeInMillis) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis)));
    }



    public static String getFinalDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat date = new SimpleDateFormat("hh:mm:ss");
        String localTime = date.format(currentTime);


        long milliSeconds = currentTime.getTime();


        DateFormat yearOnly = new SimpleDateFormat("yyyy");
        String yearValue = yearOnly.format(currentTime);
        int dateValue = currentTime.getDate();

        int monthIndex = currentTime.getMonth();
        String monthName = "";
        switch (monthIndex) {
            case 0:
                monthName = "Jan";
                break;
            case 1:
                monthName = "Feb";
                break;
            case 2:
                monthName = "Mar";
                break;
            case 3:
                monthName = "Apr";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "Jun";
                break;
            case 6:
                monthName = "Jul";
                break;
            case 7:
                monthName = "Aug";
                break;
            case 8:
                monthName = "Sep";
                break;
            case 9:
                monthName = "Oct";
                break;
            case 10:
                monthName = "Nov";
                break;
            case 11:
                monthName = "Dec";
                break;

        }


        int dayIndex = currentTime.getDay();
        String dayName = "";
        switch (dayIndex) {
            case 0:
                dayName = "Sunday";
                break;
            case 1:
                dayName = "Monday";
                break;
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            case 4:
                dayName = "Thursday";
                break;
            case 5:
                dayName = "Friday";
                break;
            case 6:
                dayName = "Saturday";
                break;
        }

        String finalDate = monthName + " " + dateValue + " " + yearValue + " , " + dayName;
        Log.e("raman", "finalDate " + finalDate + " and local time is :" + localTime + " and time in milliseconds is : " + milliSeconds);


        return finalDate;
    }

    public static String findAmorPm(int hour, int minute) {
        String timeSet = "";
        if (hour > 12) {
            timeSet = "PM";
        } else if (hour == 0) {
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }
        return timeSet;
    }

    public static String makeTime(int hour, int minutes) {
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        return new StringBuilder().append(hour).append(':')
                .append(min).append(":00").append(" ").append(timeSet).toString();
    }


}
