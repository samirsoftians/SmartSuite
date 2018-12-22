package com.transworldtechnology.crm.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.repository.MeRepoCheckIn;
import com.transworldtechnology.crm.database.repository.MeRepoImplCheckIn;
import com.transworldtechnology.crm.database.repository.MeRepoImplLogin;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmpCheckIn extends AppCompatActivity implements LocationListener{

    private EditText ed_UName;
    private EditText ed_comment;
    private Button btn_submit;
    MainActivity parentActivity;
    String status="In";
    String updateStatus="PENDING";
    String userName;
    Integer userCode;
    private MeHelper helper;
    Double latitude,longitude;
    private LocationManager locationManager;
    private String provider;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_check_in);
        helper=new MeHelper(this, MeDatabase.DB_NAME,null , MeDatabase.DB_VERSION);
        //final MeHelper meHelper=new MeHelper(this,"transworldcompressor",null , 2);
        ed_UName = (EditText) findViewById(R.id.textClientName);
        ed_comment = (EditText) findViewById(R.id.edtComments);
        sp=getSharedPreferences("checkin",MODE_PRIVATE);


        //if SharedPreferences contains username and password then redirect to Home activity
        if(sp.contains("username") && sp.contains("comment")){
            startActivity(new Intent(EmpCheckIn.this,EmpCheckOut.class));
            finish();   //finish current activity
        }
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
        PendingIntent pendingIntent;
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,1,this);
        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
//           Toast.makeText(parentActivity, "no network", Toast.LENGTH_SHORT).show();
        }
        btn_submit = (Button) findViewById(R.id.btnSubmit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empName = ed_UName.getText().toString();
                String comment = ed_comment.getText().toString();
                SharedPreferences.Editor e=sp.edit();
                e.putString("username",empName);
                e.putString("comment",comment);
                e.commit();
                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                MeRepoCheckIn repoCheckIn = new MeRepoImplCheckIn(helper);
                try {
                    repoCheckIn.saveUserDetails(userName,userCode,empName,comment,status,formattedDate,latitude,longitude,updateStatus);
                    Toast.makeText(EmpCheckIn.this, "submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception exception) {
                    Log.e("inside catch block", "" + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        });
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
        Log.e("LOCATION CHANGED", location.getLatitude() + "");
        Log.e("LOCATION CHANGED", location.getLongitude() + "");
       // Toast.makeText(EmpCheckIn.this, "My Current location:\nLatitude:"+location.getLatitude() + "\nLogitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();
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
