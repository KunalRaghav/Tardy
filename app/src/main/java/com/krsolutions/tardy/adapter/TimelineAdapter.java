package com.krsolutions.tardy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.krsolutions.tardy.R;
import com.krsolutions.tardy.data.HistoryRecord;
import com.krsolutions.tardy.data.funtool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.Viewholder> {
    List<HistoryRecord> historyRecordList;
    Context baseContext;
    private static final String TAG = "TimelineAdapter";
    class Viewholder extends RecyclerView.ViewHolder{
        TextView timelineSubjectTextView;
        TextView timelineDateTextView;
        TextView timelineDayTextView;
        TextView timelineTimeTextView;
        ImageView timelineCircleLine;
        LinearLayoutCompat timelineSubBaseLL;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            timelineSubjectTextView = itemView.findViewById(R.id.timeline_view_textView_SubjectName);
            timelineDateTextView = itemView.findViewById(R.id.timeline_view_textView_Date);
            timelineCircleLine = itemView.findViewById(R.id.timeline_view_circle);
//            timelineSubBaseLL = itemView.findViewById(R.id.timeline_view_sub_base_ll);
            timelineDayTextView = itemView.findViewById(R.id.timeline_view_textView_Day);
            timelineTimeTextView = itemView.findViewById(R.id.timeline_view_textView_Time);
        }

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.timeline_view,parent,false);
        TimelineAdapter.Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.Viewholder holder, int position) {
        HistoryRecord record = historyRecordList.get(position);
        if(record.getEntryType()==1){
            holder.timelineCircleLine.setColorFilter(baseContext.getResources().getColor(android.R.color.holo_green_light));
        }else{
            holder.timelineCircleLine.setColorFilter(baseContext.getResources().getColor(android.R.color.holo_red_light));
        }
        String strDate[]= record.getEntryTime().split(" ");
        Log.d(TAG, "onBindViewHolder: Date: " +strDate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(strDate[0].split("-")[0]),Integer.parseInt(strDate[0].split("-")[1])-1,Integer.parseInt(strDate[0].split("-")[2]));
        Log.d(TAG, "onBindViewHolder: Day: " + calendar.get(Calendar.DAY_OF_WEEK));
        holder.timelineSubjectTextView.setText(record.getSubjectName());
//        String datefield = funtool.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK))+"\t\t"+strDate+"\t"+record.getEntryTime().split(" ")[1];
//        holder.timelineDateTextView.setText(datefield);
        //Date changing from yyyy-mm-dd to dd-MMM-yy
        SimpleDateFormat format_date_org = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format_date_user_friendly = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date date = format_date_org.parse(strDate[0]);
            holder.timelineDateTextView.setText(format_date_user_friendly.format(date));
        }catch (Exception e){
            e.printStackTrace();
            holder.timelineDateTextView.setText(strDate[0]);
        }
        //Time changing from HH:mm:ss to hh:mm:ss a
        SimpleDateFormat format_time_org = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format_time_user_friendly = new SimpleDateFormat("hh:mm:ss a");
        try{
            Date time = format_time_org.parse(strDate[1]);
            holder.timelineTimeTextView.setText(format_time_user_friendly.format(time));
        }catch (Exception e) {
            e.printStackTrace();
            holder.timelineTimeTextView.setText(strDate[1]);
        }
        holder.timelineDayTextView.setText(funtool.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));


    }

    @Override
    public int getItemCount() {
        return historyRecordList.size();
    }

    public TimelineAdapter(List<HistoryRecord> records, Context baseContext) {
        historyRecordList=records;
        this.baseContext=baseContext;
    }
}
