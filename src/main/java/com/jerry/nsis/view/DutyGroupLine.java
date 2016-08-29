package com.jerry.nsis.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.activity.DutyGroupEditActivity;
import com.jerry.nsis.entity.DutyGroup;
import com.jerry.nsis.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/2/17.
 */
public class DutyGroupLine extends LinearLayout {

    private LinearLayout mLayout;
    private TextView mTextName;

    private ImageView mEdit;


    private DutyGroup group;

    public DutyGroup getGroup() {
        return group;
    }

    public void setGroup(DutyGroup group) {
        this.group = group;
    }

    public DutyGroupLine(Context context) {
        this(context, null);
    }

    public DutyGroupLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DutyGroupLine(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_duty_group_line, this);

        mLayout = (LinearLayout) findViewById(R.id.ll_dutygroup);
        mTextName = (TextView) findViewById(R.id.tv_name);
        mEdit = (ImageView) findViewById(R.id.iv_edit);

        mEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DutyGroupEditActivity.class);
                intent.putExtra("group", group);
                L.i("组信息：" + group);
                context.startActivity(intent);
            }
        });
    }


    public void setName(String name) {
        mTextName.setText(name);
    }

    public void setBedList(List<DutyGroupBed> bedList) {
        for (DutyGroupBed b : bedList) {
            mLayout.addView(b);
        }
    }
}
