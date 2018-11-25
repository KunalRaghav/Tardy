package com.krsolutions.tardy;

import android.provider.BaseColumns;

public final class TardyContract {

    private TardyContract(){}

    public class TardyEntry implements BaseColumns{
        public static final String TABLE_NAME = "tardy";
        public static final String COLUMN_NAME_SUBJECT = "subject";
        public static final String COLUMN_NAME_CLASSES_ATTENDED="classesAttended";
        public static final String COLUMN_NAME_TOTAL_CLASSES="totalClasses";
    }
}