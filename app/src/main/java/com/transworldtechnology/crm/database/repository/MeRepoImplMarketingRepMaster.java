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
 * Created by root on 4/4/16.
 */
public class MeRepoImplMarketingRepMaster implements MeRepoMarketingRepMaster {
    private MeHelper helper;

    public MeRepoImplMarketingRepMaster(MeHelper helper) {
        this.helper = helper;
    }

    @Override
    public void saveAssignToLocally(List<Map<String, Object>> listAssignTo) throws Exception {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        Log.i("@Transworld", "Assign To in Impl " + listAssignTo);
        for (Map<String, Object> mapEntity : listAssignTo) {
            ContentValues values = new ContentValues();
            values.put(MeDatabase.MeTableMarketingRepMaster.COL_MARKETING_REP_CODE, "" + mapEntity.get("MarketingRepCode"));
            values.put(MeDatabase.MeTableMarketingRepMaster.COL_MARKETING_REP_NAME, "" + mapEntity.get("MarketingRepName"));
            sqDb.insert(MeDatabase.MeTableMarketingRepMaster.TABLE_NAME, null, values);
        }
        Log.i("@Transworld", "Data Saved Locally Assign To");
        sqDb.close();
    }

    @Override
    public void deleteTableData() throws Exception {
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        sqDb.execSQL("delete from " + MeDatabase.MeTableMarketingRepMaster.TABLE_NAME);
        Log.i("@Transworld", "Deleted Record Successfully");
    }

    @Override
    public List<String> getAssignToList() throws Exception {
        List<String> assignToList = new ArrayList<>();
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        Cursor cursor = sqDb.rawQuery("select " + MeDatabase.MeTableMarketingRepMaster.COL_MARKETING_REP_NAME + " from " + MeDatabase.MeTableMarketingRepMaster.TABLE_NAME, null);
        //    Cursor cursor = sqDb.query(MeDatabase.MeTableContactDet.TABLE_NAME, new String[]{MeDatabase.MeTableContactDet.COL_CONTACT_PERSON}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            assignToList.add(cursor.getString(cursor.getColumnIndex(MeDatabase.MeTableMarketingRepMaster.COL_MARKETING_REP_NAME)));
        }
        sqDb.close();
        Log.i("@Transworld", "Assign To Infor " + assignToList);
        return assignToList;
    }
}
