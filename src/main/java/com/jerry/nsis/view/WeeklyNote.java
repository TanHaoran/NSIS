package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.DensityUtils;
import com.jerry.nsis.utils.L;

/**
 * Created by Jerry on 2016/2/17.
 */
public class WeeklyNote extends RelativeLayout {

    private Context mContext;

    private TextView mNote;


    public WeeklyNote(Context context) {
        this(context, null);
    }

    public WeeklyNote(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeeklyNote(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_weekly_note, this);
        mNote = (TextView) findViewById(R.id.tv_note);
    }


    public void setNote(String note) {
        mNote.setText(note);
    }


}
