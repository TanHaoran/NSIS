package com.jerry.nsis.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.jerry.nsis.utils.DensityUtils;

/**
 * Created by Jerry on 2016/2/17.
 */
public class Triangle extends View {


    public Triangle(Context context) {
        this(context, null);
    }

    public Triangle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Triangle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置背景色
        canvas.drawColor(Color.parseColor("#00ffffff"));
        Paint paint = new Paint();
        // 去锯齿
        paint.setAntiAlias(true);
        // 设置实心图形
        paint.setStyle(Paint.Style.FILL);
        // 设置paint的颜色
        paint.setColor(Color.parseColor("#d6b99b"));

		// 画三角形
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(getWidth(), getHeight() / 2);
        path.lineTo(0, getHeight());
        path.close();
        canvas.drawPath(path, paint);

    }


}
