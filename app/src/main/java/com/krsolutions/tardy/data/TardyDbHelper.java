package com.krsolutions.tardy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TardyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Tardy.db";


    public TardyDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TARDY);
        db.execSQL(SQL_CREATE_TABLE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL(SQL_CREATE_TABLE_ENTRY);
                break;
        }

    }

    public static final String SQL_CREATE_TABLE_TARDY =
            "CREATE TABLE " + TardyContract.TardyEntry.TABLE_NAME + " (" +
                    TardyContract.TardyEntry._ID + " INTEGER PRIMARY KEY, " +
                    TardyContract.TardyEntry.COLUMN_NAME_SUBJECT + " TEXT NOT NULL UNIQUE, " +
                    TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES + " INTEGER, " +
                    TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED + " INTEGER, "+
                    TardyContract.TardyEntry.COLUMN_NAME_DESIRED_PERCENTAGE + " INTEGER)";

    public static final String SQL_DELETE_TABLE_TARDY =
            "DROP TABLE IF EXISTS " + TardyContract.TardyEntry.TABLE_NAME;


    public static final String SQL_CREATE_TABLE_ENTRY =
            " CREATE TABLE " + DateContract.DateEntry.TABLE_NAME + " (" +
                    DateContract.DateEntry._ID + " INTEGER PRIMARY KEY, " +
                    DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + " DATETIME NOT NULL, "+
                    DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE + " INT NOT NULL, " +
                    DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + " INT NOT NULL, " +
                    "FOREIGN KEY(" + DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID +") REFERENCES "+ TardyContract.TardyEntry.TABLE_NAME + "("+ TardyContract.TardyEntry._ID +")" +
                    ");"
            ;
    public static final String SQL_DELETE_TABLE_ENTRY = "DROP TABLE IF EXISTS " + DateContract.DateEntry.TABLE_NAME +";";
}
