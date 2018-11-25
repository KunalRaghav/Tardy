package com.krsolutions.tardy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class TardyAdapter extends RecyclerView.Adapter<TardyAdapter.ViewHolder> {
    private static final String TAG="TardyAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView viewSubName;
        TextView viewSubPerc;

        public ViewHolder(View itemView){
            super(itemView);
            viewSubName = itemView.findViewById(R.id.subViewsubName);
            viewSubPerc = itemView.findViewById(R.id.subViewperc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),SubjectActivity.class);
                    if(viewSubName.getText().toString()!="No Subjects Added Yet") {
                        intent.putExtra("SubjectName", viewSubName.getText().toString());
                        v.getContext().startActivity(intent);
                    }else {
                        Toast.makeText(v.getContext(),"Add a subject",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Delete "+ viewSubName.getText().toString()+" ?");
                    builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.show();
                    return true;
                }
            });
        }
    }

    private List<Subject> subjectlist;

    public TardyAdapter(List<Subject> subjects){
        subjectlist=subjects;
    }


    @Override
    public TardyAdapter.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View subjectView = inflater.inflate(R.layout.subjectview,viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(subjectView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( TardyAdapter.ViewHolder viewHolder, int i) {
        Subject subject = subjectlist.get(i);
        TextView viewSubName = viewHolder.viewSubName;
        TextView viewSubPerc = viewHolder.viewSubPerc;
        viewSubName.setText(subject.getSubjectName());
        Log.d(TAG, "onBindViewHolder:\n Classes Attended:" + subject.ClassesAttended +"\n Total Classes:" + subject.TotalClasses);
        int clsA = subject.getClassesAttended();
        float clsT = subject.getTotalClasses();
        float perc = clsA/clsT*100;
        Log.d(TAG, "onBindViewHolder: Percent: " + clsA/clsT*100);
        viewSubPerc.setText(String.format("%.2f", perc)+" %");

    }

    @Override
    public int getItemCount() {
        return subjectlist.size();
    }
}
