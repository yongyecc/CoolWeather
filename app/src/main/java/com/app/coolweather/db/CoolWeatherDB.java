package com.app.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.coolweather.model.city;
import com.app.coolweather.model.county;
import com.app.coolweather.model.province;

import java.util.ArrayList;
import java.util.List;

public class CoolWeatherDB {
    /*
     * 封装常用的数据库操作方法，实现数据库数据的读取和写入
     */
    //数据库名称
    public static final String DBname = "coolweatherdb";
    //版本号
    public static final int version = 1;
    public static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase sqLiteDatabase;
    //单例化该类：防止该类被多次实例化
    private CoolWeatherDB(Context context){
        //实例化一个数据库帮助类，用来帮助创建数据库
        SQLiteOpenHelper sqLiteOpenHelper = new databaseHelper(context, DBname, null, version);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    //定义一个静态方法来获取这个封装类对象
    public synchronized static CoolWeatherDB getdainstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }
    /*
     * 从模型类对象中获取数据，存入数据库
     */
    public void insertProvince(province prov){
        if(prov != null){
            String provincename = prov.getProvince_name();
            int provincecode = prov.getProvince_code();
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name", provincename);
            contentValues.put("province_code", provincecode);
            sqLiteDatabase.insert("province", null, contentValues);
        }
    }
    public void insertCity(city cit){
        if(cit != null){
            String cityname = cit.getCity_name();
            int citycode = cit.getCity_code();
            int provincecode = cit.getProvince_code();
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name", cityname);
            contentValues.put("city_code", citycode);
            contentValues.put("province_code", provincecode);
            sqLiteDatabase.insert("city", null, contentValues);
        }
    }
    public void insertcounty(county cnt){
        if(cnt != null){
            String countyname = cnt.getCounty_name();
            int citycode = cnt.getCity_code();
            ContentValues contentValues = new ContentValues();
            contentValues.put("county_name", countyname);
            contentValues.put("city_code", citycode);
            sqLiteDatabase.insert("county", null, contentValues);
        }
    }
    /*
     * 获取表内全部数据，用来展示
     */
    public List<province> getAllProvince(){
        List<province> list = new ArrayList<province>();
        Cursor cursor = sqLiteDatabase.query("province", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do {
                province pro = new province();
                pro.setId(cursor.getInt(cursor.getColumnIndex("id")));
                pro.setProvince_name(cursor.getString(cursor.getColumnIndex("province_name")));
                pro.setProvince_code(cursor.getInt(cursor.getColumnIndex("province_code")));
                list.add(pro);
            }while(cursor.moveToNext());
        }
        return list;
    }
    public List<city> getAllCity(int provincecode){
        List<city> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("city", null, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                int code = cursor.getInt(cursor.getColumnIndex("province_code"));
                if (provincecode == code) {
                    city cit = new city();
                    cit.setCity_code(cursor.getInt(cursor.getColumnIndex("city_code")));
                    cit.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                    cit.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    cit.setProvince_code(cursor.getInt(cursor.getColumnIndex("province_code")));
                    list.add(cit);
                }
            }while (cursor.moveToNext());
        }
        return list;
    }
    public List<county> getAllCounty(int citycode){
        List<county> list = new ArrayList<county>();
        Cursor cursor = sqLiteDatabase.query("county", null, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do {
                int code = cursor.getInt(cursor.getColumnIndex("city_code"));
                if (citycode == code) {
                    county cnt = new county();
                    cnt.setCity_code(cursor.getInt(cursor.getColumnIndex("city_code")));
                    cnt.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    cnt.setCounty_name(cursor.getString(cursor.getColumnIndex("county_name")));
                    list.add(cnt);
                }
            }while (cursor.moveToNext());

        }
        return list;
    }
}
