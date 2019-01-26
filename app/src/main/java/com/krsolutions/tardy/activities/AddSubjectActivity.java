package com.krsolutions.tardy.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.krsolutions.tardy.R;
import com.krsolutions.tardy.data.Subject;
import com.krsolutions.tardy.data.TardyContract;
import com.krsolutions.tardy.data.TardyDbHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
        final TardyDbHelper mTardyDbHelper = new TardyDbHelper(this);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorFilter(Color.WHITE);


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
                    mSubject.SubjectName = etSubjectName.getText().toString().trim();
                    mSubject.TotalClasses = Integer.parseInt(etTotalClasses.getText().toString());
                    mSubject.ClassesAttended = Integer.parseInt(etClassesAttended.getText().toString());
                    if(mSubject.ClassesAttended<=mSubject.TotalClasses) {
                        SQLiteDatabase db = mTardyDbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TardyContract.TardyEntry.COLUMN_NAME_SUBJECT, mSubject.SubjectName);
                        values.put(TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES, mSubject.TotalClasses);
                        values.put(TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED, mSubject.ClassesAttended);
                        try {
                            long newRowId = db.insertOrThrow(TardyContract.TardyEntry.TABLE_NAME, null, values);
                            Log.d(TAG, "onClick: New Data Inserted:" + newRowId);
                            Toast.makeText(view.getContext(), "We'll track " + mSubject.SubjectName, Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (SQLiteConstraintException e) {
                            Snackbar snackbar = Snackbar.make(view, "2 subjects cannot have the same name", Snackbar.LENGTH_LONG);
                            snackbar.setAnchorView(R.id.fab);
                            snackbar.show();
                        }
                        db.close();
                        mSubject.setNull();
                        clearFields();
                    }else{
                        Snackbar snackbar = Snackbar.make(view,"Attended classes can't be more than total classes",Snackbar.LENGTH_LONG);
                        snackbar.setAnchorView(fab);
                        snackbar.show();
                    }
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
