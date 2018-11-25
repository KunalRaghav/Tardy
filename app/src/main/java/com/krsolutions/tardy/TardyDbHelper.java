package com.krsolutions.tardy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TardyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tardy.db";


    public TardyDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_CREATE_ENTRIES);
        onCreate(db);
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TardyContract.TardyEntry.TABLE_NAME + " (" +
                    TardyContract.TardyEntry._ID + " INTEGER PRIMARY KEY, " +
                    TardyContract.TardyEntry.COLUMN_NAME_SUBJECT + " TEXT UNIQUE, " +
                    TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES + " INTEGER, " +
                    TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TardyContract.TardyEntry.TABLE_NAME;



}
