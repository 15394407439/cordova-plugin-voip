package com.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class CallPhoneHelper extends SQLiteOpenHelper {

    public CallPhoneHelper(Context context) {
        super(context, "call_phone_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table call_phone (_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,name varchar,phoneNum varchar,time long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
