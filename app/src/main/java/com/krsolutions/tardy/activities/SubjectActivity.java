package com.krsolutions.tardy.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.krsolutions.tardy.R;
import com.krsolutions.tardy.data.Subject;
import com.krsolutions.tardy.data.TardyContract;
import com.krsolutions.tardy.data.TardyDbHelper;
import com.krsolutions.tardy.data.funtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.krsolutions.tardy.data.TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED;
import static com.krsolutions.tardy.data.TardyContract.TardyEntry.COLUMN_NAME_SUBJECT;
import static com.krsolutions.tardy.data.TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES;
import static com.krsolutions.tardy.data.TardyContract.TardyEntry.TABLE_NAME;

public class SubjectActivity extends AppCompatActivity {
    @BindView(R.id.subtvSubjectPercentage) TextView subjectPercentage;
    @BindView(R.id.subjectAppBar) BottomAppBar bottomtoolbar;
    @BindView(R.id.subetTotalClasses) EditText etTotalClasses;
    @BindView(R.id.subetClassesAttended) EditText etClassesAttended;
    @BindView(R.id.btn_plus) Button btnPlus;
    @BindView(R.id.btn_minus) Button btnMinus;
    @BindView(R.id.etcSubjectName) EditText etSubjectName;
    @BindView(R.id.fabSaveChanges) FloatingActionButton fabSaveChanges;
    @BindView(R.id.subDelete) ImageView btnDelete;
//    @BindView(R.id.subToolbar) Toolbar toolbar;
    private static final String TAG = "SubjectActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_subject);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        final String subname = extras.getString("SubjectName");
        final TardyDbHelper dbHelper = new TardyDbHelper(this);
        SQLiteDatabase read_db = dbHelper.getReadableDatabase();
//        toolbar.setTitle(subname);
        etSubjectName.setText(subname);
        etSubjectName.setFocusable(false);
        etSubjectName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etSubjectName.setFocusableInTouchMode(true);
                return true;
            }
        });
        Cursor cursor = read_db.rawQuery("Select * from "+TABLE_NAME+" where "+TardyContract.TardyEntry.COLUMN_NAME_SUBJECT +"=?",new String[]{subname});
        cursor.moveToFirst();
        final Subject mSubject = funtool.DbtoObject(cursor);
        read_db.close();
        fabSaveChanges.setColorFilter(Color.WHITE);
        final float perc =funtool.calcPerc(mSubject.getClassesAttended(),mSubject.getTotalClasses());
        subjectPercentage.setText(String.format("%.2f", perc) +" %");
//        setSupportActionBar(toolbar);
        etTotalClasses.setText(String.valueOf(mSubject.getTotalClasses()));
        etTotalClasses.setFocusable(false);
        etClassesAttended.setText(String.valueOf(mSubject.getClassesAttended()));
        etClassesAttended.setFocusable(false);
        SQLiteDatabase write_db = dbHelper.getWritableDatabase();
        write_db.close();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int tc = Integer.parseInt(etTotalClasses.getText().toString());
                    int ca = Integer.parseInt(etClassesAttended.getText().toString());
                    etTotalClasses.setText(String.valueOf(tc + 1));
                    etClassesAttended.setText(String.valueOf(ca + 1));
                }catch (Exception e){
                    Snackbar snackbar = Snackbar.make(v,"Field is empty",Snackbar.LENGTH_LONG);
                    snackbar.setAnchorView(fabSaveChanges);
                    snackbar.show();
                }
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int tc = Integer.parseInt(etTotalClasses.getText().toString());
                    etTotalClasses.setText(String.valueOf(tc + 1));
                }catch (Exception e){
                    Snackbar snackbar = Snackbar.make(v,"Field is empty",Snackbar.LENGTH_LONG);
                    snackbar.setAnchorView(fabSaveChanges);
                    snackbar.show();
                }
            }
        });
        etClassesAttended.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etClassesAttended.setFocusableInTouchMode(true);
                return true;
            }
        });

        etTotalClasses.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etTotalClasses.setFocusableInTouchMode(true);
                return true;
            }
        });
        fabSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int tc = Integer.parseInt(etTotalClasses.getText().toString());
                    int ca = Integer.parseInt(etClassesAttended.getText().toString());
                    String newSubName = etSubjectName.getText().toString();
                    if(ca<=tc) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        try {
                            String query = "Update " + TABLE_NAME + " set " + COLUMN_NAME_TOTAL_CLASSES + "=" + tc + ", " + COLUMN_NAME_CLASSES_ATTENDED + "=" + ca + ", " + COLUMN_NAME_SUBJECT + "=\"" + newSubName + "\" where " + _ID + "=\"" + mSubject.getSubjectID() + "\";";
                            Log.d(TAG, "onClick: Query\n" + query);
                            db.execSQL(query);
                            Snackbar snackbar = Snackbar.make(v, "Updated Subject", Snackbar.LENGTH_SHORT).setAction("Action", null);
                            snackbar.setAnchorView(fabSaveChanges);
                            snackbar.show();
                            db.close();
                            subjectPercentage.setText(String.format("%.2f", funtool.calcPerc(ca, tc)) + " %");
                        }catch (Exception e){
                            e.printStackTrace();
                            Snackbar snackbar =Snackbar.make(v,"Subject name given is not unique",Snackbar.LENGTH_LONG);
                            snackbar.setAnchorView(fabSaveChanges);
                            snackbar.show();
                        }

                    }else{
                        Snackbar snackbar = Snackbar.make(v,"Attended classes can't be more than total classes",Snackbar.LENGTH_LONG);
                        snackbar.setAnchorView(fabSaveChanges);
                        snackbar.show();
                    }
                }catch (Exception e){
                    Snackbar snackbar = Snackbar.make(v, "Field can't be empty", Snackbar.LENGTH_SHORT).setAction("Action", null);
                    snackbar.setAnchorView(fabSaveChanges);
                    snackbar.show();
                }

            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TardyDbHelper dbHelper = new TardyDbHelper(v.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Bundle extra = getIntent().getExtras();
                String delQuery = "Delete from "+TABLE_NAME+" where "+COLUMN_NAME_SUBJECT+"=\""+extra.getString("SubjectName")+"\";";
                Log.d(TAG, "onOptionsItemSelected: query:\n" + delQuery);
                db.execSQL(delQuery);
                finish();
            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.subjectmenu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.itemDelete:
//                TardyDbHelper dbHelper = new TardyDbHelper(this);
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                Bundle extra = getIntent().getExtras();
//                String delQuery = "Delete from "+TABLE_NAME+" where "+COLUMN_NAME_SUBJECT+"=\""+extra.getString("SubjectName")+"\";";
//                Log.d(TAG, "onOptionsItemSelected: query:\n" + delQuery);
//                db.execSQL(delQuery);
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
