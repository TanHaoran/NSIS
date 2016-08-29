package com.jerry.nsis.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.DensityUtils;

/**
 * Created by Jerry on 2016/2/17.
 */
public class TriangleArrow extends View {

    private String colorString = "#0000ff";


    private float triangleWidth = 12;    // 单位像素dp

    private float marginTop = 10; // 单位dp

    public TriangleArrow(Context context) {
        this(context, null);
    }

    public TriangleArrow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.TriangleArrow);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.TriangleArrow_bg_color:
                    colorString = typedArray.getString(attr);
                    break;
            }
        }
        typedArray.recycle();


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
        if (!colorString.startsWith("#")) {
            colorString = "#0033cb";
        }
        paint.setColor(Color.parseColor(colorString));

        // 画矩形
        canvas.drawRect(0, DensityUtils.dp2px(getContext(), marginTop), getWidth() - DensityUtils.dp2px(getContext(), triangleWidth), getHeight()- DensityUtils.dp2px(getContext(), marginTop), paint);// 正方形

		// 画右侧三角形
        Path path = new Path();
        path.moveTo(getWidth() - DensityUtils.dp2px(getContext(), triangleWidth), DensityUtils.dp2px(getContext(), marginTop));
        path.lineTo(getWidth(), getHeight() / 2);
        path.lineTo(getWidth() - DensityUtils.dp2px(getContext(), triangleWidth), getHeight() - DensityUtils.dp2px(getContext(), marginTop));
        path.close();
        canvas.drawPath(path, paint);

    }

    public void setColor(String colorString) {
        this.colorString = colorString;
        invalidate();
    }

}
