package com.jerry.nsis.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;

/**
 * Created by Jerry on 2016/3/18.
 */
public class ScanButton extends RelativeLayout {

    private Button mBtn;
    private TextView mNumber;

    private boolean mFocus = false;

    public ScanButton(Context context) {
        this(context, null);
    }

    public ScanButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       inflater.inflate(R.layout.view_scan_button, this);

        mNumber = (TextView) findViewById(R.id.tv_num);
        mBtn = (Button) findViewById(R.id.btn);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ScanButton);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ScanButton_text:
                    mBtn.setText(typedArray.getString(attr));
                    break;
            }
        }
        typedArray.recycle();

    }

    /**
     * 设置数量
     *
     * @param number
     */
    public void setNumber(int number) {
        if (number == 0) {
            mNumber.setVisibility(INVISIBLE);
        } else {
            mNumber.setText(String.valueOf(number));
            mNumber.setVisibility(VISIBLE);
        }
    }

    public void setFocus(boolean isFocus) {
        mFocus = isFocus;
        if (isFocus) {
            mBtn.setTextColor(getResources().getColor(R.color.pritn_bgcolor));
            mNumber.setTextColor(getResources().getColor(R.color.white));
            mNumber.setBackgroundResource(R.drawable.round_scan_button_number_focus);
        } else {
            mBtn.setTextColor(getResources().getColor(R.color.white));
            mNumber.setTextColor(getResources().getColor(R.color.pritn_bgcolor));
            mNumber.setBackgroundResource(R.drawable.round_scan_button_number);
        }
    }

    public boolean getFocus() {
        return mFocus;
    }
}
