package com.app.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {
    /*
     * 省地址数据
     */
    public static final String Create_Province = "create table province ("
            + "id integer primary key autoincrement,"
            + "province_name,"
            + "province_code)";
    /*
     * 市地址数据
     */
    public static final String Create_City = "create table city("
            + "id integer primary key autoincrement,"
            + "city_name,"
            + "city_code,"
            + "province_code)";
    /*
     * 县地址数据
     */
    public static final String Create_County = "create table county("
            + "id integer primary key autoincrement,"
            + "county_name,"
            + "city_code)";

    public databaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库表
        sqLiteDatabase.execSQL(Create_Province);
        sqLiteDatabase.execSQL(Create_City);
        sqLiteDatabase.execSQL(Create_County);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
