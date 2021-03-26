package com.example.testsimple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constans {
    public static String URL = "https://api.darksky.net/forecast/";
    public static String API_KEY = "2bb07c3bece89caf533ac9a5d23d8417";

    public static String apiRequest(String lat,String lng){
        StringBuilder sb = new StringBuilder(URL);
        sb.append(String.format("?lat-%s&lon-%s&appid",API_KEY,lat,lng));
        return sb.toString();
    }
    public static String unitTimeToDate(double unitTime){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long)unitTime*1000);
        return dateFormat.format(date);
    }
}
