package com.app.coolweather.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/*
    1. 利用该异步线程实现短暂的刷新界面
 */

public class AsynRefreshWeather extends AsyncTask<String, Integer, Boolean> {
    private Context context = null;
    private  ProgressDialog progressDialog;
    public AsynRefreshWeather(Context choose_area) {
        context = choose_area;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressDialog.cancel();
        return true;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("刷新当前天气");
        progressDialog.show();
        Log.e("async", context.toString());
    }

}
