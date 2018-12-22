package com.transworldtechnology.crm.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by twtech on 21/12/17.
 */

public class MeRepoImplCheckIn implements MeRepoCheckIn {
    MeHelper meHelper;

    public MeRepoImplCheckIn(MeHelper meHelper) {
        this.meHelper = meHelper;

    }

    @Override
    public void saveUserDetails(String userName, Integer uId, String clientName, String comments, String status, String dateTime, Double lat, Double lng, String updateStatus) throws Exception {
        SQLiteDatabase sqDb = meHelper.getWritableDatabase();
        // meHelper.onUpgrade(sqDb,2,3);
      //  sqDb.execSQL("delete from " + MeDatabase.TableCheckIn.TABLE_NAME);
        Log.e("MeRepoImplCheckIn", "eRepoImplCheckIn executed");

        ContentValues values = new ContentValues();
        values.put(MeDatabase.TableCheckIn.COL_U_NAME, userName);
        values.put(MeDatabase.TableCheckIn.COL_U_ID, uId);
        values.put(MeDatabase.TableCheckIn.COL_CLIENT_NAME, clientName);
        values.put(MeDatabase.TableCheckIn.COL_COMMENT, comments);
        values.put(MeDatabase.TableCheckIn.COL_STATUS, status);
        values.put(MeDatabase.TableCheckIn.COL_DATE_TIME, dateTime);
        values.put(MeDatabase.TableCheckIn.COL_LATITUDE, lat);
        values.put(MeDatabase.TableCheckIn.COL_LONGITUDE, lng);
        values.put(MeDatabase.TableCheckIn.COL_UPDATE_STATUS,updateStatus);
        sqDb.insert(MeDatabase.TableCheckIn.TABLE_NAME, null, values);
        sqDb.close();
    }

   /* @Override
    public List<MeCheckIn> sendLocations() throws Exception {
        List<MeCheckIn> checkInList = new ArrayList<>();
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();
        String sql = "select " +  "DISTINCT("+MeDatabase.TableCheckIn.COL_DATE_TIME + "),"+
                MeDatabase.TableCheckIn.COL_LATITUDE + ","+
                MeDatabase.TableCheckIn.COL_U_ID+ ","+
                MeDatabase.TableCheckIn.COL_LONGITUDE + ","+
                MeDatabase.TableCheckIn.COL_U_NAME + ","+
                MeDatabase.TableCheckIn.COL_STATUS +
                " from " + MeDatabase.TableCheckIn.TABLE_NAME;
        Cursor cursor = sqDb.rawQuery(sql,null);

        while (cursor.moveToNext())
        {
            MeCheckIn meCheckIn=new MeCheckIn();
            meCheckIn.setClientName(MeRepoFactory.getCheckInRepository(meHelper).getclientName());
            meCheckIn.setComments(MeRepoFactory.getCheckInRepository(meHelper).getcomments());
            meCheckIn.setUpdateStatus(MeRepoFactory.getCheckInRepository(meHelper).updateStatus());
            meCheckIn.setuId((int) cursor.getDouble((int) cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_U_ID)));
            meCheckIn.setLat(cursor.getDouble(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_LATITUDE)));
            meCheckIn.setLng(cursor.getDouble(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_LONGITUDE)));
            meCheckIn.setUserName(cursor.getString(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_U_NAME)));
            meCheckIn.setStatus(String.valueOf(cursor.getFloat(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_STATUS))));
            meCheckIn.setDateTime(String.valueOf(cursor.getLong(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_DATE_TIME))));
            //  if (userTracker!=null)
            checkInList.add(meCheckIn);
            Log.i("@Transworld","UserTracker Object " +meCheckIn.toString());
        }
        Log.i("@Transworld","User Tracking List " +checkInList);
        sqDb.close();
        return checkInList;
    }*/

    @Override
    public String getuserName() throws Exception {
        String uName = "";
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_U_NAME}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            uName = cursor.getString(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_U_NAME));
        }
        sqDb.close();
        return uName;

    }

    @Override
    public Integer getuId() throws Exception {

        Integer uId = -1;
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_U_ID}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            uId = cursor.getInt(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_U_ID));
        }
        sqDb.close();
        return uId;
    }

    @Override
    public String getclientName() throws Exception {
        String clientName = "";
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_CLIENT_NAME}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            clientName = cursor.getString(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_CLIENT_NAME));
        }
        sqDb.close();
        return clientName;
    }

    @Override
    public String getcomments() throws Exception {
        String comments = "";
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_COMMENT}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            comments = cursor.getString(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_COMMENT));
        }
        sqDb.close();
        return comments;
    }


    @Override
    public String getstatus() throws Exception {
        String status = "checked-in";
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_STATUS}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            status = cursor.getString(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_STATUS));
        }
        sqDb.close();
        return status;
    }

    @Override
    public String getdateTime() throws Exception {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // Toast.makeText(MeRepoImplCheckIn.class, formattedDate, Toast.LENGTH_SHORT).show();
        String dateTime = formattedDate;
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_DATE_TIME}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            dateTime = cursor.getString(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_DATE_TIME));
        }
        sqDb.close();
        return dateTime;
    }

    @Override
    public Double getlat() throws Exception {
        Double lat = 0.0;
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_LATITUDE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            lat = cursor.getDouble(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_LATITUDE));
        }
        sqDb.close();


        return lat;
    }

    @Override
    public Double getlng() throws Exception {
        Double lng = 0.0;
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_LONGITUDE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            lng = cursor.getDouble(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_LONGITUDE));
        }
        sqDb.close();


        return lng;
    }

    @Override
    public String updateStatus() throws Exception {
        String updateStatus = " ";
        SQLiteDatabase sqDb = meHelper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.TableCheckIn.TABLE_NAME, new String[]{MeDatabase.TableCheckIn.COL_UPDATE_STATUS}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            updateStatus = cursor.getString(cursor.getColumnIndex(MeDatabase.TableCheckIn.COL_UPDATE_STATUS));
        }
        sqDb.close();
        return updateStatus;
    }
}