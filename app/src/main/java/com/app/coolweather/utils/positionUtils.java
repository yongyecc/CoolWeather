package com.app.coolweather.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class positionUtils {
    private static Context context = globalContext.getContext();
    private static Double latitude;
    private static Double longitude;

    public static Double getLatitude() {
        return latitude;
    }

    public static Double getLongitude() {
        return longitude;
    }

    public static void getCurrentLocation() {
        latitude = 0.0;
        longitude = 0.0;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        while (location == null){
            locationManager.requestLocationUpdates(locationProvider, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {
                    Toast.makeText(context, "开始监听本地位置,获取天气信息", Toast.LENGTH_SHORT);
                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}

