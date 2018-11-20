package com.app.coolweather.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.app.coolweather.Interface.HttpCallBack;
import com.app.coolweather.Interface.getPlacenameAndWeatherInfo;
import com.app.coolweather.R;
import com.app.coolweather.utils.AsynRefreshWeather;
import com.app.coolweather.utils.HttpGetLocation;
import com.app.coolweather.utils.SaveData2Local;
import com.app.coolweather.utils.getWeatherUtils;
import com.app.coolweather.utils.positionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class choose_area extends Activity {
    public Button buttonPosition;
    private Button RefreshButton;
    private Double latitude;
    private Double longitude;
    private String placeName;
    private String PlacelCode;
    private TextView title;
    private TextView updateTime;
    private TextView WeatherType;
    private TextView temperature;
    private JSONObject jsonObject;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{
                    title = findViewById(R.id.current_place_name);
                    title.setText((String) msg.obj);
                }
                case 2:{
                    String response = (String)msg.obj;
                    updateTime = findViewById(R.id.updateTime);
                    WeatherType = findViewById(R.id.weatherTypt);
                    temperature = findViewById(R.id.temperature);
                    try {
                        if (response.contains("{")) {
                            if (response.contains("null")) {
                                jsonObject = new JSONObject(response.substring(4));
                            } else {
                                jsonObject = new JSONObject(response);
                            }
                            JSONObject JsonCityInfo = null;
                            JsonCityInfo = jsonObject.getJSONObject("cityInfo");
                            String updatatime = null;
                            updatatime = JsonCityInfo.getString("updateTime");
                            updateTime.setText("今天 " + updatatime + " 发布");
                            /*
                             * 未来几天的天气信息，包括今天
                             */
                            JSONObject jsonObject1 = null;
                            jsonObject1 = jsonObject.getJSONObject("data");
                            temperature.setText(jsonObject1.getString("wendu"));
                            JSONArray JsonForecast = jsonObject1.getJSONArray("forecast");
                            JSONObject JsonToday = (JSONObject) JsonForecast.get(0);
                            WeatherType.setText(JsonToday.getString("type"));
                            Log.e("end,", jsonObject.getString("time"));
                            //写入本地ShareRefresh文件中
                            SaveData2Local.saveWeatherData2Local(placeName, updatatime, jsonObject1.getString("wendu"), JsonToday.getString("type"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        /*
        获取当前位置，http请求天气数据并展示
         */
        positionUtils.getCurrentLocation();
        latitude = Math.abs(positionUtils.getLatitude());
        longitude = Math.abs(positionUtils.getLongitude());
        /*
        这里将经纬度全部取正，因为这个API接口好像查不到负值的地点
         */

        String latitudeAndLongitude = String.valueOf(latitude) + "," + String.valueOf(longitude);
        HttpGetLocation.getLocation(latitudeAndLongitude, new getPlacenameAndWeatherInfo() {
            @Override
            public void getinfo(String placeCode, String PlaceName) {
                placeName = PlaceName;
                PlacelCode = placeCode;
                final Message message = new Message();
                message.what = 1;
                message.obj = placeName;
                handler.sendMessage(message);
                getWeatherUtils.getWeatherInfo(placeCode, new HttpCallBack() {
                    @Override
                    public void finished(String response) {
                        Message message1 = new Message();
                        message1.what = 2;
                        message1.obj = response;
                        handler.sendMessage(message1);
                    }
                });
            }
        });

        buttonPosition = (Button) findViewById(R.id.buttonPosition);
        buttonPosition.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.app.showArea");
                startActivity(intent);
            }
        });
        RefreshButton = findViewById(R.id.refresh);
        RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsynRefreshWeather(choose_area.this).execute();
                //根据当前位置，获取天气数据，利用消息传递天气信息
                if(placeName!=null && PlacelCode!= null){
                    getWeatherUtils.getWeatherInfo(PlacelCode, new HttpCallBack() {
                        @Override
                        public void finished(String response) {
                            Message message1 = new Message();
                            message1.what = 2;
                            message1.obj = response;
                            handler.sendMessage(message1);
                        }
                    });
                }
            }
        });
    }
}
