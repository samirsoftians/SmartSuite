package com.transworldtechnology.crm.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 22/1/16.
 */
public class MeRepoImplLogin implements MeRepoLogin {

    private MeHelper helper;

    public MeRepoImplLogin(MeHelper helper) {
        this.helper = helper;
    }

    @Override
    public void saveUserCrediantials(String userName, String password, Integer employeeCode, Integer companyMasterId, String role, Integer userTypeCode, String activeStatus, String userLevel, String companyCode, String empEmailId,String imei,String empName) throws Exception {

        SQLiteDatabase sqDb = helper.getWritableDatabase();

        sqDb.execSQL("delete from " + MeDatabase.MeTableLogin.TABLE_NAME);
        sqDb.execSQL("delete from " + MeDatabase.MeTableFollowUp.TABLE_NAME);
        sqDb.execSQL("delete from " + MeDatabase.MeTableContactDet.TABLE_NAME);
        sqDb.execSQL("delete from " + MeDatabase.MeTableMarketingRepMaster.TABLE_NAME);
        sqDb.execSQL("delete from " + MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME);
        sqDb.execSQL("delete from " + MeDatabase.MeTableUserTracker.TABLE_NAME);


        ContentValues values = new ContentValues();
        values.put(MeDatabase.MeTableLogin.COL_USER_NAME, userName);
        values.put(MeDatabase.MeTableLogin.COL_PASSWORD, password);
        values.put(MeDatabase.MeTableLogin.COL_EMPLOYEE_CODE, employeeCode);
        values.put(MeDatabase.MeTableLogin.COL_COMPANY_MASTER_ID, companyMasterId);
        values.put(MeDatabase.MeTableLogin.COL_ROLE, role);
        values.put(MeDatabase.MeTableLogin.COL_USER_TYPE_CODE, userTypeCode);
        values.put(MeDatabase.MeTableLogin.COL_ACTIVE_STATUS, activeStatus);
        values.put(MeDatabase.MeTableLogin.COL_USER_LEVEL, userLevel);
        values.put(MeDatabase.MeTableLogin.COL_COMPANY_CODE, companyCode);
        values.put(MeDatabase.MeTableLogin.COL_EMP_EMAIL_ID,empEmailId);
        values.put(MeDatabase.MeTableLogin.COL_IMEI,imei);
        values.put(MeDatabase.MeTableLogin.COL_EMPLOYEE_NAME,empName);
        values.put(MeDatabase.MeTableLogin.COL_FLAG, 1); //user details save after login user


        sqDb.insert(MeDatabase.MeTableLogin.TABLE_NAME, null, values);
        sqDb.close();
    }

    @Override
    public void saveSpecialEmails(List<Map<String, Object>> specialEmailsList) {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        for (Map<String, Object> specialEmails : specialEmailsList) {
            ContentValues values = new ContentValues();
            values.put(MeDatabase.MeTableLogin.COL_EMP_EMAIL_ID, (String) specialEmails.get("email"));
            values.put(MeDatabase.MeTableLogin.COL_EMPLOYEE_NAME, (String) specialEmails.get("empname"));
            values.put(MeDatabase.MeTableLogin.COL_USER_LEVEL, (String) specialEmails.get("userlevel"));
            values.put(MeDatabase.MeTableLogin.COL_EMPLOYEE_CODE, (Integer) specialEmails.get("empcode"));
            values.put(MeDatabase.MeTableLogin.COL_FLAG, 0);//user details saved for SpecialEmails purpose
            sqDb.insert(MeDatabase.MeTableLogin.TABLE_NAME, null, values);
            Log.i("@Transworld", "Special Email Saved" + values);
        }
        sqDb.close();
    }

    @Override
    public List<String> getEmployeeNames() throws Exception {
        List<String> empNameList = new ArrayList<>();
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME,
                new String[]{MeDatabase.MeTableLogin.COL_EMPLOYEE_NAME},
                MeDatabase.MeTableLogin.COL_FLAG + "=0", new String[]{}, null, null, null);
        // Cursor cursor = sqDb.rawQuery("select " + MeDatabase.MeTableLogin.COL_EMPLOYEE_NAME + " from " + MeDatabase.MeTableLogin.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            empNameList.add(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_EMPLOYEE_NAME)));
        }
        sqDb.close();
        Log.i("@Transworld", "Employee Names " + empNameList);
        return empNameList;
    }

    @Override
    public String getEmpName() throws Exception {
        String empName = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_EMPLOYEE_NAME}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            empName = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_EMPLOYEE_NAME));
        }
        sqDb.close();

        return empName;
    }

    @Override
    public String getUserName() throws Exception {

        String userName = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_USER_NAME}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            userName = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_USER_NAME));
        }
        sqDb.close();

        return userName;
    }

    @Override
    public String getPassword() throws Exception {

        String password = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_PASSWORD}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            password = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_PASSWORD));
        }
        sqDb.close();

        return password;

    }

    @Override
    public Integer getEmployeeCode() throws Exception {

        Integer empCode = -1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_EMPLOYEE_CODE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            empCode = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_EMPLOYEE_CODE));
        }
        sqDb.close();


        return empCode;
    }

    @Override
    public Integer getCompanyMasterId() throws Exception {
        Integer companyMasterId = -1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_COMPANY_MASTER_ID}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            companyMasterId = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_COMPANY_MASTER_ID));
        }
        sqDb.close();


        return companyMasterId;
    }

    @Override
    public String getRole() throws Exception {
        String role = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_ROLE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            role = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_ROLE));
        }
        sqDb.close();


        return role;
    }

    @Override
    public Integer getUserTypeCode() throws Exception {
        Integer userTypeCode = -1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_USER_TYPE_CODE}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            userTypeCode = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_USER_TYPE_CODE));
        }
        sqDb.close();
        return userTypeCode;
    }

    @Override
    public String getActiveStatus() throws Exception {

        String activeStatus = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();

        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_ACTIVE_STATUS}, null, null, null, null, null);
        if (cursor.moveToNext()) {

            activeStatus = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_ACTIVE_STATUS));
        }
        sqDb.close();


        return activeStatus;
    }

    @Override
    public String getUserLevel() throws Exception {
        String userLevel = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_USER_LEVEL}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            userLevel = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_USER_LEVEL));
        }
        sqDb.close();
        return userLevel;
    }

    @Override
    public String getImei() throws Exception {
        String imei = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_IMEI}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            imei = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_IMEI));
        }
        sqDb.close();
        return imei;
    }

    @Override
    public String getCompanyCode() throws Exception {
        String companyCode = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_COMPANY_CODE}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            companyCode = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_COMPANY_CODE));
        }
        sqDb.close();
        return companyCode;
    }

    @Override
    public String getEmpEmailId() throws Exception {
        String empEmailId = "";
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_EMP_EMAIL_ID}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            empEmailId = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_EMP_EMAIL_ID));
        }
        sqDb.close();
        return empEmailId;
    }

    @Override
    public Integer getFlag() throws Exception {
        Integer flag = 1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableLogin.TABLE_NAME, new String[]{MeDatabase.MeTableLogin.COL_FLAG}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            flag = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableLogin.COL_FLAG));
        }
        sqDb.close();
        return flag;
    }


}
