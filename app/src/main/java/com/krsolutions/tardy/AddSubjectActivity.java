package com.krsolutions.tardy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddSubjectActivity extends AppCompatActivity {
    private static final String TAG = "AddSubjectActivity";
    Subject mSubject;
    @BindView(R.id.etSubjectName) EditText etSubjectName;
    @BindView(R.id.etTotalClasses) EditText etTotalClasses;
    @BindView(R.id.etClassesAttended) EditText etClassesAttended;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mSubject = new Subject(-1,"null",-1,-1);
        final TardyDbHelper mDbHelper = new TardyDbHelper(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: strings:\n1: "
                        + etSubjectName.getText().toString() + "\n2: "
                        + etTotalClasses.getText().toString() + "\n3: "
                        + etClassesAttended.getText().toString());
                if(
                        etSubjectName.getText().toString().length() > 0 &&
                        !etTotalClasses.getText().toString().isEmpty() &&
                                !etClassesAttended.getText().toString().isEmpty()
                        ){
                    mSubject.SubjectName = etSubjectName.getText().toString();
                    mSubject.TotalClasses = Integer.parseInt(etTotalClasses.getText().toString());
                    mSubject.ClassesAttended = Integer.parseInt(etClassesAttended.getText().toString());
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(TardyContract.TardyEntry.COLUMN_NAME_SUBJECT,mSubject.SubjectName);
                    values.put(TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES,mSubject.TotalClasses);
                    values.put(TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED,mSubject.ClassesAttended);
                    try {
                        long newRowId = db.insertOrThrow(TardyContract.TardyEntry.TABLE_NAME, null, values);
                        Log.d(TAG, "onClick: New Data Inserted:" + newRowId);
                        Toast.makeText(view.getContext(),"We'll track "+ mSubject.SubjectName,Toast.LENGTH_SHORT).show();
                    }catch (SQLiteConstraintException e){
                        Snackbar.make(view,"2 subjects cannot have the same name",Snackbar.LENGTH_LONG).show();
                    }
                    db.close();
                    mSubject.setNull();
                    clearFields();
                }else{
                Snackbar.make(view, "Incomplete Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    void clearFields(){
        etSubjectName.setText("");
        etClassesAttended.setText("");
        etTotalClasses.setText("");
        mSubject.setNull();
    }
}
