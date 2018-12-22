package com.transworldtechnology.crm.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;

import java.util.Map;


/**
 * Created by root on 5/4/16.
 */
public class MeRepoImplFollowUpDocs implements MeRepoFollowUpDocs {
    private MeHelper helper;

    public MeRepoImplFollowUpDocs(MeHelper helper) {
        this.helper = helper;
    }

    @Override
    public void saveFollowUpDocsLocal(Map<String, Object> mapEntity) throws Exception {
        ContentValues values = new ContentValues();
        SQLiteDatabase sqDb = helper.getWritableDatabase();
        Log.i("@Transworld", "Max Sr No " + getMaxSrNo());
        if (mapEntity != null) {
            values.put(MeDatabase.MeTableFollowUpDocs.COL_SR_NO, getMaxSrNo());
            values.put(MeDatabase.MeTableFollowUpDocs.COL_DOC_COUNT, (Integer) mapEntity.get("docCount"));
            values.put(MeDatabase.MeTableFollowUpDocs.COL_FOLLOW_UP_DATE, (String) mapEntity.get("followUpDate"));
            values.put(MeDatabase.MeTableFollowUpDocs.COL_FOLLOW_UP_TIME, (String) mapEntity.get("followUpTime"));
            values.put(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_1, (String) mapEntity.get("docPath1"));
            values.put(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_2, (String) mapEntity.get("docPath2"));
            values.put(MeDatabase.MeTableFollowUpDocs.COL_DOC_PATH_3, (String) mapEntity.get("docPath3"));
            values.put(MeDatabase.MeTableFollowUpDocs.COL_FLAG, 1);//Data stored to sqlite only
            Log.i("@Transworld", "Insert Data Files" + values);
            sqDb.insert(MeDatabase.MeTableFollowUpDocs.TABLE_NAME, null, values);
        }
    }

    @Override
    public int getMaxSrNo() {
        int maxSrNo = -1;
        SQLiteDatabase sqDb = helper.getReadableDatabase();
        String maxSrNoStr = "select max (" + MeDatabase.MeTableFollowUp.COL_SR_NO + ") from " + MeDatabase.MeTableFollowUp.TABLE_NAME;
        Cursor cursor = sqDb.rawQuery(maxSrNoStr, null);
        if (cursor.moveToFirst()) {
            maxSrNo = cursor.getInt(0);
            return maxSrNo;
        }
        return maxSrNo;
    }


}
