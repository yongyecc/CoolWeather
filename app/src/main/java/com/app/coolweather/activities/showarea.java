package com.app.coolweather.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.KeyEventDispatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.coolweather.Interface.HttpCallBack;
import com.app.coolweather.R;
import com.app.coolweather.db.CoolWeatherDB;
import com.app.coolweather.model.city;
import com.app.coolweather.model.county;
import com.app.coolweather.model.province;
import com.app.coolweather.model.provinceAdapter;
import com.app.coolweather.utils.getWeatherUtils;
import com.app.coolweather.utils.infilterName;
import com.app.coolweather.utils.parseAreaJSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class showarea extends Activity {
    private List<province> provinces;
    private List<city> cities = new ArrayList<city>();
    private List<county> counties;
    private Adapter proAdapter;
    private Adapter cityAdapter;
    private Adapter countyAdapter;
    public ListView listView;
    private TextView textView;
    private int LEVEL_PROVINCE = 0;
    private int LEVEL_CITY = 1;
    private int LEVEL_COUNTY = 2;
    private int Level;
    private int cityid;
    private int countyid;
    private province sigleprovince;
    private String sigleCityName;
    private String sigleProvinceName;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.province_layout);
        listView = (ListView)findViewById(R.id.province);
        textView = (TextView)findViewById(R.id.title);
        provinces = new ArrayList<province>();
        /*
         * 这里留下一个问题：如果provinces为空，在setAdapter方法之后在填充内容，需要使用add而不能直接
         * 使用下面这个语句直接赋值所有的内容
         */
        if(provinces != null){
            queryprovince();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (Level == LEVEL_CITY){
                        cityid = i;
                        querycity(cityid);
                    }else if (Level == LEVEL_COUNTY){
                        countyid = i;
                        querycounty(countyid);
                    }else if(Level == LEVEL_PROVINCE){
                        county cnt = counties.get(i);
                        cnt.getCity_code();
                        final String PlaceName = cnt.getCounty_name();
                        String placeCode = infilterName.infilterName(PlaceName);
                        if (placeCode == null){
                            if (sigleCityName.equals("市辖区")) {
                                placeCode = infilterName.infilterName(sigleProvinceName);
                            }else {
                                placeCode = infilterName.infilterName(sigleCityName);
                            }
                        }
                        getWeatherUtils.getWeatherInfo(placeCode, new HttpCallBack() {
                            @Override
                            public void finished(String response) {
                                Intent intent = new Intent("com.app.showWeatherInfo");
                                intent.putExtra("jsondata", response);
                                intent.putExtra("placeName", PlaceName);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });

        }else {
            throw new NullPointerException();
        }
    }
    /*
     * 获取所有省份并展示
     */
    private void queryprovince(){
        textView.setText("中国");
        provinces = (List<province>) parseAreaJSON.parseJSON("province", 0);
        List<province> pros = (List<province>) parseAreaJSON.parseJSON("province", 0);
        /*
         * [可选] 使用系统内部默认的子视图布局文件
         * provinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datalist);
         */
        /*
         * 使用自定义的适配器
         */
        proAdapter = new provinceAdapter(this, R.layout.son_province, provinces);
        listView.setAdapter((ListAdapter) proAdapter);
        Level = LEVEL_CITY;
    }
    /*
     * 显示市数据
     */
    private void querycity(int i){
        sigleProvinceName = provinces.get(i).getProvince_name();
        textView.setText(sigleProvinceName);
        sigleprovince = provinces.get(i);
        int provincecode = sigleprovince.getProvince_code();
        cities = (List<city>) parseAreaJSON.parseJSON("city", provincecode);
        List<String> citynames = new ArrayList<String>();
        for(city ct:cities){
            citynames.add(ct.getCity_name());
        }
        cityAdapter = new ArrayAdapter<String>(showarea.this, android.R.layout.simple_list_item_1, citynames);
        listView.setAdapter((ListAdapter) cityAdapter);
        Level = LEVEL_COUNTY;
    }
    /*
     * 显示县数据
     */
    private void querycounty(int i){
        sigleCityName = cities.get(i).getCity_name();
        textView.setText(sigleCityName);
        city siglecounty = cities.get(i);
        int citycode = siglecounty.getCity_code();
        counties = (List<county>) parseAreaJSON.parseJSON("county", citycode);
        List<String> countynames = new ArrayList<String>();
        for (county cnt:counties){
            countynames.add(cnt.getCounty_name());
        }
        countyAdapter = new ArrayAdapter<String>(showarea.this, android.R.layout.simple_list_item_1, countynames);
        listView.setAdapter((ListAdapter) countyAdapter);
        Level = LEVEL_PROVINCE;
    }

    @Override
    public void onBackPressed() {
        if(Level == LEVEL_CITY){
            super.onBackPressed();
//            ComponentName componentName = new ComponentName("com.app.coolweather.activities", "choose_area");
//            Intent intent = new Intent("coolweather.mainactivity");
//
////            intent.setComponent(componentName);
//            startActivity(intent);
        }else if(Level == LEVEL_COUNTY){
            queryprovince();
        }else if(Level == LEVEL_PROVINCE){
            querycity(cityid);
        };
    }
}
