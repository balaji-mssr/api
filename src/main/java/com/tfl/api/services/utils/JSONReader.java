package com.tfl.api.services.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Balaji Ravivarman
 * Date: 05/09/13
 * Time: 16:51
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONReader {

    public static String getJSON(String URL){
        //http://dev01.dev.beta.tfl.gov.uk:81/RoadPlannedWorks?corridorIds=&severities=&swLat=&swLon=&neLat=&neLon=&startDate=2013-09-07T00:00&endDate=2013-09-08T00:00
        return getJSON(URL,10000);
    }

    private static String getJSON(String url, int timeout) {
        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            //Logger.getLogger(DebugServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(DebugServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
