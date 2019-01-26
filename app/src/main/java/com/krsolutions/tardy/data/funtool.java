package com.krsolutions.tardy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static Subject upClass(Context context,String subjectName){
        TardyDbHelper tardyDbHelper = new TardyDbHelper(context);
        SQLiteDatabase tardyDB = tardyDbHelper.getReadableDatabase();
        Cursor cursor = tardyDB.rawQuery("SELECT * FROM "+TardyContract.TardyEntry.TABLE_NAME+
                " WHERE "+TardyContract.TardyEntry.COLUMN_NAME_SUBJECT+ "=?"
                ,new String[]{subjectName});
        cursor.moveToFirst();
        Subject mSubject = DbtoObject(cursor);
        tardyDB.close();
        tardyDB = tardyDbHelper.getWritableDatabase();
        int newTotal = mSubject.getTotalClasses()+1;
        int newAttended = mSubject.getClassesAttended()+1;
        tardyDB.execSQL(updateTardyDB(newAttended,newTotal,mSubject.getSubjectName()));

        ContentValues cv = new ContentValues();
        cv.put(DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        cv.put(DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE,1);
        cv.put(DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID,mSubject.getSubjectID());
        tardyDB.insertOrThrow(DateContract.DateEntry.TABLE_NAME,null,cv);
        tardyDB.close();
        tardyDbHelper.close();
        return new Subject(mSubject.subjectID,mSubject.SubjectName,mSubject.TotalClasses+1,mSubject.ClassesAttended+1);
    }

    public static Subject missedClass(Context context,String subjectName){
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
        db.execSQL(updateTardyDB(newAttended,newTotal,mSubject.getSubjectName()));
        ContentValues cv = new ContentValues();
        cv.put(DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        cv.put(DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE,-1);
        cv.put(DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID,mSubject.getSubjectID());
        db.insertOrThrow(DateContract.DateEntry.TABLE_NAME,null,cv);
        db.close();
        dbHelper.close();
        return new Subject(mSubject.subjectID,mSubject.SubjectName,mSubject.TotalClasses+1,mSubject.ClassesAttended);
    }

    public static String updateTardyDB(int attend, int total, String subName){
        String string = "UPDATE "+TardyContract.TardyEntry.TABLE_NAME + " SET " +
                TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED + "= " + attend +
                ", " + TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES+"= " +total +
                " WHERE " + TardyContract.TardyEntry.COLUMN_NAME_SUBJECT+"= "+"\""+subName+"\""+";";
        return string;

    }

    public static String getWeekDay(int num){
        switch (num){
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "Chutti";
        }
    }

     public enum SORT{
        alpha,
        percent,
        classAttended
    }
}
