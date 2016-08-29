package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jerry.nsis.R;

/**
 * Created by Jerry on 2016/2/17.
 */
public class MainMenuNote extends LinearLayout {

    private Context mContext;

    private Paint mPaint = new Paint();

    private String colorString = "#7cdbf7";

    public MainMenuNote(Context context) {
        this(context, null);
    }

    public MainMenuNote(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMenuNote(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_main_note, this);
    }



}
