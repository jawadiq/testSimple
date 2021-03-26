package com.example.testsimple;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Apis {
    static String stream = null;
    private Context mContext;

    public Apis() {
    }
    public String getHttpData(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
           if (httpURLConnection.getResponseCode()==200){
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
               StringBuilder stringBuilder = new StringBuilder();
               String line;
               while ((line = bufferedReader.readLine())!=null)
                   stringBuilder.append(line);
               stream = stringBuilder.toString();
               httpURLConnection.disconnect();

           }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
