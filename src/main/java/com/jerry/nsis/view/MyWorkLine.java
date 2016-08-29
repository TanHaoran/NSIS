package com.jerry.nsis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Schedule;

/**
 * Created by Jerry on 2016/2/17.
 */
public class MyWorkLine extends LinearLayout {

    private boolean hasNurse = false;

    private Schedule mSchedule;


    private LinearLayout mLayout;
    private TextView mTitleText;

    public MyWorkLine(Context context) {
        this(context, null);
    }

    public MyWorkLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWorkLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_mywork_line, this);

        mLayout = (LinearLayout)findViewById(R.id.ll_time);
        mTitleText = (TextView)findViewById(R.id.tv_title) ;

    }

    public void addNurse(NurseImageName imageName){
        mLayout.addView(imageName);
        hasNurse = true;
    }

    public void setTitle(String title) {
        mTitleText.setText(title);
    }

    public Schedule getSchedule() {
        return mSchedule;
    }

    public void setSchedule(Schedule schedule) {
        mSchedule = schedule;
    }

    public boolean isHasNurse() {
        return hasNurse;
    }
}
