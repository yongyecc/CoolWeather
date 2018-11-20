package com.app.coolweather.utils;

import com.app.coolweather.Interface.HttpCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getWeatherUtils {
    private static String responseData;
    private static HttpURLConnection httpURLConnection;
    private static InputStream inputStream;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;

    public static void getWeatherInfo(final String placeCode, final HttpCallBack httpCallBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    responseData = "";
                    URL url = new URL("http://t.weather.sojson.com/api/weather/city/" + placeCode);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setConnectTimeout(5000);
                    inputStream = httpURLConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line=bufferedReader.readLine()) != null){
                        responseData += line;
                    }
                    httpURLConnection.disconnect();
                    inputStream.close();
                    bufferedReader.close();
                    inputStreamReader.close();
                    if (httpCallBack != null){
                        httpCallBack.finished(responseData);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
