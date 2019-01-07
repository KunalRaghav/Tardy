package com.krsolutions.tardy.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class funtool {

    public static Subject DbtoObject(Cursor cursor){
        long id = (cursor.getLong(cursor.getColumnIndex(TardyContract.TardyEntry._ID)));
        String name = cursor.getString(cursor.getColumnIndex(TardyContract.TardyEntry.COLUMN_NAME_SUBJECT));
        int totalClasses = cursor.getInt(cursor.getColumnIndex(TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES));
        int classesAttended = cursor.getInt(cursor.getColumnIndex(TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED));
        Subject mSubject= new Subject(id,name,totalClasses,classesAttended);
        return mSubject;
    }
    public static float calcPerc(float x, float r){
        return x/r*100;
    }

    public static int bunkPossible(float attended, float total, float desired){
        float pos=0;
        pos=(attended-total*desired/100)/(desired/100);
        return (int)pos;
    }

    public static int classesToAttend(float attended, float total, float desired){
        float req=0;
        req=(desired/100*total-attended)/(1-desired/100);
        return (int)(req+0.5);
    }
    public static int upClass(Context context,String subjectName){
        TardyDbHelper dbHelper = new TardyDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TardyContract.TardyEntry.TABLE_NAME+
                " WHERE "+TardyContract.TardyEntry.COLUMN_NAME_SUBJECT+ "=?"
                ,new String[]{subjectName});
        cursor.moveToFirst();
        Subject mSubject = DbtoObject(cursor);
        db.close();
        db = dbHelper.getWritableDatabase();
        int newTotal = mSubject.getTotalClasses()+1;
        int newAttended = mSubject.getClassesAttended()+1;
        db.execSQL(updateDB(newAttended,newTotal,mSubject.getSubjectName()));
        return 0;
    }

    public static int missedClass(Context context,String subjectName){
        TardyDbHelper dbHelper = new TardyDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TardyContract.TardyEntry.TABLE_NAME+
                        " WHERE "+TardyContract.TardyEntry.COLUMN_NAME_SUBJECT+ "=?"
                ,new String[]{subjectName});
        cursor.moveToFirst();
        Subject mSubject = DbtoObject(cursor);
        db.close();
        db = dbHelper.getWritableDatabase();
        int newTotal = mSubject.getTotalClasses()+1;
        int newAttended = mSubject.getClassesAttended();
        db.execSQL(updateDB(newAttended,newTotal,mSubject.getSubjectName()));
        return 0;
    }

    public static String updateDB(int attend, int total, String subName){
        String string = "UPDATE "+TardyContract.TardyEntry.TABLE_NAME + " SET " +
                TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED + "= " + attend +
                ", " + TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES+"= " +total +
                " WHERE " + TardyContract.TardyEntry.COLUMN_NAME_SUBJECT+"= "+"\""+subName+"\""+";";
        return string;

    }
}
