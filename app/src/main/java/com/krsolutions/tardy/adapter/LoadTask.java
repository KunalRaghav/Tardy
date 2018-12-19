package com.krsolutions.tardy.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

    public LoadTask(Context context, ProgressBar progressBar, RecyclerView recyclerView) {
        this.context = context;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute() {
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
        Cursor cursor = db.rawQuery("SELECT * FROM " + TardyContract.TardyEntry.TABLE_NAME, null);
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
