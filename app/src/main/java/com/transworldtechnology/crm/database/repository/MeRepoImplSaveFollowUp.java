package com.transworldtechnology.crm.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.domain.MeFollowUp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 18/2/16.
 */
public class MeRepoImplSaveFollowUp implements MeRepoSaveFollowUp {
    private static String followUpType;
    private MeHelper helper;
    private static Integer theSrno;

    public MeRepoImplSaveFollowUp(MeHelper helper) {
        this.helper = helper;
    }

    @Override
    public void saveFollowUpLocally(Map<String, Object> mapEntity) {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MeDatabase.MeTableFollowUp.COL_ADDRESS, "" + mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED, "" + mapEntity.get("amountExpected"));
        values.put(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED_ON, "" + mapEntity.get("amountExpectedOn"));
        values.put(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USER_ID, "" + mapEntity.get("assignToUserId"));
        values.put(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USERNAME, "" + mapEntity.get("assignToUserName"));
        values.put(MeDatabase.MeTableFollowUp.COL_CITY, "" + mapEntity.get("city"));
        // values.put(MeDatabase.MeTableFollowUp.COL_COLLECTION_STAFF_NAME,""+mapEntity.get("address"));
        //  values.put(MeDatabase.MeTableFollowUp.COL_NEXT_COLLECTION_STAFF_NAME,""+mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_COMMENTS, "" + mapEntity.get("comments"));
        values.put(MeDatabase.MeTableFollowUp.COL_CONTACT_ID, "" + mapEntity.get("contactId"));
        values.put(MeDatabase.MeTableFollowUp.COL_CONTACT_PERSON, "" + mapEntity.get("contactPerson"));
        values.put(MeDatabase.MeTableFollowUp.COL_DESIGNATION, "" + mapEntity.get("designation"));
        // values.put(MeDatabase.MeTableFollowUp.COL_VALUE,""+mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_TIMEZONE, "" + mapEntity.get("timeZone"));
        //  values.put(MeDatabase.MeTableFollowUp.COL_THE_GROUP_CODE,  ""+mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_STATUS, "" + mapEntity.get("status"));
        values.put(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO, "" + mapEntity.get("spokenTo"));
        //  values.put(MeDatabase.MeTableFollowUp.COL_REPLY, ""+mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_REMARKS, "" + mapEntity.get("remarks"));
        // values.put(MeDatabase.MeTableFollowUp.COL_QTY,  ""+mapEntity.get("qty"));
        values.put(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME, "" + mapEntity.get("prospCustName"));
        values.put(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_CODE, "" + mapEntity.get("prospCustCode"));
        values.put(MeDatabase.MeTableFollowUp.COL_PREPARATION, "" + mapEntity.get("preparation"));
        values.put(MeDatabase.MeTableFollowUp.COL_PAYMENT_FOLLOW_UP, "" + mapEntity.get("paymentFollowUp"));
        //   values.put(MeDatabase.MeTableFollowUp.COL_NO_OF_SALES_QUO,  ""+mapEntity.get("address"));
        //  values.put(MeDatabase.MeTableFollowUp.COL_NO_OF_MAILER_TO_CUST, ""+mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE, "" + mapEntity.get("nextFollowUpType"));
        values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE, "" + mapEntity.get("nextFollowUpDate"));
        values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME, "" + mapEntity.get("nextFollowUpTime"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_DATE, "" + mapEntity.get("followUpDate"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TIME, "" + mapEntity.get("followUpTime"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TYPE, "" + mapEntity.get("followUpType"));
        //  values.put(MeDatabase.MeTableFollowUp.COL_MODEL, ""+mapEntity.get("address"));
        //  values.put(MeDatabase.MeTableFollowUp.COL_DOCUMENT_COUNT,  ""+mapEntity.get("address"));
        //   values.put(MeDatabase.MeTableFollowUp.COL_DOCUMENT_NAME,  ""+mapEntity.get("address"));
        //   values.put(MeDatabase.MeTableFollowUp.COL_DOCUMENT_STATUS, ""+mapEntity.get("address"));
        //    values.put(MeDatabase.MeTableFollowUp.COL_DOCUMENT_TYPE, ""+mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_IN_OUT, "" + mapEntity.get("followUpInOut"));
        values.put(MeDatabase.MeTableFollowUp.COL_LOC_LAT, "" + mapEntity.get("locLat"));
        values.put(MeDatabase.MeTableFollowUp.COL_LOC_LONG, "" + mapEntity.get("locLong"));
        values.put(MeDatabase.MeTableFollowUp.COL_LOC_SOURCE, "" + mapEntity.get("locSource"));
        //  values.put(MeDatabase.MeTableFollowUp.COL_MAKE_CODE, ""+mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_MARKETING_REP_CODE, "" + mapEntity.get("marketingRepCode"));
        values.put(MeDatabase.MeTableFollowUp.COL_DOC_PATH_1,""+mapEntity.get("docPath1"));
        values.put(MeDatabase.MeTableFollowUp.COL_DOC_PATH_2,""+mapEntity.get("docPath2"));
        values.put(MeDatabase.MeTableFollowUp.COL_DOC_PATH_3,""+mapEntity.get("docPath3"));
        values.put(MeDatabase.MeTableFollowUp.COL_FLAG, 1);
        sqDb.insert(MeDatabase.MeTableFollowUp.TABLE_NAME, null, values);
        Log.i("@Transworld", "Data Saved Locally with flag 1 offline"+values);
        sqDb.close();
    }

    @Override
    public void saveFollowUpLocallyServer(Map<String, Object> mapEntity) {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MeDatabase.MeTableFollowUp.COL_ADDRESS, "" + mapEntity.get("address"));
        values.put(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED, "" + mapEntity.get("amountExpected"));
        values.put(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED_ON, "" + mapEntity.get("amountExpectedOn"));
        values.put(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USER_ID, "" + mapEntity.get("assignToUserId"));
        values.put(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USERNAME, "" + mapEntity.get("assignToUserName"));
        values.put(MeDatabase.MeTableFollowUp.COL_CITY, "" + mapEntity.get("city"));
        values.put(MeDatabase.MeTableFollowUp.COL_COMMENTS, "" + mapEntity.get("comments"));
        values.put(MeDatabase.MeTableFollowUp.COL_CONTACT_ID, "" + mapEntity.get("contactId"));
        values.put(MeDatabase.MeTableFollowUp.COL_CONTACT_PERSON, "" + mapEntity.get("contactPerson"));
        values.put(MeDatabase.MeTableFollowUp.COL_DESIGNATION, "" + mapEntity.get("designation"));
        values.put(MeDatabase.MeTableFollowUp.COL_TIMEZONE, "" + mapEntity.get("timeZone"));
        values.put(MeDatabase.MeTableFollowUp.COL_STATUS, "" + mapEntity.get("status"));
        values.put(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO, "" + mapEntity.get("spokenTo"));
        values.put(MeDatabase.MeTableFollowUp.COL_REMARKS, "" + mapEntity.get("remarks"));
        values.put(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME, "" + mapEntity.get("prospCustName"));
        values.put(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_CODE, "" + mapEntity.get("prospCustCode"));
        values.put(MeDatabase.MeTableFollowUp.COL_PREPARATION, "" + mapEntity.get("preparation"));
        values.put(MeDatabase.MeTableFollowUp.COL_PAYMENT_FOLLOW_UP, "" + mapEntity.get("paymentFollowUp"));
        values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE, "" + mapEntity.get("nextFollowUpType"));
        values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE, "" + mapEntity.get("nextFollowUpDate"));
        values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME, "" + mapEntity.get("nextFollowUpTime"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_DATE, "" + mapEntity.get("followUpDate"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TIME, "" + mapEntity.get("followUpTime"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TYPE, "" + mapEntity.get("followUpType"));
        values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_IN_OUT, "" + mapEntity.get("followUpInOut"));
        values.put(MeDatabase.MeTableFollowUp.COL_LOC_LAT, "" + mapEntity.get("locLat"));
        values.put(MeDatabase.MeTableFollowUp.COL_LOC_LONG, "" + mapEntity.get("locLong"));
        values.put(MeDatabase.MeTableFollowUp.COL_LOC_SOURCE, "" + mapEntity.get("locSource"));
        values.put(MeDatabase.MeTableFollowUp.COL_MARKETING_REP_CODE, "" + mapEntity.get("marketingRepCode"));
        values.put(MeDatabase.MeTableFollowUp.COL_DOC_PATH_1,""+mapEntity.get("docPath1"));
        values.put(MeDatabase.MeTableFollowUp.COL_DOC_PATH_2,""+mapEntity.get("docPath2"));
        values.put(MeDatabase.MeTableFollowUp.COL_DOC_PATH_3,""+mapEntity.get("docPath3"));
        values.put(MeDatabase.MeTableFollowUp.COL_FLAG, 0);
        sqDb.insert(MeDatabase.MeTableFollowUp.TABLE_NAME, null, values);
        Log.i("@Transworld", "Data Saved Locally with flag 1 online");
        sqDb.close();
    }

    @Override
    public void saveListAppointment(List<MeFollowUp> appointmentList) {
        if (appointmentList != null) {
            SQLiteDatabase sqDb = helper.getWritableDatabase();
            //  sqDb.execSQL("delete from " + MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME);

            Log.i("@Transworld", "Map in Impl" + appointmentList);
            for (MeFollowUp followUp : appointmentList) {
                ContentValues values = new ContentValues();
                //Date Format
                Long nextFollowUpDate = Long.parseLong(followUp.getNextFollowUpDate());
                Date date1 = new Date(nextFollowUpDate);
                Long followUpDate = Long.parseLong(followUp.getFollowUpDate());
                Date date2 = new Date(followUpDate);
                Date amtDate = null;
                if (followUp.getAmountExpectedOn() != null) {
                    Long amtExptOn = Long.parseLong(followUp.getAmountExpectedOn());
                    amtDate = new Date(amtExptOn);
                }
                //Time Format
                Long nextFollowUPTime = Long.parseLong(followUp.getNextFollowUpTime());
                Date time1 = new Date(nextFollowUPTime);
                Long followUPTime = Long.parseLong(followUp.getFollowUpTime());
                Date time2 = new Date(followUPTime);


                values.put(MeDatabase.MeTableFollowUp.COL_SR_NO, "" + followUp.getTheSrNo());
                values.put(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_CODE, "" + followUp.getProspCustCode());
                values.put(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME, "" + followUp.getProspCustName());
                values.put(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED, "" + followUp.getAmountExpected());
                values.put(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED_ON, "" + ((amtDate != null) ? (new SimpleDateFormat("yyyy-MM-dd").format(amtDate).toString()) : followUp.getAmountExpectedOn()));
                values.put(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USER_ID, "" + followUp.getAssignedToUserId());
                values.put(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USERNAME, "" + followUp.getAssignedToUserName());
                values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE, "" + followUp.getNextFollowUpType());
                values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE, "" + new SimpleDateFormat("yyyy-MM-dd").format(date1).toString()); //
                values.put(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME, "" + new SimpleDateFormat("HH:mm").format(time1).toString());
                values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_DATE, "" + new SimpleDateFormat("yyyy-MM-dd").format(date2).toString());
                values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TIME, "" + new SimpleDateFormat("HH:mm").format(time2).toString());
                values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TYPE, "" + followUp.getFollowUpType());
                values.put(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_IN_OUT, "" + followUp.getFollowUpInOut());
                values.put(MeDatabase.MeTableFollowUp.COL_PAYMENT_FOLLOW_UP, "" + followUp.getPaymentFollowUp());
                values.put(MeDatabase.MeTableFollowUp.COL_COMMENTS, "" + followUp.getComments());
                values.put(MeDatabase.MeTableFollowUp.COL_PREPARATION, "" + followUp.getPreparation());
                values.put(MeDatabase.MeTableFollowUp.COL_CONTACT_ID, "" + followUp.getContactId());
                values.put(MeDatabase.MeTableFollowUp.COL_CONTACT_PERSON, "" + followUp.getContactPerson());
                values.put(MeDatabase.MeTableFollowUp.COL_REMARKS, followUp.getRemarks());
                values.put(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO, "" + followUp.getSpokenTo());
                values.put(MeDatabase.MeTableFollowUp.COL_STATUS, "" + followUp.getStatus());
                values.put(MeDatabase.MeTableFollowUp.COL_FLAG, 0);
                Log.i("@Transworld", "Insert Data " + values);
                sqDb.insert(MeDatabase.MeTableFollowUp.TABLE_NAME, null, values);
            }
            sqDb.close();
        }
    }

    @Override
    public void getFollowUpType(String followUpType) throws Exception {
        if (followUpType != null) {
            this.followUpType = followUpType;
            Log.i("@Transworld", "Follow Up Type:" + followUpType);
        }
    }

    @Override
    public List<Map<String, Object>> getAppointmentsListToday(String date1, String followUpType) throws Exception {
        List<Map<String, Object>> appointmentList = new ArrayList<>();

        Cursor cursor;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        if (followUpType.equalsIgnoreCase("All")) {
            final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + "," + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," + MeDatabase.MeTableFollowUp.COL_STATUS + " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                    " where date(" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + ") = date('" + date1 + "')";//+date1;
            cursor = sqDb.rawQuery(sql, null);
            Log.i("@Transworld", "All Query: " + sql);
        } else {
            final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + "," + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," + MeDatabase.MeTableFollowUp.COL_STATUS + " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                    " where " + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + " = '" + followUpType + "'";//+
            cursor = sqDb.rawQuery(sql, null);
            Log.i("@Transworld", "Follow Up Query: " + cursor);
        }

        while (cursor.moveToNext()) {
            Map<String, Object> mapInner = new HashMap<>();
            mapInner.put("theSrNo", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            mapInner.put("companyName", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME)));
            mapInner.put("contactPerson", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO)));
            mapInner.put("followUpType", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE)));
            mapInner.put("appointmentDate", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE)));
            mapInner.put("Time", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME)));
            //          mapInner.put("TheSrno", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            appointmentList.add(mapInner);



        }
        Log.i("@Transworld", "Appointment List SQLite: " + appointmentList);
        sqDb.close();
        return appointmentList;
    }

    @Override
    public List<Map<String, Object>> getAppointmentsListWeekly(String date1, String followUpType) throws Exception {
        List<Map<String, Object>> appointmentListWeekly = new ArrayList<>();
        Log.i("@Transworld", "Current Date is weekly:" + date1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date1));
        c.add(Calendar.DATE, 7);  // number of days to add
        String date2 = sdf.format(c.getTime());

        Cursor cursor;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        if (followUpType.equalsIgnoreCase("All")) {
            final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + "," + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," + MeDatabase.MeTableFollowUp.COL_STATUS + " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                    " where " + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + " BETWEEN date('" + date1 + "') AND date('" + date2 + "')";
            cursor = sqDb.rawQuery(sql, null);
            Log.i("@Transworld", "All Query Weekly: " + sql);
        } else {
            final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + "," + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," + MeDatabase.MeTableFollowUp.COL_STATUS + " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                    " where " + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + " BETWEEN date('" + date1 + "') AND date('" + date2 + "') and "
                    + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + " = '" + followUpType + "'";//+
            cursor = sqDb.rawQuery(sql, null);
            Log.i("@Transworld", "Follow Up Query Weekly: " + cursor);
        }
        while (cursor.moveToNext()) {
            Map<String, Object> mapInner = new HashMap<>();
            mapInner.put("theSrNo", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            mapInner.put("companyName", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME)));
            mapInner.put("contactPerson", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO)));
            mapInner.put("followUpType", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE)));
            mapInner.put("appointmentDate", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE)));
            mapInner.put("Time", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME)));
            //          mapInner.put("TheSrno", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            appointmentListWeekly.add(mapInner);
        }
        Log.i("@Transworld", "Appointment List SQLite Weekly: " + appointmentListWeekly);
        sqDb.close();
        return appointmentListWeekly;
    }

    @Override
    public List<Map<String, Object>> getAppointmentsListMonthly(String date1, String followUpType) throws Exception {
        List<Map<String, Object>> appointmentListWeekly = new ArrayList<>();
        Log.i("@Transworld", "Current Date is Monthly:" + date1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date1));
        c.add(Calendar.MONTH, 1);  // after 1 month
        String date2 = sdf.format(c.getTime());
        Cursor cursor;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        if (followUpType.equalsIgnoreCase("All")) {
            final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + "," + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," + MeDatabase.MeTableFollowUp.COL_STATUS + " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                    " where " + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + " BETWEEN date('" + date1 + "') AND date('" + date2 + "')";
            cursor = sqDb.rawQuery(sql, null);
            Log.i("@Transworld", "All Query Monthly: " + sql);
        } else {
            final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + "," + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                    "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," + MeDatabase.MeTableFollowUp.COL_STATUS + " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                    " where " + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + " BETWEEN date('" + date1 + "') AND date('" + date2 + "') and "
                    + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + " = '" + followUpType + "'";//+
            cursor = sqDb.rawQuery(sql, null);
            Log.i("@Transworld", "Follow Up Query Monthly: " + cursor);
        }
        while (cursor.moveToNext()) {
            Map<String, Object> mapInner = new HashMap<>();
            mapInner.put("theSrNo", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            mapInner.put("companyName", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME)));
            mapInner.put("contactPerson", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO)));
            mapInner.put("followUpType", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE)));
            mapInner.put("appointmentDate", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE)));
            mapInner.put("Time", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME)));
            //          mapInner.put("TheSrno", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            appointmentListWeekly.add(mapInner);
        }
        Log.i("@Transworld", "Appointment List SQLite Monthly: " + appointmentListWeekly);
        sqDb.close();
        return appointmentListWeekly;
    }

    @Override
    public List<HashMap<String, Object>> getAppointmentDetails(Integer theSrno) throws Exception {
        List<HashMap<String, Object>> appointmentListDetails = new ArrayList<>();
        Cursor cursor;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                "" + MeDatabase.MeTableFollowUp.COL_CONTACT_PERSON + "," +
                "" + MeDatabase.MeTableFollowUp.COL_PREPARATION + "," +
                "" + MeDatabase.MeTableFollowUp.COL_PAYMENT_FOLLOW_UP + "," +
                "" + MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED + "," +
                "" + MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED_ON + "," +
                "" + MeDatabase.MeTableFollowUp.COL_REMARKS + "," +
                "" + MeDatabase.MeTableFollowUp.COL_STATUS + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_IN_OUT + "," +
                "" + MeDatabase.MeTableFollowUp.COL_COMMENTS + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_DATE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TIME + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TYPE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +
                "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + "," + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," + MeDatabase.MeTableFollowUp.COL_STATUS + " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                " where " + MeDatabase.MeTableFollowUp.COL_SR_NO + " = " + theSrno + "";
        cursor = sqDb.rawQuery(sql, null);
        Log.i("@Transworld", "Follow Up Query Monthly: " + cursor);
        while (cursor.moveToNext()) {
            HashMap<String, Object> mapInner = new HashMap<>();
            mapInner.put("contactPerson",cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO)));//changes
            mapInner.put("preparation", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PREPARATION)));
            mapInner.put("paymentFollowUp", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PAYMENT_FOLLOW_UP)));
            mapInner.put("amountExpected", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED)));
            mapInner.put("amountExpectedOn", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED_ON)));
            mapInner.put("remarks", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_REMARKS)));
            mapInner.put("status", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_STATUS)));
            mapInner.put("followUpInOut", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_IN_OUT)));
            mapInner.put("comments", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_COMMENTS)));
            // mapInner.put("theSrNo",cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            mapInner.put("companyName", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME)));
            //   mapInner.put("contactPerson", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO)));
            mapInner.put("nextFollowUpType", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE)));
            mapInner.put("nextFollowUpDate", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE)));
            mapInner.put("nextFollowUpTime", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME)));
            mapInner.put("followUpType", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TYPE)));
            mapInner.put("followUpDate", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_DATE)));
            mapInner.put("followUpTime", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TIME)));
            //          mapInner.put("TheSrno", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO)));
            appointmentListDetails.add(mapInner);
        }
        Log.i("@Transworld", "Appointment List SQLite Details: " + appointmentListDetails);
        sqDb.close();
        return appointmentListDetails;
    }

    @Override
    public Integer checkDataFollowUp() {

        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + MeDatabase.MeTableFollowUp.TABLE_NAME + " where " + MeDatabase.MeTableFollowUp.COL_FLAG + " = 1";
        Cursor cursor= sqDb.rawQuery(countQuery ,null);
        int cnt = cursor.getCount();
        Log.i("@Transworld","COunt FollowUp "+cnt +" ," +countQuery);
        cursor.close();
        return cnt;

    }

    @Override
    public List<Map<String, Object>> getFollowUpData() {
        List<Map<String, Object>> followupData = new ArrayList<>();
        Cursor cursor;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        final String sql = " select " + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME + "," +
                "" + MeDatabase.MeTableFollowUp.COL_PROSP_CUST_CODE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_MARKETING_REP_CODE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_PREPARATION + "," +
                "" + MeDatabase.MeTableFollowUp.COL_PAYMENT_FOLLOW_UP + "," +
                "" + MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED + "," +
                "" + MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED_ON + "," +
                "" + MeDatabase.MeTableFollowUp.COL_REMARKS + "," +
                "" + MeDatabase.MeTableFollowUp.COL_STATUS + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_IN_OUT + "," +
                "" + MeDatabase.MeTableFollowUp.COL_COMMENTS + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_DATE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TIME + "," +
                "" + MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TYPE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE + ","
                + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME + "," +
                "" + MeDatabase.MeTableFollowUp.COL_SR_NO + "," +  //requires to find out follow up docs
                "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                "" + MeDatabase.MeTableFollowUp.COL_CITY + "," +
                "" + MeDatabase.MeTableFollowUp.COL_CONTACT_ID + "," +
                "" + MeDatabase.MeTableFollowUp.COL_CONTACT_PERSON + "," +
                "" + MeDatabase.MeTableFollowUp.COL_SPOKEN_TO + "," +
                "" + MeDatabase.MeTableFollowUp.COL_DESIGNATION + "," +
                "" + MeDatabase.MeTableFollowUp.COL_ADDRESS + "," +
                "" + MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USER_ID + "," +
                "" + MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USERNAME + "," +
                "" + MeDatabase.MeTableFollowUp.COL_LOC_SOURCE + "," +
                "" + MeDatabase.MeTableFollowUp.COL_LOC_LAT + "," +
                "" + MeDatabase.MeTableFollowUp.COL_LOC_LONG + "," +
                "" + MeDatabase.MeTableFollowUp.COL_DOC_PATH_1 + ","+
                "" + MeDatabase.MeTableFollowUp.COL_DOC_PATH_2 + ","+
                "" + MeDatabase.MeTableFollowUp.COL_DOC_PATH_3+
                " from  " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                " where " + MeDatabase.MeTableFollowUp.COL_FLAG + " = 1 ";
        cursor = sqDb.rawQuery(sql, null);
        Log.i("@Transworld", "Follow Up Query Monthly: " + cursor);
        while (cursor.moveToNext()) {
            Map<String, Object> mapInner = new HashMap<>();
            theSrno = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SR_NO));
            Log.i("@Transworld", "SR NO IN " + theSrno);
            mapInner.put("prospCustName", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_NAME)));
            mapInner.put("prospCustCode", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PROSP_CUST_CODE)));
            mapInner.put("marketingRepCode", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_MARKETING_REP_CODE)));
            mapInner.put("preparation", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PREPARATION)));
            mapInner.put("paymentFollowUp", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_PAYMENT_FOLLOW_UP)));
            mapInner.put("amountExpected", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED)));
            mapInner.put("amountExpectedOn", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_AMOUNT_EXPECTED_ON)));
            mapInner.put("remarks", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_REMARKS)));
            mapInner.put("status", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_STATUS)));
            mapInner.put("followUpInOut", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_IN_OUT)));
            mapInner.put("comments", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_COMMENTS)));
            mapInner.put("followUpType", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TYPE)));
            mapInner.put("followUpDate", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_DATE)));
            mapInner.put("followUpTime", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FOLLOW_UP_TIME)));
            mapInner.put("nextFollowUpType", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TYPE)));
            mapInner.put("nextFollowUpDate", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_DATE)));
            mapInner.put("nextFollowUpTime", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_NEXT_FOLLOW_UP_TIME)));
            mapInner.put("spokenTo", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_SPOKEN_TO)));
            mapInner.put("city", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_CITY)));
            mapInner.put("contactId", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_CONTACT_ID)));
            mapInner.put("contactPerson", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_CONTACT_PERSON)));
            mapInner.put("designation", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_DESIGNATION)));
            mapInner.put("address", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_ADDRESS)));
            mapInner.put("locSource", cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_LOC_SOURCE)));
            mapInner.put("locLat", cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_LOC_LAT)));
            mapInner.put("locLong", cursor.getDouble(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_LOC_LONG)));
            mapInner.put("assignToUserName", cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USERNAME)));
            mapInner.put("assignToUserId", cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_ASSIGN_TO_USER_ID)));
            mapInner.put("docPath1",new File((cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_DOC_PATH_1))) != null ? (cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_DOC_PATH_1))) : ""));
            mapInner.put("docPath2",new File((cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_DOC_PATH_2))) != null ? (cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_DOC_PATH_2))) : ""));
            mapInner.put("docPath3",new File((cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_DOC_PATH_3))) != null ? (cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_DOC_PATH_3))) : ""));
            try {
                mapInner.put("companyMasterId", "" + MeRepoFactory.getLoginRepository(helper).getCompanyMasterId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            followupData.add(mapInner);
        }

      /*  Map<String,Object> uriListToServer = new HashMap<>();
        SQLiteDatabase sqDb1 = helper.getReadableDatabase();
        String sql1 = "select " + MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_1 + ","
                + MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_2 + ","
                + MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_3 +
                " from " + MeDatabase.MeTableFollowUpDocs.TABLE_NAME +
                " where " + MeDatabase.MeTableFollowUpDocs.COL_SR_NO + " = " + theSrno;
        Cursor cursor1 = sqDb1.rawQuery(sql1, null);
        while (cursor1.moveToNext()) {
            List<File> uriList = new ArrayList<>();
            uriList.add(new File((cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_1))) != null ? (cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_1))) : ""));
            uriList.add(new File((cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_2))) != null ? (cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_2))) : ""));
            uriList.add(new File((cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_3))) != null ? (cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_3))) : ""));
            uriListToServer.put("multipartFile",uriList);
        }
        Log.i("@Transworld", "List of Files" + uriListToServer);
        followupData.add(uriListToServer);*/

        Log.i("@Transworld", " Details: " + followupData);
        sqDb.close();
        return followupData;
    }

    @Override
    public List<File> getFileList() {
        List<File> uriListToServer = new ArrayList<>();
        SQLiteDatabase sqDb1 = helper.getReadableDatabase();
        String sql1 = "select " + MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_1 + ","
                + MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_2 + ","
                + MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_3 +
                " from " + MeDatabase.MeTableFollowUpDocs.TABLE_NAME +
                " where " + MeDatabase.MeTableFollowUpDocs.COL_SR_NO + " = " + theSrno;
        Log.i("@Transworld","SQL : "+sql1);
        Cursor cursor1 = sqDb1.rawQuery(sql1, null);
        while (cursor1.moveToNext()) {
            List<File> uriList = new ArrayList<>();
            uriList.add(new File((cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_1))) != null ? (cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_1))) : ""));
            uriList.add(new File((cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_2))) != null ? (cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_2))) : ""));
            uriList.add(new File((cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_3))) != null ? (cursor1.getString(cursor1.getColumnIndex(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_3))) : ""));
            uriListToServer = uriList;
        }
        Log.i("@Transworld", "List of Files" + uriListToServer);
        return uriListToServer;
    }

    @Override
    public void updateFlag() throws Exception {
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String sql = "UPDATE " + MeDatabase.MeTableFollowUp.TABLE_NAME +
                " SET " + MeDatabase.MeTableFollowUp.COL_FLAG + " = 0  WHERE " +
                MeDatabase.MeTableFollowUp.COL_FLAG + "=1";
        Log.i("@Transworld", "Update flag " + sql);
        String sql1 = "UPDATE " + MeDatabase.MeTableFollowUpDocs.TABLE_NAME +
                " SET " + MeDatabase.MeTableFollowUpDocs.COL_FLAG + " = 0  WHERE " +
                MeDatabase.MeTableFollowUpDocs.COL_FLAG + "=1";
        Log.i("@Transworld", "Update flag " + sql1);
        sqDb.execSQL(sql);
        sqDb.execSQL(sql1);
        sqDb.close();
    }

    @Override
    public void deleteTableData() {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        sqDb.execSQL("delete from " + MeDatabase.MeTableFollowUp.TABLE_NAME);
        Log.i("@Transworld", "Deleted Record Successfully");
        sqDb.close();
    }

    @Override
    public Integer getFlag() throws Exception {
        Integer flag = 1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableFollowUp.TABLE_NAME, new String[]{MeDatabase.MeTableFollowUp.COL_FLAG}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            flag = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableFollowUp.COL_FLAG));
        }
        sqDb.close();
        return flag;
    }
}
