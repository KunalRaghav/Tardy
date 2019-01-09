package com.krsolutions.tardy.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.krsolutions.tardy.data.Subject;
import com.krsolutions.tardy.data.TardyContract;
import com.krsolutions.tardy.data.TardyDbHelper;
import com.krsolutions.tardy.data.funtool;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class LoadTask extends AsyncTask<Void,Subject,Void> {

    private Context context;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TardyAdapter adapter;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private funtool.SORT sortOrder;

    private static final String TAG = "LoadTask";

    public LoadTask(Context context, ProgressBar progressBar, RecyclerView recyclerView) {
        this.context = context;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }

    public LoadTask(Context context, ProgressBar progressBar, RecyclerView recyclerView, funtool.SORT sortOrder) {
        this.context = context;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        this.sortOrder = sortOrder;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: attaching adapter of recyclerView");
        adapter = new TardyAdapter(subjects,context,progressBar,recyclerView);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onProgressUpdate(Subject... values) {
        subjects.add(values[0]);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        TardyDbHelper dbHelper = new TardyDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        if(sortOrder!=null) {
            switch (sortOrder) {
                case alpha:
                    cursor = db.rawQuery("SELECT * FROM " + TardyContract.TardyEntry.TABLE_NAME + " ORDER BY " + TardyContract.TardyEntry.COLUMN_NAME_SUBJECT, null);
                    break;
                case percent:
                    cursor = db.rawQuery("SELECT *, ("+ TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED+"*100.0/"+TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES +") as PERCENT FROM " + TardyContract.TardyEntry.TABLE_NAME + " ORDER BY PERCENT", null);
                    break;
                case classAttended:
                    cursor = db.rawQuery("SELECT * FROM " + TardyContract.TardyEntry.TABLE_NAME + " ORDER BY "+ TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED, null);
                    break;
                default:
                    cursor = db.rawQuery("SELECT * FROM " + TardyContract.TardyEntry.TABLE_NAME, null);
            }
        }else{
            cursor = db.rawQuery("SELECT * FROM " + TardyContract.TardyEntry.TABLE_NAME, null);
        }
        while (cursor.moveToNext()){
            Subject mSubject = funtool.DbtoObject(cursor);
            publishProgress(mSubject);
//            try {
//                Thread.sleep(500);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
        cursor.close();
        dbHelper.close();

        return null;

    }
}
