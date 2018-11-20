package com.app.coolweather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SaveData2Local {
    private static Context context = globalContext.getContext();


    public static void saveWeatherData2Local(String PlaceName, String updataTime,String temperature,
                                             String weatherType){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("placename", PlaceName);
        editor.putString("updatetime", updataTime);
        editor.putString("temperature", temperature);
        editor.putString("weathertype", weatherType);
        editor.commit();

    }
}
