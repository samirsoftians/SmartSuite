package com.transworldtechnology.crm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by root on 22/1/16.
 */
public class MeHelper extends SQLiteOpenHelper {

    public MeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL(MeDatabase.MeTableLogin.createTable());
        db.execSQL(MeDatabase.MeTableFollowUp.createTable());
        db.execSQL(MeDatabase.MeTableProspectiveCustMaster.createTable());
        db.execSQL(MeDatabase.MeTableContactDet.createTable());
        db.execSQL(MeDatabase.MeTableFollowUpDocs.createTable());
        db.execSQL(MeDatabase.MeTableMarketingRepMaster.createTable());
        db.execSQL(MeDatabase.MeTableUserTracker.createTable());
        db.execSQL(MeDatabase.TableCheckIn.createTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean updateNameStatus(String status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);


        String strSQL = "UPDATE tab_usertracker SET status='updated' WHERE status='pending'";
        Log.e("MeHelper",strSQL);

        db.execSQL(strSQL);

        db.close();
        return true;
    }
}
