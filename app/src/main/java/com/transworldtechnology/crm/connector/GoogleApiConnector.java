package com.transworldtechnology.crm.connector;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.repository.MeRepoImplUserTracker;
import com.transworldtechnology.crm.database.repository.MeRepoUserTracker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by root on 13/5/16.
 */
public class GoogleApiConnector
{

    private GoogleApiClient googleApiClient;
    private Location location;

    public synchronized void buildGoogleApiClient(final Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        saveDataToDB(context, location);
                        //getLongitude(location);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (!googleApiClient.isConnected())
                            googleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        if (!googleApiClient.isConnected())
                            googleApiClient.connect();
                    }
                })
                .addApi(LocationServices.API)
                .build();
    }

    public final void connectGoogleApi() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }


    public final void disconnectGoogleApi() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private void saveDataToDB(Context context, Location location) {
        MeHelper helper = new MeHelper(context, MeDatabase.DB_NAME, null, MeDatabase.DB_VERSION);
        MeRepoUserTracker userTracker = new MeRepoImplUserTracker(helper);
        try {
            userTracker.saveUserCrediantials(location.getLatitude() >= 0 ? location.getLatitude() : -1, location.getLongitude() >= 0 ? location.getLongitude() : -1, getEnabledLocationProvider(context), "" + Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis(), location.getSpeed(), location.getAltitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        helper = null;
    }

    public final Double getLatitude() {
        return (location != null ? location.getLatitude() : -1);
    }

    public final Double getLongitude() {
        return (location != null ? location.getLongitude() : -1);
    }



    public final String getEnabledLocationProvider(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
    }
}
