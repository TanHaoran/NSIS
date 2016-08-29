package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.DensityUtils;

/**
 * Created by Jerry on 2016/3/1.
 */
public class DutyGroupBed extends TextView {

    public DutyGroupBed(Context context) {
        this(context, null);
    }

    public DutyGroupBed(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DutyGroupBed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextSize(DensityUtils.dp2px(context, 23));
        setTextColor(Color.parseColor("#656565"));
        setWidth(DensityUtils.dp2px(context, 90));
        setHeight(DensityUtils.dp2px(context, 60));
        setGravity(Gravity.CENTER);
    }
}