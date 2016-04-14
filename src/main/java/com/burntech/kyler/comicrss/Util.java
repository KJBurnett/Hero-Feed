package com.burntech.kyler.comicrss;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Kyler J. Burnett on 4/10/2015.
 */
public class Util {

    //--------------------------------------------------
    // Date & Time Utilities
    //--------------------------------------------------

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public static String getMostRecentSunday() {
        Calendar cal=Calendar.getInstance();
        cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-1));

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int year = cal.get(Calendar.YEAR);

        String lastSunday = String.format("%02d", month)+"/"+String.format("%02d", day)+"/"+year;
        return lastSunday;
    }

    public static String getSecondToLastSunday() {
        Calendar cal=Calendar.getInstance();
        cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-1)-7);

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int year = cal.get(Calendar.YEAR);

        String sunday = String.format("%02d", month)+"/"+String.format("%02d", day)+"/"+year;
        return sunday;
    }

    //--------------------------------------------------
    // Internet
    //--------------------------------------------------

    public static boolean hasInternetAccess() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
