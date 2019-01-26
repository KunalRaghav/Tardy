package com.krsolutions.tardy.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.krsolutions.tardy.R;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    private int fieldName;
    TextView from_subjectActivity;
    TextView from_timeline;
    TextView to_subjectActivity;
    TextView to_timeline;
    String origin;
    private static final String TAG = "DatePickerFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        if(origin=="subject") {
            from_subjectActivity = getActivity().findViewById(R.id.subject_card_from);
            to_subjectActivity = getActivity().findViewById(R.id.subject_card_to);
        }else if(origin=="timeline") {
            from_timeline = getActivity().getSupportFragmentManager().findFragmentByTag("timelineView").getView().findViewById(R.id.timeline_from);
            to_timeline = getActivity().getSupportFragmentManager().findFragmentByTag("timelineView").getView().findViewById(R.id.timeline_to);
        }
        return new DatePickerDialog(getContext(),listener,year,month,date);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        fieldName=args.getInt("field");
        origin = args.getString("ORIGIN");
    }
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            String date = DateFormat.getDateInstance().format(c.getTime());
            Log.d(TAG, "onDateSet: " + date + "\t" + c.getTime());
            switch (fieldName){
                case 1:
                    from_subjectActivity.setText("From: "+date);
                    break;
                case 2:
                    to_subjectActivity.setText("To: "+date);
                    break;
                case 3:
                    from_timeline.setText("From: "+date);
                    break;
                case 4:
                    to_timeline.setText("To: "+date);
                    break;
            }
        }
    };
}
