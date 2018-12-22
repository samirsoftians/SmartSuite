package com.transworldtechnology.crm.database.repository;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.domain.MeAddContact;
import com.transworldtechnology.crm.prefs.MePrefs;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by root on 27/1/16.
 */
public class MeRepoImplContactDet implements MeRepoContactDet {
    private MeHelper helper;
    private static Integer salesCustCode = null;
    private static Integer custCode = null;
    private static String contactPerson = null;

    public MeRepoImplContactDet(MeHelper helper) {
        this.helper=helper;
    }

    //Spoken To data coming from server
    @Override
    public void saveContacts(List<MeAddContact> addContactList) {
        if (addContactList != null) {
            SQLiteDatabase sqDb = helper.getWritableDatabase();
            //  sqDb.execSQL("delete from " + MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME);
            Log.i("@Transworld", "Map in Impl" + addContactList);
            for (MeAddContact addContact : addContactList) {
                ContentValues values = new ContentValues();
                values.put(MeDatabase.MeTableContactDet.COL_CONTACT_PERSON, addContact.getContactPerson());
                values.put(MeDatabase.MeTableContactDet.COL_CITY, addContact.getCity());
                values.put(MeDatabase.MeTableContactDet.COL_DESIGNATION, addContact.getDesignation());
                values.put(MeDatabase.MeTableContactDet.COL_ADDRESS, addContact.getAddress());
                values.put(MeDatabase.MeTableContactDet.COL_CUSTOMER_CODE, addContact.getCustomerCode());
                values.put(MeDatabase.MeTableContactDet.COL_CONTACT_ID, addContact.getContact_id());
                values.put(MeDatabase.MeTableContactDet.COL_SALES_CUSTOMER_CODE,addContact.getSalesCustomerCode());
                Log.i("@Transworld", "Insert Data " + values);
                values.put(MeDatabase.MeTableContactDet.COL_FLAG, 0);
                sqDb.insert(MeDatabase.MeTableContactDet.TABLE_NAME, null, values);
            }
            sqDb.close();
        }
    }



    @Override
    public void deleteTableData() {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        sqDb.execSQL("delete from " + MeDatabase.MeTableContactDet.TABLE_NAME);
        Log.i("@Transworld", "Deleted Record Successfully");
    }

    //save contacts locally
    @Override
    public void addContactsLocally(MeAddContact meAddContact) {
        if (meAddContact != null) {
            SQLiteDatabase sqDb = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MeDatabase.MeTableContactDet.COL_CONTACT_PERSON, meAddContact.getContactPerson()); //1
            values.put(MeDatabase.MeTableContactDet.COL_CITY, meAddContact.getCity());//2
            values.put(MeDatabase.MeTableContactDet.COL_DESIGNATION, meAddContact.getDesignation());
            values.put(MeDatabase.MeTableContactDet.COL_CUSTOMER_CODE, meAddContact.getCustomerCode());
            values.put(MeDatabase.MeTableContactDet.COL_CONTACT_ID, meAddContact.getContact_id());

            values.put(MeDatabase.MeTableContactDet.COL_COMPANY_NAME, meAddContact.getCompanyName());
            values.put(MeDatabase.MeTableContactDet.COL_COUNTRY, meAddContact.getCountry());
            values.put(MeDatabase.MeTableContactDet.COL_ALTERNET_NO, meAddContact.getAlternetNo());
            values.put(MeDatabase.MeTableContactDet.COL_ADDRESS, meAddContact.getAddress());
            values.put(MeDatabase.MeTableContactDet.COL_EMAIL, meAddContact.getEmail());

            values.put(MeDatabase.MeTableContactDet.COL_EMAIL_1, meAddContact.getEmail1());
            values.put(MeDatabase.MeTableContactDet.COL_EMAIL_2, meAddContact.getEmail2());
            values.put(MeDatabase.MeTableContactDet.COL_ENTRY_BY, meAddContact.getEntryBy());
            values.put(MeDatabase.MeTableContactDet.COL_FAX, meAddContact.getFax());
            values.put(MeDatabase.MeTableContactDet.COL_FIRST_NAME, meAddContact.getFirstName());
            values.put(MeDatabase.MeTableContactDet.COL_LAST_NAME, meAddContact.getLastName());
            values.put(MeDatabase.MeTableContactDet.COL_MIDDLE_NAME, meAddContact.getMiddleName());
            values.put(MeDatabase.MeTableContactDet.COL_MOBILE_NO, meAddContact.getMobileNo());
            values.put(MeDatabase.MeTableContactDet.COL_MOBILE_NO_1, meAddContact.getMobileno1());
            values.put(MeDatabase.MeTableContactDet.COL_MOBILE_NO_2, meAddContact.getMobileno2());
            values.put(MeDatabase.MeTableContactDet.COL_PHONE, meAddContact.getPhone());
            values.put(MeDatabase.MeTableContactDet.COL_SALES_CUSTOMER_CODE, meAddContact.getSalesCustomerCode());
            values.put(MeDatabase.MeTableContactDet.COL_SALUTATION, meAddContact.getSalutation());
            values.put(MeDatabase.MeTableContactDet.COL_STATE, meAddContact.getState());
            values.put(MeDatabase.MeTableContactDet.COL_ZIPCODE, meAddContact.getZipcode());
            values.put(MeDatabase.MeTableContactDet.COL_FLAG, 1);
            Log.i("@Transworld", "Insert Data " + values);
            sqDb.insert(MeDatabase.MeTableContactDet.TABLE_NAME, null, values);
        }
    }

    //upload contacts to server
    @Override
    public List<MeAddContact> uploadContactsToServer() throws Exception {
        List<MeAddContact> addContactList = new ArrayList<>();
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String checkCountSql = "select count(*) from " + MeDatabase.MeTableContactDet.TABLE_NAME +
                " where " + MeDatabase.MeTableContactDet.COL_FLAG + "= 1";
        Cursor cursorCount = sqDb.rawQuery(checkCountSql, null);
        if (cursorCount.moveToFirst()) {
            cursorCount.getInt(0);
            Log.i("@Transworld", "Count Flags : " + cursorCount.getInt(0));
        }
        if (cursorCount.getInt(0) > 0) {
            final String sql = "select " + MeDatabase.MeTableContactDet.COL_CUSTOMER_CODE + "," +
                    "" + MeDatabase.MeTableContactDet.COL_SALES_CUSTOMER_CODE + "," +
                    "" + MeDatabase.MeTableContactDet.COL_COMPANY_NAME + "," +
                    "" + MeDatabase.MeTableContactDet.COL_CONTACT_PERSON + "," +
                    "" + MeDatabase.MeTableContactDet.COL_SALUTATION + "," +
                    "" + MeDatabase.MeTableContactDet.COL_FIRST_NAME + "," +
                    "" + MeDatabase.MeTableContactDet.COL_MIDDLE_NAME + "," +
                    "" + MeDatabase.MeTableContactDet.COL_LAST_NAME + "," +
                    "" + MeDatabase.MeTableContactDet.COL_ADDRESS + "," +
                    "" + MeDatabase.MeTableContactDet.COL_CITY + "," +
                    "" + MeDatabase.MeTableContactDet.COL_ZIPCODE + "," +
                    "" + MeDatabase.MeTableContactDet.COL_DESIGNATION + "," +
                    "" + MeDatabase.MeTableContactDet.COL_MOBILE_NO + "," +
                    "" + MeDatabase.MeTableContactDet.COL_MOBILE_NO_1 + "," +
                    "" + MeDatabase.MeTableContactDet.COL_MOBILE_NO_2 + "," +
                    "" + MeDatabase.MeTableContactDet.COL_PHONE + "," +
                    "" + MeDatabase.MeTableContactDet.COL_ALTERNET_NO + "," +
                    "" + MeDatabase.MeTableContactDet.COL_EMAIL + "," +
                    "" + MeDatabase.MeTableContactDet.COL_EMAIL_1 + "," +
                    "" + MeDatabase.MeTableContactDet.COL_EMAIL_2 + "," +
                    "" + MeDatabase.MeTableContactDet.COL_FAX + "," +
                    "" + MeDatabase.MeTableContactDet.COL_STATE + "," +
                    "" + MeDatabase.MeTableContactDet.COL_COUNTRY + "," +
                    "" + MeDatabase.MeTableContactDet.COL_ENTRY_BY +
                    " from  " + MeDatabase.MeTableContactDet.TABLE_NAME +
                    " where " + MeDatabase.MeTableContactDet.COL_FLAG + " = 1";
            Cursor cursor = sqDb.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                MeAddContact addContact = new MeAddContact();
                addContact.setCompanyMasterId(MeRepoFactory.getLoginRepository(helper).getCompanyMasterId());
                addContact.setCustomerCode(cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_CUSTOMER_CODE)));
                addContact.setSalesCustomerCode(cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_SALES_CUSTOMER_CODE)));
                addContact.setCompanyName(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_COMPANY_NAME)));
                addContact.setContactPerson(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_CONTACT_PERSON)));
                addContact.setSalutation(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_SALUTATION)));
                addContact.setFirstName(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_FIRST_NAME)));
                addContact.setMiddleName(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_MIDDLE_NAME)));
                addContact.setLastName(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_LAST_NAME)));
                addContact.setAddress(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_ADDRESS)));
                addContact.setCity(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_CITY)));
                addContact.setZipcode(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_ZIPCODE)));
                addContact.setDesignation(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_DESIGNATION)));
                addContact.setMobileNo(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_MOBILE_NO)));
                addContact.setMobileno1(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_MOBILE_NO_1)));
                addContact.setMobileno2(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_MOBILE_NO_2)));
                addContact.setPhone(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_PHONE)));
                addContact.setAlternetNo(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_ALTERNET_NO)));
                addContact.setEmail(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_EMAIL)));
                addContact.setEmail1(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_EMAIL_1)));
                addContact.setEmail2(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_EMAIL_2)));
                addContact.setFax(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_FAX)));
                addContact.setState(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_STATE)));
                addContact.setCountry(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_COUNTRY)));
                addContact.setEntryBy(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_ENTRY_BY)));
                addContactList.add(addContact);
            }
            Log.i("@Transworld", "Local List Ready to Upload:  " + addContactList);
        }
        sqDb.close();
        return addContactList;
    }

    @Override
    public void updateFlag() throws Exception {
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String sql = "UPDATE " + MeDatabase.MeTableContactDet.TABLE_NAME +
                " SET " + MeDatabase.MeTableContactDet.COL_FLAG + " = 0  WHERE " +
                MeDatabase.MeTableContactDet.COL_FLAG + "=1";
        Log.i("@Transworld", "Update flag " + sql);
        sqDb.execSQL(sql);
        sqDb.close();
    }

    @Override
    public Integer checkDataContacts() {

        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + MeDatabase.MeTableContactDet.TABLE_NAME + " where " + MeDatabase.MeTableContactDet.COL_FLAG +" = 1" ;
        Cursor cursor= sqDb.rawQuery(countQuery ,null);
        int cnt = cursor.getCount();
        Log.i("@Transworld","COunt "+cnt + " ," +countQuery);
        cursor.close();
        return cnt;

    }

    @Override
    public List<String> getContactPerson() throws Exception {
        List<String> contactPersonlList = new ArrayList<>();
        Cursor cursor;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Log.i("@Transworld","SaleCustCode : "+salesCustCode + " , " +"CustCode : "+custCode);
        if (salesCustCode == 0) {
            cursor = sqDb.rawQuery("select " + MeDatabase.MeTableContactDet.COL_CONTACT_PERSON + " from " + MeDatabase.MeTableContactDet.TABLE_NAME + " where " + MeDatabase.MeTableContactDet.COL_CUSTOMER_CODE + " = " + custCode +" or " + MeDatabase.MeTableContactDet.COL_FLAG + " = 1" , null);
        }
        else {
            cursor = sqDb.rawQuery("select " + MeDatabase.MeTableContactDet.COL_CONTACT_PERSON + " from " + MeDatabase.MeTableContactDet.TABLE_NAME+ " where " + MeDatabase.MeTableContactDet.COL_SALES_CUSTOMER_CODE + " = " + salesCustCode + " or " + MeDatabase.MeTableContactDet.COL_FLAG + " =1", null);
        }
        //    Cursor cursor = sqDb.query(MeDatabase.MeTableContactDet.TABLE_NAME, new String[]{MeDatabase.MeTableContactDet.COL_CONTACT_PERSON}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            contactPersonlList.add(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_CONTACT_PERSON)));
        }
        sqDb.close();
        Log.i("@Transworld", "Contact Person  Infor " + contactPersonlList);
        return contactPersonlList;
    }

    @Override
    public void saveCode(Integer salesCustCode,Integer custCode){
        this.custCode = custCode;
        this.salesCustCode = salesCustCode;
    }

    public void saveContactPerson(String contactPerson){
        this.contactPerson = contactPerson;
    }

    @Override
    public String getCity() throws Exception {
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String city = null;

        Cursor cursor = sqDb.query(
                MeDatabase.MeTableContactDet.TABLE_NAME,
                new String[]{MeDatabase.MeTableContactDet.COL_CITY},
                MeDatabase.MeTableContactDet.COL_CONTACT_PERSON + " LIKE '%" + contactPerson + "%'",
                null, null, null, null, null);

        while (cursor.moveToNext()){
            city = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_CITY));

        }
        Log.i("@Transworld","City is "+city);
        return city;
    }

    @Override
    public String getDesignation() throws Exception {
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String designation = null;
        Cursor cursor =  sqDb.query(
                MeDatabase.MeTableContactDet.TABLE_NAME,
                new String[]{MeDatabase.MeTableContactDet.COL_DESIGNATION},
                MeDatabase.MeTableContactDet.COL_CONTACT_PERSON + " LIKE '%" + contactPerson + "%'",
                null, null, null, null, null);
        while (cursor.moveToNext()){
            designation = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_DESIGNATION));
        }
        return designation;
    }

    @Override
    public String getAddress() throws Exception {
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String address = null;
        Cursor cursor =  sqDb.query(
                MeDatabase.MeTableContactDet.TABLE_NAME,
                new String[]{MeDatabase.MeTableContactDet.COL_ADDRESS},
                MeDatabase.MeTableContactDet.COL_CONTACT_PERSON + " LIKE '%" + contactPerson + "%'",
                null, null, null, null, null);
        while (cursor.moveToNext()){
            address = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_ADDRESS));
        }
        return address;
    }

    @Override
    public Integer getFlag() throws Exception {
        Integer flag = 1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableContactDet.TABLE_NAME, new String[]{MeDatabase.MeTableContactDet.COL_FLAG}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            flag = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_FLAG));
        }
        sqDb.close();
        return flag;
    }

    @Override
    public Integer getContactId() throws Exception {
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Integer contactId = null;
        Cursor cursor =  sqDb.query(
                MeDatabase.MeTableContactDet.TABLE_NAME,
                new String[]{MeDatabase.MeTableContactDet.COL_CONTACT_ID},
                MeDatabase.MeTableContactDet.COL_CONTACT_PERSON + " LIKE '%" + contactPerson + "%'",
                null, null, null, null, null);
        while (cursor.moveToNext()){
            contactId = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableContactDet.COL_CONTACT_ID));
        }
        return contactId;
    }
}
