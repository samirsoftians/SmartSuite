package com.transworldtechnology.crm.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.repository.MeRepoCheckIn;
import com.transworldtechnology.crm.database.repository.MeRepoImplCheckIn;
import com.transworldtechnology.crm.database.repository.MeRepoImplLogin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;

/**
 * Created by twtech on 24/8/18.
 */

public class EmpCheckOut extends AppCompatActivity implements LocationListener {

    private Button btn_submit;
    MainActivity parentActivity;
    String status="Out";
    String updateStatus="PENDING";
    String userName;
    Integer userCode;
    private MeHelper helper;
    Double latitude,longitude;
    private LocationManager locationManager;
    private String provider;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper=new MeHelper(this, MeDatabase.DB_NAME,null , MeDatabase.DB_VERSION);
        MeRepoImplLogin m=new MeRepoImplLogin(helper);
        try {
            userName=m.getUserName();
            userCode=m.getEmployeeCode();
        } catch (Exception e) {
            Log.e("exception","exception"+e.getMessage());
            e.printStackTrace();
        }

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
//          Toast.makeText(parentActivity, "no network", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences sp=getSharedPreferences("checkin",MODE_WORLD_READABLE);
        SharedPreferences.Editor editor=sp.edit();
        String name=sp.getString("username","");
        String comment=sp.getString("comment","");
        editor.clear();
        editor.commit();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        MeRepoCheckIn repoCheckIn = new MeRepoImplCheckIn(helper);
        try {
            repoCheckIn.saveUserDetails(userName,userCode,name,comment,status,formattedDate,latitude,longitude,updateStatus);
            final AlertDialog.Builder builder = new AlertDialog.Builder(EmpCheckOut.this);
            builder.setMessage("Sure to Check Out?")
                    .setCancelable(false)
                    .setPositiveButton("CheckOut", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            Toast.makeText(EmpCheckOut.this, "checkout successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.dismiss();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            Log.e("inside catch block", "" + e.getMessage());
            e.printStackTrace();
        }
    }


    public final MeHelper getDbHelper() {
        return helper;
    }


    @Override
    public void onLocationChanged(Location location) {

        latitude=location.getLatitude() ;
        longitude=location.getLongitude() ;
        String lat=" Latitude "+latitude;
        String lng=" Longitude "+longitude;
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
}
