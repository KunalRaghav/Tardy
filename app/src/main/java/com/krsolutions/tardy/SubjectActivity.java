package com.krsolutions.tardy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.krsolutions.tardy.TardyContract.TardyEntry.COLUMN_NAME_CLASSES_ATTENDED;
import static com.krsolutions.tardy.TardyContract.TardyEntry.COLUMN_NAME_SUBJECT;
import static com.krsolutions.tardy.TardyContract.TardyEntry.COLUMN_NAME_TOTAL_CLASSES;
import static com.krsolutions.tardy.TardyContract.TardyEntry.TABLE_NAME;

public class SubjectActivity extends AppCompatActivity {
    @BindView(R.id.subtvSubjectPercentage) TextView subjectPercentage;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.subetTotalClasses) EditText etTotalClasses;
    @BindView(R.id.subetClassesAttended) EditText etClassesAttended;
    @BindView(R.id.btn_plus) Button btnPlus;
    @BindView(R.id.btn_minus) Button btnMinus;
    @BindView(R.id.fabSaveChanges) FloatingActionButton fabSaveChanges;
    private static final String TAG = "SubjectActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        final String subname = extras.getString("SubjectName");
        final TardyDbHelper dbHelper = new TardyDbHelper(this);
        SQLiteDatabase read_db = dbHelper.getReadableDatabase();
        Cursor cursor = read_db.rawQuery("Select * from "+TABLE_NAME+" where "+TardyContract.TardyEntry.COLUMN_NAME_SUBJECT +"=?",new String[]{subname});
        cursor.moveToFirst();
        Subject mSubject = funtool.DbtoObject(cursor);
        read_db.close();
        final float perc =funtool.calcPerc(mSubject.getClassesAttended(),mSubject.getTotalClasses());
        subjectPercentage.setText(String.format("%.2f", perc) +" %");
        toolbar.setTitle(subname);
        setSupportActionBar(toolbar);
        etTotalClasses.setText(String.valueOf(mSubject.getTotalClasses()));
        etTotalClasses.setFocusable(false);
        etClassesAttended.setText(String.valueOf(mSubject.getClassesAttended()));
        etClassesAttended.setFocusable(false);
        SQLiteDatabase write_db = dbHelper.getWritableDatabase();
        write_db.close();
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tc = Integer.parseInt(etTotalClasses.getText().toString());
                int ca = Integer.parseInt(etClassesAttended.getText().toString());
                etTotalClasses.setText(String.valueOf(tc+1));
                etClassesAttended.setText(String.valueOf(ca+1));
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tc = Integer.parseInt(etTotalClasses.getText().toString());
                int ca = Integer.parseInt(etClassesAttended.getText().toString());
                etTotalClasses.setText(String.valueOf(tc+1));
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
                int tc = Integer.parseInt(etTotalClasses.getText().toString());
                int ca = Integer.parseInt(etClassesAttended.getText().toString());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String query ="Update " + TABLE_NAME + " set " + COLUMN_NAME_TOTAL_CLASSES + "=" + tc +", "+ COLUMN_NAME_CLASSES_ATTENDED + "="+ca +" where "+ COLUMN_NAME_SUBJECT + "=\""+subname+"\";";
                Log.d(TAG, "onClick: Query\n" +query);
                db.execSQL(query);
                Snackbar.make(v,"Updated Subject",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                db.close();
                subjectPercentage.setText(String.format("%.2f",funtool.calcPerc(ca,tc))+" %");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.subjectmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.itemDelete:
                TardyDbHelper dbHelper = new TardyDbHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Bundle extra = getIntent().getExtras();
                String delQuery = "Delete from "+TABLE_NAME+" where "+COLUMN_NAME_SUBJECT+"=\""+extra.getString("SubjectName")+"\";";
                Log.d(TAG, "onOptionsItemSelected: query:\n" + delQuery);
                db.execSQL(delQuery);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
