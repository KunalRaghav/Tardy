package com.krsolutions.tardy.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.krsolutions.tardy.R;
import com.krsolutions.tardy.adapter.LoadTaskTimeline;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFragment extends Fragment {
    View view = null;
    RecyclerView historyRecyclerView;
    ImageView closeHistoryIV;
    FragmentTransaction fragmentTransaction;
    ProgressBar progressBar;
    TextView timelineFrom;
    TextView timelineTo;
    MaterialButton btRefine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history,container,false);
        return view;
    }

    public void passFT(FragmentTransaction ft){
        fragmentTransaction=ft;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyRecyclerView = view.findViewById(R.id.history_recycler_view);
        progressBar = view.findViewById(R.id.timeline_progressBar);
        closeHistoryIV = view.findViewById(R.id.closeHistory);
        timelineFrom = view.findViewById(R.id.timeline_from);
        timelineTo = view.findViewById(R.id.timeline_to);
        btRefine = view.findViewById(R.id.timeline_refine);
        timelineFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("field",3);
                bundle.putString("ORIGIN","timeline");
                datePickerFragment.setArguments(bundle);
                datePickerFragment.show(getActivity().getSupportFragmentManager(),"datePickerFrom");
            }
        });
        timelineTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("field",4);
                bundle.putString("ORIGIN","timeline");
                datePickerFragment.setArguments(bundle);
                datePickerFragment.show(getActivity().getSupportFragmentManager(),"datePickerTo");
            }
        });
        btRefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String lowerBound = timelineFrom.getText().toString().substring(6);
                    Date date = new Date(lowerBound);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    lowerBound = simpleDateFormat.format(date);
                    String upperBound = timelineTo.getText().toString().substring(4);
                    date = new Date(upperBound);
                    upperBound = simpleDateFormat.format(date);
                    new LoadTaskTimeline(getContext(),progressBar,historyRecyclerView,lowerBound,upperBound).execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        closeHistoryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFrag();
//                getActivity().getSupportFragmentManager().popb

            }
        });
//        TardyDbHelper dbHelper = new TardyDbHelper(getContext());
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery(SQL_SELECTOR,null);
//        while(cursor.moveToNext()){
//            int recordID = cursor.getInt(cursor.getColumnIndex(DateContract.DateEntry._ID));
//            String EntryTime = cursor.getString(cursor.getColumnIndex(DateContract.DateEntry.COLUMN_NAME_ENTRY_TIME));
//            int index=cursor.getColumnIndex(DateContract.DateEntry.COLUMN_NAME_ENTRY_TYPE);
//            Log.d(TAG, "onViewCreated: INDEX: "+index);
//            int EntryType = cursor.getInt(index);
//            int SubjectID = cursor.getInt(cursor.getColumnIndex(DateContract.DateEntry.COLUMN_NAME_TARDY_SUBJECT_ID));
//            String SubjectName = cursor.getString(cursor.getColumnIndex(TardyContract.TardyEntry.COLUMN_NAME_SUBJECT));
//            Log.d(TAG, "onViewCreated: recordID:"+recordID+"\n"+"EntryType: "+EntryType+"\n"+"Subject Name: "+SubjectName+"\n"+"Time: "+EntryTime);
//            HistoryRecord historyRecord = new HistoryRecord(recordID,EntryType,EntryTime,SubjectID,SubjectName);
//            if(historyRecord!=null) {
//                historyRecords.add(historyRecord);
//            }
//        }
//        cursor.close();
//        db.close();
//        dbHelper.close();
//        timelineAdapter = new TimelineAdapter(historyRecords,getContext());
//        historyRecyclerView.setAdapter(timelineAdapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat format_query_date = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-10);
        Date tenDaysBefore = calendar.getTime();
        String lowerDate = format.format(tenDaysBefore);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+11);
        Date tomorrowDate = calendar.getTime();
        String upperDate = format.format(tomorrowDate);
        timelineFrom.setText("From: "+lowerDate);
        timelineTo.setText("To: "+upperDate);
        String upperdateLimit =format_query_date.format(tomorrowDate);
        String lowerdatelimit = format_query_date.format(tenDaysBefore);
        new LoadTaskTimeline(getContext(),progressBar,historyRecyclerView,lowerdatelimit,upperdateLimit).execute();

    }

    private void removeFrag(){
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_down,R.anim.slide_up_hide,R.anim.slide_down,R.anim.slide_up_hide).remove(this).commit();
//        fragmentTransaction.remove(this).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
