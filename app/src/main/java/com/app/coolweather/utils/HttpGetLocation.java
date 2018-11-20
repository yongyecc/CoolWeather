package com.app.coolweather.utils;


import android.content.Intent;
import android.util.Log;

import com.app.coolweather.Interface.HttpCallBack;
import com.app.coolweather.Interface.getPlacenameAndWeatherInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

/*
 由经纬度解析地名，再经过xml文件过滤得出实际地名对应的行政号，再访问API接口获取数据
 */


public class HttpGetLocation {
    private static String website = "http://api.map.baidu.com/geocoder?callback=renderReverse&output=json&pois=1&location=";
    public static HttpResponse response;
    private static JSONObject jsonObject;
    private static JSONObject jsonObject1;
    private static JSONObject jsonObject2;
    private static String districtName;
    private static String cityName ;
    private static String placeCode;
    private static String PlaceName;


    public static void getLocation(final String LatitudeAndLongitude, final getPlacenameAndWeatherInfo getPlacenameAndWeatherInfo){
        districtName = "";
        cityName = "";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    placeCode = "";
                    districtName = "";
                    cityName = "";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet getRequest = new HttpGet(website+LatitudeAndLongitude);
                    response = httpClient.execute(getRequest);
                    if(response.getStatusLine().getStatusCode() == 200){
                        String jsondata = EntityUtils.toString(response.getEntity());
                        jsonObject = new JSONObject(jsondata);
                        jsonObject1 = jsonObject.getJSONObject("result");
                        jsonObject2 = jsonObject1.getJSONObject("addressComponent");
                        districtName = jsonObject2.getString("district");
                        cityName = jsonObject2.getString("city");
                        PlaceName = districtName;
                        if (districtName == ""){
                            PlaceName = cityName;
                        }
                        placeCode = infilterName.infilterName(districtName);
                        if (placeCode == null){
                            placeCode = infilterName.infilterName(cityName);
                        }
                        getPlacenameAndWeatherInfo.getinfo(placeCode, PlaceName);
                        Log.e("------------","-----------------");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
