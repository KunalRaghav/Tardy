package com.krsolutions.tardy.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.krsolutions.tardy.R;
import com.krsolutions.tardy.activities.SubjectActivity;
import com.krsolutions.tardy.data.Subject;
import com.krsolutions.tardy.data.funtool;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class TardyAdapter extends RecyclerView.Adapter<TardyAdapter.ViewHolder> {
    private static final String TAG="TardyAdapter";

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView viewSubName;
        TextView viewSubPerc;
        LinearLayout viewMenu;
        TextView viewSubBunk;
        TextView viewSubBunkV;
        Boolean menuShown = false;
        public ViewHolder(final View itemView){
            super(itemView);
            viewSubName = itemView.findViewById(R.id.subViewsubName);
            viewSubPerc = itemView.findViewById(R.id.subViewperc);
            viewMenu = itemView.findViewById(R.id.ll_menu);
            viewSubBunk = itemView.findViewById(R.id.subViewBunk);
            viewSubBunkV = itemView.findViewById(R.id.subViewBunkValue);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(menuShown){
                        viewMenu.removeViewAt(1);
                        menuShown=false;
                    }else{
                        LayoutInflater inflater = LayoutInflater.from(v.getContext());
                        View subjectMenu = inflater.inflate(R.layout.menu_subject_view,null);
                        viewMenu.addView(subjectMenu);
                        Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.slide_up);
                        subjectMenu.startAnimation(animation);
                        menuShown=true;
                        ImageView upClass = subjectMenu.findViewById(R.id.upClass);
                        ImageView downClass = subjectMenu.findViewById(R.id.downClass);
                        ImageView editClass = subjectMenu.findViewById(R.id.editClass);


                        upClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View buttonView) {
                                funtool.upClass(buttonView.getContext(),viewSubName.getText().toString());
                                viewMenu.removeViewAt(1);
                                menuShown=false;
                                new LoadTask(mBaseContext,mProgressBar,mRecyclerView).execute();
                            }
                        });
                        downClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View buttonView) {
                                funtool.missedClass(buttonView.getContext(),viewSubName.getText().toString());
                                viewMenu.removeViewAt(1);
                                menuShown=false;
                                new LoadTask(mBaseContext,mProgressBar,mRecyclerView).execute();
                            }
                        });

                        editClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(),SubjectActivity.class);
                                if(viewSubName.getText().toString()!="No Subjects Added Yet") {
                                    intent.putExtra("SubjectName", viewSubName.getText().toString());
                                    ActivityOptions options =
                                            ActivityOptions.makeClipRevealAnimation(v,(int)v.getX(),(int)v.getY(),v.getWidth(),100);
                                    v.getContext().startActivity(intent,options.toBundle());
                                }else {
                                    Toast.makeText(v.getContext(),"Add a subject",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(v.getContext(),SubjectActivity.class);
                    if(viewSubName.getText().toString()!="No Subjects Added Yet") {
                        intent.putExtra("SubjectName", viewSubName.getText().toString());
                        ActivityOptions options =
                                ActivityOptions.makeClipRevealAnimation(v,(int)v.getX(),(int)v.getY(),v.getWidth(),100);
                        v.getContext().startActivity(intent,options.toBundle());
                    }else {
                        Toast.makeText(v.getContext(),"Add a subject",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
        }
    }

    private List<Subject> subjectlist;
    private Context mBaseContext;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    public TardyAdapter(List<Subject> subjects,Context baseContext, ProgressBar progressBar,RecyclerView recyclerView){
        subjectlist=subjects;
        mBaseContext=baseContext;
        mProgressBar =progressBar;
        mRecyclerView= recyclerView;
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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(viewHolder.itemView.getContext());
        Float desiredPerc = Float.valueOf(sp.getString("pref_desired", "65.0"));
        Log.d(TAG, "onBindViewHolder: Desired Percentage: "+desiredPerc);
        Context context = viewHolder.itemView.getContext();
        Subject subject = subjectlist.get(i);
        TextView viewSubName = viewHolder.viewSubName;
        TextView viewSubPerc = viewHolder.viewSubPerc;
        TextView viewSubBunk = viewHolder.viewSubBunk;
        TextView viewSubBV = viewHolder.viewSubBunkV;
        viewSubName.setText(subject.getSubjectName());
        Log.d(TAG, "onBindViewHolder:\n Classes Attended:" + subject.ClassesAttended +"\n Total Classes:" + subject.TotalClasses);
        int clsA = subject.getClassesAttended();
        float clsT = subject.getTotalClasses();
        float perc = clsA/clsT*100;
        Log.d(TAG, "onBindViewHolder: Percent: " + clsA/clsT*100);
        viewSubPerc.setText(String.format("%.2f", perc)+" %");
        if(perc>desiredPerc){
            viewSubPerc.setBackgroundColor(ContextCompat.getColor(context,R.color.color_DesertSand));
            viewSubPerc.setTextColor(ContextCompat.getColor(context,R.color.color_GreyInde));
            viewSubBunk.setText("Bunks Possible:");
            viewSubBV.setText(String.valueOf(funtool.bunkPossible(clsA,clsT,desiredPerc)));
        }else{
            viewSubPerc.setBackgroundColor(ContextCompat.getColor(context,R.color.color_GreyInde));
            viewSubPerc.setTextColor(ContextCompat.getColor(context,R.color.color_DesertSand));
            viewSubBunk.setText("Classes To Attend:");
            viewSubBV.setText(String.valueOf(funtool.classesToAttend(clsA,clsT,desiredPerc)));
        }

    }

    @Override
    public int getItemCount() {
        return subjectlist.size();
    }
}
