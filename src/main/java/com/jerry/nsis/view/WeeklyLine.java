package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.entity.Schedule;
import com.jerry.nsis.entity.WeekWork;

/**
 * Created by Jerry on 2016/2/17.
 */
public class WeeklyLine extends RelativeLayout {


    private TextView mCol1;
    private TextView mCol2;
    private TextView mCol3;
    private TextView mCol4;
    private TextView[] mCols;

    private TextView mMonday;
    private TextView mTuesday;
    private TextView mWednesday;
    private TextView mThursday;
    private TextView mFriday;
    private TextView mSaturday;
    private TextView mSunday;


    public WeeklyLine(Context context) {
        this(context, null);
    }

    public WeeklyLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeeklyLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_weekly_line, this);
        mCol1 = (TextView) findViewById(R.id.tv_col1);
        mCol2 = (TextView) findViewById(R.id.tv_col2);
        mCol3 = (TextView) findViewById(R.id.tv_col3);
        mCol4 = (TextView) findViewById(R.id.tv_col4);
        mCols = new TextView[]{mCol1, mCol2, mCol3, mCol4};
        mMonday = (TextView) findViewById(R.id.tv_monday);
        mTuesday = (TextView) findViewById(R.id.tv_tuesday);
        mWednesday = (TextView) findViewById(R.id.tv_wednesday);
        mThursday = (TextView) findViewById(R.id.tv_thursday);
        mFriday = (TextView) findViewById(R.id.tv_friday);
        mSaturday = (TextView) findViewById(R.id.tv_saturday);
        mSunday = (TextView) findViewById(R.id.tv_sunday);
    }

    public void setColContent(int index, String content) {
        mCols[index].setText(content);
    }


    public void setWeekWork(WeekWork w) {
        mMonday.setText(w.getMonday());
        setTextColor(mMonday, w.getMonday());

        mTuesday.setText(w.getTuesday());
        setTextColor(mTuesday, w.getTuesday());

        mWednesday.setText(w.getWednesday());
        setTextColor(mWednesday, w.getWednesday());

        mThursday.setText(w.getThursday());
        setTextColor(mThursday, w.getThursday());

        mFriday.setText(w.getFriday());
        setTextColor(mFriday, w.getFriday());

        mSaturday.setText(w.getSaturday());
        setTextColor(mSaturday, w.getSaturday());

        mSunday.setText(w.getSunday());
        setTextColor(mSunday, w.getSunday());
    }

    /**
     * 从班次信息中查取排班名称的显示颜色（有可能一天是两个班）
     * @param view
     * @param content
     */
    private void setTextColor(TextView view, String content) {
        String[] array = content.split(" ");
        SpannableString span = new SpannableString(content);
        int index = 0;
        for (String s : array) {
            span.setSpan(new ForegroundColorSpan(Color.parseColor(getColorBySecheduleName(s))),
                    index, index + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = s.length() + 1;
        }
        view.setText(span);
    }

    /**
     * 获取排班类型
     *
     * @param scheduleName
     * @return
     */
    private String getTypeNameBySecheduleName(String scheduleName) {
        for (Schedule s : LoginInfo.mScheduleList) {
            if (s.getScheduleName().equals(scheduleName)) {
                return s.getTypeName();
            }
        }
        return null;
    }

    /**
     * 获取排班类型
     *
     * @param scheduleName
     * @return
     */
    private String getColorBySecheduleName(String scheduleName) {
        for (Schedule s : LoginInfo.mScheduleList) {
            if (s.getScheduleName().equals(scheduleName)) {
                return "#" + s.getColor();
            }
        }
        return "#222222";
    }


}
