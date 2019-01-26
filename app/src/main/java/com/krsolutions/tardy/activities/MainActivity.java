package com.krsolutions.tardy.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.krsolutions.tardy.R;
import com.krsolutions.tardy.adapter.LoadTask;
import com.krsolutions.tardy.data.funtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MaterialButton fab;
    BottomAppBar appBar;
    ProgressBar progressBar;
    TextView username;
    CoordinatorLayout mainCoord;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        appBar = findViewById(R.id.appBar);
        progressBar = findViewById(R.id.progressBar);
        setSupportActionBar(appBar);
        username = findViewById(R.id.username);
        mainCoord = findViewById(R.id.main_co_ord);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddSubjectActivity.class);
                ActivityOptions options =
                        ActivityOptions.makeClipRevealAnimation(v,(int)v.getY(),(int)v.getX(),v.getWidth(),100);
                startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sp = getSharedPreferences("com.krsolutions.tardy",MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.sortByTimeAdded:
                sp.edit().putInt("sortOrder",0).commit();
                new LoadTask(getApplicationContext(),progressBar,recyclerView).execute();
                break;
            case R.id.sortByAlpha:
                new LoadTask(getApplicationContext(),progressBar,recyclerView, funtool.SORT.alpha).execute();
                sp.edit().putInt("sortOrder",1).commit();
                break;
            case R.id.sortByPercentage:
                new LoadTask(getApplicationContext(),progressBar,recyclerView, funtool.SORT.percent).execute();
                sp.edit().putInt("sortOrder",2).commit();
                break;
            case R.id.sortByClassAttended:
                sp.edit().putInt("sortOrder",3).commit();
                new LoadTask(getApplicationContext(),progressBar,recyclerView,funtool.SORT.classAttended).execute();
                break;
            case R.id.history:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                HistoryFragment historyFragment = new HistoryFragment();
                historyFragment.passFT(ft);
                ft.setCustomAnimations(R.anim.slide_down,R.anim.slide_up_hide,R.anim.slide_down,R.anim.slide_up_hide);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.add(R.id.main_co_ord,historyFragment,"timelineView").addToBackStack(null).commit();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: IN HERE");
        SharedPreferences dsp = PreferenceManager.getDefaultSharedPreferences(this);
        String uname = dsp.getString("pref_user", "");
        SharedPreferences sp = getSharedPreferences("com.krsolutions.tardy",MODE_PRIVATE);
        username.setText(uname);
        Log.d(TAG, "onResume: sortOrder: "+ sp.getInt("sortOrder",0));
        switch(sp.getInt("sortOrder",0)) {
            case 0:
                new LoadTask(getApplicationContext(), progressBar, recyclerView).execute();
                break;
            case 1:
                new LoadTask(getApplicationContext(),progressBar,recyclerView,funtool.SORT.alpha).execute();
                break;
            case 2:
                new LoadTask(getApplicationContext(),progressBar,recyclerView,funtool.SORT.percent).execute();
                break;
            case 3:
                new LoadTask(getApplicationContext(),progressBar,recyclerView,funtool.SORT.classAttended).execute();
                break;
        }
    }
}
