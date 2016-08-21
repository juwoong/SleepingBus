package kr.tamiflus.sleepingbus.utils;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import kr.tamiflus.sleepingbus.HomeActivity;

/**
 * Created by tamiflus on 2016. 8. 3..
 */
public class LocManager extends Service implements LocationListener {
    Context context, dialogContext;
    LocationManager manager;
    double longitude, latitude;
    Location location;
    private Handler handler;
    private float accuracy;


    public LocManager(Context context, Context dialogContext, Handler handler) {
        this.context = context;
        this.dialogContext = dialogContext;
        this.handler = handler;
        manager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        location = null;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            accuracy = location.getAccuracy();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void checkIsGPSOn() {
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setMessage("해당 기능을 이용하려면 GPS가 필요합니다")
                .setCancelable(false)
                .setTitle("GPS..")
                .setPositiveButton("", new DialogInterface.OnClickListener() {

                    //  폰 위치 설정 페이지로 넘어감
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void loadLocation() {
        boolean gpsEnabled = manager.isProviderEnabled(manager.GPS_PROVIDER);
        boolean networkEnabled = manager.isProviderEnabled(manager.NETWORK_PROVIDER);
        Log.d("LatLng", Boolean.toString(networkEnabled));
        Log.d("LatLng", Boolean.toString(gpsEnabled));
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("LatLng", "PERMISSION PROBLEM");
                return;
            }

            if (gpsEnabled) {
                doWhenGpsEnabled();
                if ((longitude == 0 && latitude == 0) && networkEnabled) {
                    doWhenNetworkEnalbed();
                }
//                if (location == null) {
//                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                            1000,
//                            10, locationListener);
//                    Log.d("LatLng", "Call Location");
//
//                    try { Thread.sleep(3000); } catch(Exception e) { }
//
//                    if (manager != null) {
//                        location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        Log.d("LatLng", "Get Location");
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            Log.d("LatLng", "Lat : " + Double.toString(latitude) + ", Lng : " + Double.toString(longitude));
//                        }
//                    }
//                }
            } else if (networkEnabled) {
                doWhenNetworkEnalbed();
//                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListener);
//
//                try { Thread.sleep(3000); } catch(Exception e) { }
//                if (manager != null) {
//                    location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    if (location != null) {
//                        // 위도 경도 저장
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d("LocManager", "onBind()");
        return null;
    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        Message msg = new Message();
        msg.what = HomeActivity.LOCATION_CHANGED;
        handler.sendMessage(msg);
        Log.d("onLocationChanged()", "Lat : " + Double.toString(latitude) + ", Lng : " + Double.toString(longitude));
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    public void stop() {
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
        manager.removeUpdates(this);
    }

    private void doWhenGpsEnabled() {
        Log.d("LocManager", "doWhenGpsEnabled() called");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("LatLng", "PERMISSION PROBLEM");
            return;
        }
        try {
            Toast.makeText(context, "requestLocationUpdates(GPS)", Toast.LENGTH_SHORT).show();
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    10, locationListener);
            Log.d("LatLng", "Call Location");

            Thread.sleep(3000);

            if (manager != null) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("LatLng", "Get Location");
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("LatLng", "Lat : " + Double.toString(latitude) + ", Lng : " + Double.toString(longitude));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doWhenNetworkEnalbed() {
        Log.d("LocManager", "doWhenNetworkEnabled() called");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("LatLng", "PERMISSION PROBLEM");
            return;
        }
        Toast.makeText(context, "requestLocationUpdates(NETWORK)", Toast.LENGTH_SHORT).show();

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60 * 1000, 10, locationListener);

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        if (manager != null) {
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                // 위도 경도 저장
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

}
