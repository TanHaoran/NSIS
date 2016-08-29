package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.entity.Nursing;
import com.jerry.nsis.entity.NursingDetail;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.NsisUtil;

/**
 * Created by Jerry on 2016/2/17.
 */
public class SmallBed extends LinearLayout {

    private TextView textNo;
    private TextView textContent;

    private TextView textNumber;

    private BedInfo info;

    private boolean hasInfo;


    public SmallBed(Context context) {
        this(context, null);
    }

    public SmallBed(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallBed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    public BedInfo getInfo() {
        return info;
    }

    public void setInfo(BedInfo info) {
        this.info = info;
        textNo.setTextColor(Color.parseColor(info.getColor()));
        textContent.setTextColor(Color.parseColor(info.getColor()));
        if (!TextUtils.isEmpty(info.getNo().trim())) {
            textNo.setText(info.getNo());
        } else {
            textNo.setText("空");
        }
        if (info.getNursing().getShowPatientName() == 0) {
            textContent.setText("(" + info.getContent() + ")");
        } else {
            String content = NsisUtil.getPatientNameByHosIdAndInTimes(info.getHosId(), info.getInTimes());
            if (!TextUtils.isEmpty(content)) {
                textContent.setText("(" + content + ")");
            }
        }
        if (info.getNursingDetail() != null) {
            int number = info.getNursingDetail().getExeTimes() - info.getNursingDetail().getExedTimes();
            if (number > 0) {
                setNumber(number);
            }
        }
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_small_bed, this);
        textNo = (TextView) findViewById(R.id.tv_no);
        textContent = (TextView) findViewById(R.id.tv_doc);
        textNumber = (TextView) findViewById(R.id.tv_number);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasInfo) {
                    BedPopupWindow popupWindow = new BedPopupWindow(getContext());
                    L.i("所点击的信息住院号：" + info.getHosId() + "，住院次数：" + info.getInTimes());
                    popupWindow.showPopupWindow(view, info);
                }
            }
        });
    }

    public void clearBedInfo() {
        this.info = new BedInfo();
        textNo.setText("");
        textContent.setText("");
        hasInfo = false;
        textNumber.setVisibility(INVISIBLE);
    }

    public boolean isHasInfo() {
        return hasInfo;
    }

    public void setHasInfo(boolean hasInfo) {
        this.hasInfo = hasInfo;
    }

    private void setNumber(int number) {
        textNumber.setText(String.valueOf(number));
        textNumber.setVisibility(VISIBLE);
    }
}
