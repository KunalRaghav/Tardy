package com.krsolutions.tardy.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.krsolutions.tardy.data.DateContract;
import com.krsolutions.tardy.data.HistoryRecord;
import com.krsolutions.tardy.data.TardyContract;
import com.krsolutions.tardy.data.TardyDbHelper;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class LoadTaskTimeline extends AsyncTask<Void, HistoryRecord,Void> {
    private Context mContext;
    private ProgressBar mProgressbar;
    private RecyclerView mRecyclerView;
    private TimelineAdapter adapter;
    private ArrayList<HistoryRecord> historyRecords = new ArrayList<>();
    private long SubjectID = -1;
    private static final String TAG = "LoadTaskTimeline";
    private String lowerDateBound;
    private String upperDateBound;


    public LoadTaskTimeline(Context context, ProgressBar progressBar, RecyclerView recyclerView) {
        mContext = context;
        mProgressbar = progressBar;
        mRecyclerView = recyclerView;
    }

    public LoadTaskTimeline(Context context, ProgressBar progressBar, RecyclerView recyclerView, long id) {
        mContext = context;
        mProgressbar = progressBar;
        mRecyclerView = recyclerView;
        SubjectID = id;
        Log.d(TAG, "LoadTaskTimeline: SubjectID: " + SubjectID);
    }
    public LoadTaskTimeline(Context context, ProgressBar progressBar, RecyclerView recyclerView, String from, String to) {
        mContext = context;
        mProgressbar = progressBar;
        mRecyclerView = recyclerView;
        lowerDateBound = from;
        upperDateBound = to;
        Log.d(TAG, "LoadTaskTimeline: SubjectID: " + SubjectID);
    }
    public LoadTaskTimeline(Context context, ProgressBar progressBar, RecyclerView recyclerView, long id, String from, String to) {
        mContext = context;
        mProgressbar = progressBar;
        mRecyclerView = recyclerView;
        SubjectID = id;
        lowerDateBound = from;
        upperDateBound = to;
        Log.d(TAG, "LoadTaskTimeline: SubjectID: " + SubjectID);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        adapter = new TimelineAdapter(historyRecords, mContext);
        mRecyclerView.setAdapter(adapter);
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    protected void onProgressUpdate(HistoryRecord... values) {
        historyRecords.add(values[0]);
        adapter.notifyItemInserted(historyRecords.size() - 1);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        TardyDbHelper dbHelper = new TardyDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        if (SubjectID == -1) {
            if(lowerDateBound==null) {
                cursor = db.rawQuery(SQL_SELECTOR, null);
            }else {
                cursor = db.rawQuery(SQL_SELECTOR_BOUNDS_GIVEN(lowerDateBound,upperDateBound),null);
            }
        } else {
            if(lowerDateBound==null) {
                cursor = db.rawQuery(SQL_SELECTOR_ID_GIVEN(SubjectID), null);
                Log.d(TAG, "doInBackground: History:" + SQL_SELECTOR_ID_GIVEN(SubjectID));
            }else {
                cursor = db.rawQuery(SQL_SELECTOR_ID_GIVEN_BOUNDS_GIVEN(SubjectID,lowerDateBound,upperDateBound), null);
                Log.d(TAG, "doInBackground: History: "+SQL_SELECTOR_ID_GIVEN_BOUNDS_GIVEN(SubjectID,lowerDateBound,upperDateBound));
            }

        }
        try {
            Thread.sleep(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (cursor.moveToNext()) {
            int recordID = cursor.getInt(cursor.getColumnIndex(DateContract.DateEntry._ID));
            String EntryTime = cursor.getString(cursor.getColumnIndex(DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME));
            int index = cursor.getColumnIndex(DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE);
            int EntryType = cursor.getInt(index);
            int SubjectID = cursor.getInt(cursor.getColumnIndex(DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID));
            String SubjectName = cursor.getString(cursor.getColumnIndex(TardyContract.TardyEntry.COLUMN_NAME_SUBJECT));
            HistoryRecord historyRecord = new HistoryRecord(recordID, EntryType, EntryTime, SubjectID, SubjectName);
            publishProgress(historyRecord);

        }
        cursor.close();
        db.close();
        dbHelper.close();
        return null;
    }

    public static final String SQL_SELECTOR = "SELECT " + DateContract.DateEntry.TABLE_NAME + "." + DateContract.DateEntry._ID + ", " +
            DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + ", " +
            DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE + ", " +
            DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + ", " +
            TardyContract.TardyEntry.COLUMN_NAME_SUBJECT + " FROM " +
            DateContract.DateEntry.TABLE_NAME + ", " + TardyContract.TardyEntry.TABLE_NAME +
            " WHERE " + DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + " = " + TardyContract.TardyEntry.TABLE_NAME + "." + TardyContract.TardyEntry._ID + " ORDER BY " +
            DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + " DESC " + ";";

    public String SQL_SELECTOR_ID_GIVEN(long sub) {
        return "SELECT " + DateContract.DateEntry.TABLE_NAME + "." + DateContract.DateEntry._ID + ", " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + ", " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE + ", " +
                DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + ", " +
                TardyContract.TardyEntry.COLUMN_NAME_SUBJECT + " FROM " +
                DateContract.DateEntry.TABLE_NAME + ", " + TardyContract.TardyEntry.TABLE_NAME +
                " WHERE " + DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + " = " + sub +
                " AND " + DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + " = " + TardyContract.TardyEntry.TABLE_NAME + "." + TardyContract.TardyEntry._ID + " ORDER BY " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + " DESC " + ";";
    }
    public String SQL_SELECTOR_BOUNDS_GIVEN(String from, String to) {
        return "SELECT " + DateContract.DateEntry.TABLE_NAME + "." + DateContract.DateEntry._ID + ", " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + ", " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE + ", " +
                DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + ", " +
                TardyContract.TardyEntry.COLUMN_NAME_SUBJECT + " FROM " +
                DateContract.DateEntry.TABLE_NAME + ", " + TardyContract.TardyEntry.TABLE_NAME +
                " WHERE " + DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + " = " + TardyContract.TardyEntry.TABLE_NAME + "." + TardyContract.TardyEntry._ID +
                " AND " + DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + " BETWEEN \"" + from + "\" AND \"" + to +
                "\" ORDER BY " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + " DESC " + ";";
    }
    public String SQL_SELECTOR_ID_GIVEN_BOUNDS_GIVEN(long sub, String from, String to) {
        return "SELECT " + DateContract.DateEntry.TABLE_NAME + "." + DateContract.DateEntry._ID + ", " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + ", " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE + ", " +
                DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + ", " +
                TardyContract.TardyEntry.COLUMN_NAME_SUBJECT + " FROM " +
                DateContract.DateEntry.TABLE_NAME + ", " + TardyContract.TardyEntry.TABLE_NAME +
                " WHERE " + DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + " = " + sub +
                " AND " + DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID + " = " + TardyContract.TardyEntry.TABLE_NAME + "." + TardyContract.TardyEntry._ID +
                " AND " + DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + " BETWEEN \"" + from + "\" AND \"" + to +
                "\" ORDER BY " +
                DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME + " DESC " + ";";
    }
}