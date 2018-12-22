package com.transworldtechnology.crm.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by root on 19/2/16.
 */
public class MeNetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            Log.i("@Transworld", "Network Available");
            //   context.startService(new Intent(context, LocationService.class));


        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable(Context context) {
        Boolean isWiFi = false;
        Boolean isMobile = false;
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager != null && activeNetworkInfo != null) {
            isWiFi = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            isMobile = activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return isMobile || isWiFi;
    }
}
