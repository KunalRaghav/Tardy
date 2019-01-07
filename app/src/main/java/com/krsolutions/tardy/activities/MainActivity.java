package com.krsolutions.tardy.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.krsolutions.tardy.R;
import com.krsolutions.tardy.adapter.LoadTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MaterialButton fab;
    BottomAppBar appBar;
    ProgressBar progressBar;
    TextView username;
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
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
//                Fragment preferenceFragment = new SettingsFragment();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.pref_container, preferenceFragment);
//                ft.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
//


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String uname = sp.getString("pref_user", "");
        username.setText(uname);
        new LoadTask(getApplicationContext(),progressBar,recyclerView).execute();
    }
}
