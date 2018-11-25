package com.krsolutions.tardy;

import android.database.Cursor;

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
}
