package com.transworldtechnology.crm.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.domain.MeUserTracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by transworldtechnology on 12/4/16.
 */
public class MeRepoImplUserTracker implements MeRepoUserTracker {
    private MeHelper helper;

    public MeRepoImplUserTracker(MeHelper helper) {
        this.helper = helper;
    }

    @Override
    public void saveUserCrediantials(Double lat, Double lng, String source, String timeZone, Float speed, Double altitude) throws Exception {
        ContentValues values = new ContentValues();
        SQLiteDatabase sqDb = helper.getWritableDatabase();
      /*  values.put(MeDatabase.MeTableUserTracker.COL_COMPANY_MASTER_ID, companyMasterId);
        values.put(MeDatabase.MeTableUserTracker.COL_MARKETING_REP_CODE, MrepCode);
        values.put(MeDatabase.MeTableUserTracker.COL_USERNAME, userName);*/
        values.put(MeDatabase.MeTableUserTracker.COL_LATITUDE, lat);
        values.put(MeDatabase.MeTableUserTracker.COL_LONGITUDE, lng);
        values.put(MeDatabase.MeTableUserTracker.COL_SOURCE, source);
        values.put(MeDatabase.MeTableUserTracker.COL_TIME_ZONE, timeZone);
        //   values.put(MeDatabase.MeTableUserTracker.COL_IMEI, imei);
        values.put(MeDatabase.MeTableUserTracker.COL_SPEED, speed);
        values.put(MeDatabase.MeTableUserTracker.COL_ALTITUDE, altitude);
        values.put(MeDatabase.MeTableUserTracker.COL_STATUS, "pending");
        sqDb.insert(MeDatabase.MeTableUserTracker.TABLE_NAME, null, values);
        Log.i("@transworldcrm", "Data Inserted!!\n" + values);
        sqDb.close();


    }

    @Override
    public List<MeUserTracker> sendLocations() throws Exception {
        List<MeUserTracker> userTrackerList = new ArrayList<>();
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String sql = "select " +  "DISTINCT("+MeDatabase.MeTableUserTracker.COL_TIME_ZONE + "),"+
                MeDatabase.MeTableUserTracker.COL_LATITUDE + ","+
                MeDatabase.MeTableUserTracker.COL_ALTITUDE + ","+
                MeDatabase.MeTableUserTracker.COL_LONGITUDE + ","+
                MeDatabase.MeTableUserTracker.COL_SOURCE + ","+
                MeDatabase.MeTableUserTracker.COL_SPEED +


                 " from " + MeDatabase.MeTableUserTracker.TABLE_NAME+" WHERE status = 'pending'";
        Cursor cursor = sqDb.rawQuery(sql,null);
        Log.i("SQL Query",sql);

        while (cursor.moveToNext())
        {
            MeUserTracker userTracker = new MeUserTracker();
            userTracker.setCompanyMasterId(MeRepoFactory.getLoginRepository(helper).getCompanyMasterId());
            userTracker.setUserId(MeRepoFactory.getLoginRepository(helper).getEmployeeCode());
            userTracker.setUserName(MeRepoFactory.getLoginRepository(helper).getUserName());
            userTracker.setImei(MeRepoFactory.getLoginRepository(helper).getImei());
            userTracker.setAltitude(cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_ALTITUDE)));
            userTracker.setLoc_lat(cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_LATITUDE)));
            userTracker.setLoc_long(cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_LONGITUDE)));
            userTracker.setLoc_source(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_SOURCE)));
            userTracker.setSpeed(cursor.getFloat(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_SPEED)));
            userTracker.setTimeZone(cursor.getLong(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_TIME_ZONE)));
            //  if (userTracker!=null)
            userTrackerList.add(userTracker);
            Log.i("@Transworld","UserTracker Object " +userTracker.toString());
        }
        Log.i("@Transworld","User Tracking List " +userTrackerList);
        sqDb.close();
        return userTrackerList;
    }

    @Override
    public void deleteTableData() throws Exception {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        sqDb.execSQL("delete from " + MeDatabase.MeTableUserTracker.TABLE_NAME);
        Log.i("@Transworld", "Deleted Record Successfully");
    }

    @Override
    public Integer getcompanyMasterId() throws Exception {
        Integer companyMasterId = -10;
        //  SQLiteDatabase sqDb = helper.getReadableDatabase();

       /*// Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_COMPANY_MASTER_ID}, null, null, null, null, null);
        if (cursor.moveToNext()) {

           // companyMasterId = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_COMPANY_MASTER_ID));
        }
        sqDb.close();*/
        return companyMasterId;
    }

    @Override
    public Integer getMrepCode() throws Exception {
        Integer MarketingRepCode = -1;
        // SQLiteDatabase sqDb = helper.getReadableDatabase();

      /* // Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_MARKETING_REP_CODE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

          //  MarketingRepCode = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_MARKETING_REP_CODE));
        }
        sqDb.close();
*/

        return MarketingRepCode;
    }

    @Override
    public String getUserName() throws Exception {
        String userName = "";
       /* SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_USERNAME}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            userName = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_USERNAME));
        }
        sqDb.close();*/

        return userName;
    }

    @Override
    public Double getLatitude() throws Exception {
        Double lat =0.0;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_LATITUDE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            lat = cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_LATITUDE));
        }
        sqDb.close();


        return lat;
    }

    @Override
    public Double getLongitude() throws Exception {
        Double lng =0.0;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_LONGITUDE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            lng = cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_LONGITUDE));
        }
        sqDb.close();


        return lng;
    }

    @Override
    public String getSource() throws Exception {
        String source = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_SOURCE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            source = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_SOURCE));
        }
        sqDb.close();

        return source;
    }

    @Override
    public String getTimeZone() throws Exception {
        String timeZone = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_TIME_ZONE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            timeZone = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_TIME_ZONE));
        }
        sqDb.close();

        return timeZone;
    }

    @Override
    public String getImei() throws Exception {
        String imei = "";
      /*  SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_IMEI}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            imei = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_IMEI));
        }
        sqDb.close();
*/
        return imei;
    }

    @Override
    public Float getSpeed() throws Exception {
        Float speed = 0.0f;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_SPEED}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            speed = cursor.getFloat(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_SPEED));
        }
        sqDb.close();

        return speed;
    }

    @Override
    public Double getAltitude() throws Exception {
        Double altitude = 0.0;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableUserTracker.TABLE_NAME, new String[]{MeDatabase.MeTableUserTracker.COL_ALTITUDE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            altitude = cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableUserTracker.COL_ALTITUDE));
        }
        sqDb.close();

        return altitude;
    }
}
