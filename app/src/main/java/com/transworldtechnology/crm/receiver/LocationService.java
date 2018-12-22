package com.transworldtechnology.crm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.transworldtechnology.crm.connector.GoogleApiConnector;

import java.util.Date;

/**
 * Created by root on 15/4/16.
 */
public class LocationService extends BroadcastReceiver {
    private GoogleApiClient googleApiClient;
    private Location location;
    private GoogleApiConnector googleApiConnector;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("@", "Running" + new Date().toString());
        googleApiConnector = new GoogleApiConnector();
        googleApiConnector.buildGoogleApiClient(context);
        googleApiConnector.connectGoogleApi();

    }
//
//    private synchronized void buildGoogleApiClient(final Context context) {
//        googleApiClient = new GoogleApiClient.Builder(context)
//                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                    @Override
//                    public void onConnected(@Nullable Bundle bundle) {
//                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//                        saveDataToDB(context, location);
//                        //getLongitude(location);
//                    }
//
//                    @Override
//                    public void onConnectionSuspended(int i) {
//                        if (!googleApiClient.isConnected())
//                            googleApiClient.connect();
//                    }
//                })
//                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        if (!googleApiClient.isConnected())
//                            googleApiClient.connect();
//                    }
//                })
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    public final void connectGoogleApi() {
//        if (googleApiClient != null) {
//            googleApiClient.connect();
//        }
//    }
//
//    public final void disconnectGoogleApi() {
//        if (googleApiClient.isConnected()) {
//            googleApiClient.disconnect();
//        }
//    }
//
//    private void saveDataToDB(Context context, Location location) {
//        MeHelper helper = new MeHelper(context, MeDatabase.DB_NAME, null, MeDatabase.DB_VERSION);
//        MeRepoUserTracker userTracker = new MeRepoImplUserTracker(helper);
//        try {
//            userTracker.saveUserCrediantials(location.getLatitude() >= 0 ? location.getLatitude() : -1, location.getLongitude() >= 0 ? location.getLongitude() : -1, getEnabledLocationProvider(context), "" + System.currentTimeMillis(), location.getSpeed(), location.getAltitude());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        helper = null;
//    }
//
//    public final Double getLatitude() {
//        return (location != null ? location.getLatitude() : -1);
//    }
//
//    public final Double getLongitude() {
//        return (location != null ? location.getLongitude() : -1);
//    }
//
//
//
//    public final String getEnabledLocationProvider(Context context) {
//        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
//    }
}
