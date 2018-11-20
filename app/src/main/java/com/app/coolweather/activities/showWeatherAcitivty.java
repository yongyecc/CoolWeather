package com.app.coolweather.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import com.app.coolweather.R;
import com.app.coolweather.utils.SaveData2Local;

import org.json.JSONArray;
import org.json.JSONObject;

public class showWeatherAcitivty extends Activity {
    private String responsedata;
    private TextView PlaceName;
    private TextView updateTime;
    private TextView WeatherType;
    private TextView temperature;
    private JSONObject jsonObject;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showweatherinfo_layout);
        updateTime = (TextView)findViewById(R.id.updateTime);
        PlaceName = (TextView)findViewById(R.id.placename);
        WeatherType = findViewById(R.id.weatherTypt);
        temperature = findViewById(R.id.temperature);
        try {
            responsedata = getIntent().getStringExtra("jsondata");
            String placeName = getIntent().getStringExtra("placeName");
            PlaceName.setText(placeName);
            try {
                jsonObject = new JSONObject(responsedata.substring(4));
            }catch (Exception e){
                jsonObject = new JSONObject(responsedata);
            }
            JSONObject JsonCityInfo = jsonObject.getJSONObject("cityInfo");
            String updatatime = JsonCityInfo.getString("updateTime");
            updateTime.setText("今天 " + updatatime + " 发布");
            /*
             * 未来几天的天气信息，包括今天
             */
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            temperature.setText(jsonObject1.getString("wendu"));
            JSONArray JsonForecast = jsonObject1.getJSONArray("forecast");
            JSONObject JsonToday = (JSONObject) JsonForecast.get(0);
            WeatherType.setText(JsonToday.getString("type"));
            Log.e("end,",jsonObject.getString("time"));

            //写入本地ShareRefresh文件中
            SaveData2Local.saveWeatherData2Local(placeName, updatatime, jsonObject1.getString("wendu"), JsonToday.getString("type"));
        }catch (Exception e){
            throw new NullPointerException();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
