package com.app.coolweather.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.ListView;

import com.app.coolweather.db.CoolWeatherDB;
import com.app.coolweather.model.city;
import com.app.coolweather.model.county;
import com.app.coolweather.model.province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class parseAreaJSON {
    /*
     * 先从数据库中读取数据，如果没有再从json文件中写入数据到数据库中
     */
    private static byte[] buffer;
    public static List<String> provincelist = new ArrayList<String>();
    public static List<String> citylist = new ArrayList<String>();
    public static List<String> countylist = new ArrayList<String>();
    private static Context context = globalContext.getContext();
    public static List<province> provinces;
    public static List<city>cities;
    public static List<county> counties;
    public static String jsondata = "";
    private static CoolWeatherDB coolWeatherDB = CoolWeatherDB.getdainstance(context);


    public static Object parseJSON(String flag, int code) {
        //获取资产文件，来解析城市信息
        AssetManager assetManager = globalContext.getContext().getAssets();
        try {
            InputStream inputStream = assetManager.open("pca.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsondata += line;
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        provinces = coolWeatherDB.getAllProvince();
        cities = coolWeatherDB.getAllCity(code);
        counties = coolWeatherDB.getAllCounty(code);
        //如果数据库中没有数据，从json文件中读取
        if (provinces.size() == 0) {
            try {
                JSONObject provinceJsonObj = new JSONObject(jsondata);
                Iterator<String> iterable = provinceJsonObj.keys();
                while (iterable.hasNext()) {
                    province pr = new province();
                    //写入省名称和代码
                    String provincename = iterable.next();
                    int provincecode = provincename.hashCode();
                    pr.setProvince_name(provincename);
                    pr.setProvince_code(provincecode);
                    coolWeatherDB.insertProvince(pr);
                    //写入市数据
                    JSONObject cityJsonObj = (JSONObject) provinceJsonObj.get(provincename);
                    Iterator<String> cityiterable = cityJsonObj.keys();
                    String cityname;
                    while (cityiterable.hasNext()) {
                        if ((cityname = cityiterable.next()) == null) {
                            break;
                        }
                        city ct = new city();
                        ct.setProvince_code(provincecode);
                        ct.setCity_name(cityname);
                        ct.setCity_code(cityname.hashCode());
                        coolWeatherDB.insertCity(ct);
                        //写入县数据
                        JSONArray countyJsonArr = (JSONArray) cityJsonObj.get(cityname);
                        for (int i = 0; i < countyJsonArr.length(); i++) {
                            county cnt = new county();
                            cnt.setCounty_name((String) countyJsonArr.get(i));
                            cnt.setCity_code(cityname.hashCode());
                            coolWeatherDB.insertcounty(cnt);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /*
         * 根据flag字段，判断需要获取的的哪些数据
         */
        switch (flag) {
            case "province":
                return provinces;
            case "city":
                return cities;
            case "county":
                return counties;
        }
        return null;
    }
}
