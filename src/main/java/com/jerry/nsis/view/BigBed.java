package com.jerry.nsis.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.NsisUtil;

/**
 * Created by Jerry on 2016/2/17.
 */
public class BigBed extends LinearLayout {


    public static final String OPERATION = "手术";
    public static final String IN_HOS = "入院";
    public static final String HIGH_TEMPER = "高温";

    private TextView textNo;
    private TextView textContent;
    private TextView textNumber;
    private LinearLayout eventLayout;
    private TextView textEventOperation;
    private TextView textEventIn;
    private TextView textEventTemper;
    private TextView textEventOrder;

    private BedInfo info;

    private boolean hasInfo;

    public BedInfo getInfo() {
        return info;
    }

    public void setInfo(BedInfo info) {
        if (info == null) {
            return;
        }
        this.info = info;
        if (!TextUtils.isEmpty(info.getNo())) {
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
        if (!TextUtils.isEmpty(info.getEvent())) {
            setEventData();
        } else {
            eventLayout.setVisibility(GONE);
        }
        if (info.getNursingDetail() != null) {
            int number = info.getNursingDetail().getExeTimes() - info.getNursingDetail().getExedTimes();
            if (number > 0) {
                setNumber(number);
            }
        }
    }

    /**
     * 总共有4中护理时间：入、术、温、医(其余的都是医)
     */
    private void setEventData() {
        eventLayout.setVisibility(GONE);
        textEventOperation.setVisibility(GONE);
        textEventIn.setVisibility(GONE);
        textEventTemper.setVisibility(GONE);
        textEventOrder.setVisibility(GONE);

        String event = info.getEvent();
        event = event.replace("|", "");
        if (event.contains(OPERATION)) {
            textEventOperation.setVisibility(VISIBLE);
            eventLayout.setVisibility(VISIBLE);
            event = event.replace(OPERATION, "");
        }
        if (event.contains(IN_HOS)) {
            textEventIn.setVisibility(VISIBLE);
            eventLayout.setVisibility(VISIBLE);
            event = event.replace(IN_HOS, "");
        }
        if (event.contains(HIGH_TEMPER)) {
            textEventTemper.setVisibility(VISIBLE);
            eventLayout.setVisibility(VISIBLE);
            event = event.replace(HIGH_TEMPER, "");
        }
        if (event.length() > 0) {
            textEventOrder.setVisibility(VISIBLE);
            eventLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * 清除保存的信息
     */
    public void clearBedInfo() {
        this.info = new BedInfo();
        textNo.setText("");
        textContent.setText("");
        textNumber.setVisibility(INVISIBLE);
        eventLayout.setVisibility(GONE);
        textEventOperation.setVisibility(GONE);
        textEventIn.setVisibility(GONE);
        textEventTemper.setVisibility(GONE);
        textEventOrder.setVisibility(GONE);
        hasInfo = false;
    }

    public BigBed(Context context) {
        this(context, null);
    }

    public BigBed(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigBed(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.item_big_bed, this);
        textNo = (TextView) findViewById(R.id.tv_no);
        textContent = (TextView) findViewById(R.id.tv_doc);
        textNumber = (TextView) findViewById(R.id.tv_number);
        eventLayout = (LinearLayout) findViewById(R.id.ll_event);
        textEventOperation = (TextView) findViewById(R.id.tv_event_operation);
        textEventIn = (TextView) findViewById(R.id.tv_event_in_hos);
        textEventTemper = (TextView) findViewById(R.id.tv_event_temper);
        textEventOrder = (TextView) findViewById(R.id.tv_event_order);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasInfo) {
                    BedPopupWindow popupWindow = new BedPopupWindow(getContext());
                    popupWindow.showPopupWindow(view, info);
                }
            }
        });

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
