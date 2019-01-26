package com.krsolutions.tardy.data;

import android.provider.BaseColumns;

public class DateContract {
    private DateContract(){}

    public class DateEntry implements BaseColumns{
        public static final String TABLE_NAME="entryTable";
        public static final String COLUMN_NAME_ENTRY_TIME="entryTime";
        public static final String COLUMN_NAME_ENTRY_TYPE="entryType";
        public static final String COLUMN_NAME_TARDY_SUBJECT_ID="tardySubjectID";

    }

}
