package com.jerry.nsis.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.activity.MyWorkActivity;
import com.jerry.nsis.entity.Nurse;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.StreamUtil;

/**
 * Created by Jerry on 2016/2/17.
 */
public class NurseImageName extends LinearLayout {

    private Context mContext;

    private Nurse mNurse;

    private CircleImageView mHead;
    private TextView mNameText;
    private TextView mTypeText;


    public NurseImageName(Context context) {
        this(context, null);
    }

    public NurseImageName(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NurseImageName(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_nurseimagename, this);

        mHead = (CircleImageView) findViewById(R.id.iv_head);
        mNameText = (TextView) findViewById(R.id.tv_col4);
        mTypeText = (TextView) findViewById(R.id.tv_type);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("点击的信息：" + mNurse.toString());
                Intent intent = new Intent();
                intent.putExtra("nurse", mNurse);
                intent.setClass(getContext(), MyWorkActivity.class);
                mContext.startActivity(intent);
            }
        });

    }


    /**
     * 设置护士的值班类型
     *
     * @param type
     */
    public void setType(String type) {
        mTypeText.setText(type);
        mTypeText.setVisibility(VISIBLE);
    }


    public void setNurse(Nurse nurse) {
        mNurse = nurse;
        mNameText.setText(nurse.getName());
        if (!TextUtils.isEmpty(nurse.getThumbnail())) {
            mHead.setImageBitmap(StreamUtil.base64ToBitmap(nurse.getThumbnail()));
        }
    }

    public Nurse getNurse() {
        return mNurse;
    }

}
