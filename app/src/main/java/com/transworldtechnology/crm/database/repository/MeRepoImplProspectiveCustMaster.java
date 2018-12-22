package com.transworldtechnology.crm.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.domain.MeProspectiveCustMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 18/2/16.
 */
public class MeRepoImplProspectiveCustMaster implements MeRepoProspectiveCustMaster {
    private static String companyName = null;
    private MeHelper helper;

    public MeRepoImplProspectiveCustMaster(MeHelper helper) {
        this.helper = helper;
    }

    @Override
    public void saveListCompanies(List<MeProspectiveCustMaster> map) {
        if (map != null) {
            SQLiteDatabase sqDb = helper.getWritableDatabase();
            //  sqDb.execSQL("delete from " + MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME);
            Log.i("@Transworld", "Map in Impl" + map);
            for (MeProspectiveCustMaster custMaster : map) {
                ContentValues values = new ContentValues();
                values.put(MeDatabase.MeTableProspectiveCustMaster.COL_OPPORTUNITY_CODE, custMaster.getOpportunityCode());
                values.put(MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME, custMaster.getCompanyName());
                values.put(MeDatabase.MeTableProspectiveCustMaster.COL_CUSTOMER_CODE, custMaster.getCustomerCode());
                values.put(MeDatabase.MeTableProspectiveCustMaster.COL_SALES_CUSTOMER_CODE, custMaster.getSalesCustomerCode());
                values.put(MeDatabase.MeTableProspectiveCustMaster.COL_FLAG, 1);
                Log.i("@Transworld", "Insert Data " + values);
                //  sqDb.beginTransaction();
                sqDb.insert(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME, null, values);
                // sqDb.endTransaction();
                //sqDb.setTransactionSuccessful();
            }
            sqDb.close();
        }
    }

    @Override
    public void deleteTableData() {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        sqDb.execSQL("delete from " + MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME);
        Log.i("@Transworld", "Deleted Record Successfully");
    }

    @Override
    public void saveCompanyName(String companyName) throws Exception {
        if (companyName != null) {
            this.companyName = companyName;
            Log.i("@Transworld", "Save Str : " + companyName);
        }
    }

    @Override
    public Integer getOpportunityCode() throws Exception {
        Integer opportunityCode = -1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Log.i("@Transworld", "in opp code" + companyName);
        //    db.query("sku_table", columns, "owner=?", new String[] { owner }, null, null, null);
        Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME,
                new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_OPPORTUNITY_CODE},
                MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME + "=?", new String[]{companyName}, null, null, null);
        //   Cursor cursor = sqDb.rawQuery("select " + MeDatabase.MeTableProspectiveCustMaster.COL_OPPORTUNITY_CODE + " from " +
        //         MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME + " where " + MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME + " = " + companyNameLocal, null);
        // Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME, new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_OPPORTUNITY_CODE}, null, null, null, null, null);
        Log.i("@Transworld", "Cursor" + DatabaseUtils.dumpCursorToString(cursor));
        while (cursor.moveToNext()) {
            opportunityCode = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_OPPORTUNITY_CODE));
        }
        sqDb.close();
        return opportunityCode;
    }

    @Override
    public List<String> getCompanyInfo() throws Exception {
        List<String> list = new ArrayList<>();
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(
                MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME,
                new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME,
                        MeDatabase.MeTableProspectiveCustMaster.COL_OPPORTUNITY_CODE,
                        MeDatabase.MeTableProspectiveCustMaster.COL_CUSTOMER_CODE,
                        MeDatabase.MeTableProspectiveCustMaster.COL_SALES_CUSTOMER_CODE},
                MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME + " LIKE '%" + companyName + "%'",
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME))
                    + ", " + cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_OPPORTUNITY_CODE))
                    + ", " + cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_CUSTOMER_CODE))
                    + ", " + cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_SALES_CUSTOMER_CODE)));
        }
        sqDb.close();
        Log.i("@Transworld", "COmpany Infor " + list);
        return list;
    }

    @Override
    public String getCompanyName() throws Exception {
        String companyName1 = companyName;
       /* SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.rawQuery("select " + MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME + " from " + MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME, null);
        //Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME, new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            companyName = cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME));
        }
        sqDb.close();*/
        Log.i("@Transworld", "companyName " + companyName);
        return companyName1;
    }

    @Override
    public Integer getCustomerCode() throws Exception {
        Integer customerCode = -1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME,
                new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_CUSTOMER_CODE},
                MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME + "=?", new String[]{companyName}, null, null, null);
        //    Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME, new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_CUSTOMER_CODE}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            customerCode = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_CUSTOMER_CODE));
        }
        sqDb.close();
        return customerCode;
    }

    @Override
    public Integer getSalesCustomerCode() throws Exception {
        Integer salesCustomerCode = -1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME,
                new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_SALES_CUSTOMER_CODE},
                MeDatabase.MeTableProspectiveCustMaster.COL_COMPANY_NAME + "=?", new String[]{companyName}, null, null, null);
        // Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME, new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_SALES_CUSTOMER_CODE}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            salesCustomerCode = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_SALES_CUSTOMER_CODE));
        }
        sqDb.close();
        return salesCustomerCode;
    }

    @Override
    public Integer getFlag() throws Exception {
        Integer flag = 1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.query(MeDatabase.MeTableProspectiveCustMaster.TABLE_NAME, new String[]{MeDatabase.MeTableProspectiveCustMaster.COL_FLAG}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            flag = cursor.getInt(cursor.getColumnIndex(MeDatabase.MeTableProspectiveCustMaster.COL_FLAG));
        }
        sqDb.close();
        return flag;
    }
}
