package com.krsolutions.tardy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    TardyAdapter adapter;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=findViewById(R.id.floatingActionButton) ;
        recyclerView = findViewById(R.id.recyclerView);
        TardyDbHelper tardyDbHelper = new TardyDbHelper(this);
        SQLiteDatabase db = tardyDbHelper.getReadableDatabase();
        Log.d(TAG, "onCreate: I'm HERE");
        Cursor cursor = db.rawQuery("SELECT * FROM " + TardyContract.TardyEntry.TABLE_NAME,null);
        List subjects = new ArrayList<Subject>();
        while (cursor.moveToNext()){
            Subject mSubject= funtool.DbtoObject(cursor);
            subjects.add(mSubject);
        }
        if(subjects.size()==0){
            Subject msubject= new Subject(-1,"No Subjects Added Yet",100,100);
            subjects.add(msubject);
        }
        Log.d(TAG, "onCreate: Subjects:" + subjects);
        adapter = new TardyAdapter(subjects);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AddSubjectActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mmRefresh:
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
