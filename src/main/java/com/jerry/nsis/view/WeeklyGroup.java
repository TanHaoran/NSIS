package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.DensityUtils;
import com.jerry.nsis.utils.L;

/**
 * Created by Jerry on 2016/2/17.
 */
public class WeeklyGroup extends RelativeLayout {

    private Context mContext;

    private TextView mGroup;
    private LinearLayout mGroupLayout;

    private LinearLayout mSmallGroup;
    private LinearLayout mLayout;


    public WeeklyGroup(Context context) {
        this(context, null);
    }

    public WeeklyGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeeklyGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_weekly_group, this);
        mGroup = (TextView) findViewById(R.id.tv_group);
        mGroupLayout = (LinearLayout) findViewById(R.id.ll_weekly_line);
        mSmallGroup = (LinearLayout) findViewById(R.id.ll_small_group);
        mLayout = (LinearLayout) findViewById(R.id.tools_layout);
    }

    public void addLine(WeeklyLine line) {
        mGroupLayout.addView(line);
    }

    public void setGroup(String group) {
        mGroup.setText(group);
    }

    public void addSmallGroup(String name, int rows) {
        L.i("添加到布局中：" + name + "，行数：" + rows);
        mLayout.setVisibility(VISIBLE);
        TextView view = new TextView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, rows * 30 - 1));
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(lp);
        view.setText(name);
        view.setTextColor(Color.parseColor("#000000"));
        View line = new View(mContext);
        line.setBackgroundColor(Color.parseColor("#b2b2b2"));
        LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, 1));
        line.setLayoutParams(lineLp);
        mSmallGroup.addView(view);
        mSmallGroup.addView(line);
    }


}
