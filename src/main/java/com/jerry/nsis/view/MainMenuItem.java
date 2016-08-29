package com.jerry.nsis.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.DensityUtils;

/**
 * Created by Jerry on 2016/2/17.
 */
public class MainMenuItem extends View {

    private Context mContext;
    private int maringTop = 4;
    private Paint mPaint = new Paint();

    private int resourceId = R.drawable.menu_edu;
    private float rotate = 0;
    private String backgroundColor = "#7cdbf7";
    private int index = 0;

    public MainMenuItem(Context context) {
        this(context, null);
    }

    public MainMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MainMenuItem);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MainMenuItem_rotate:
                    rotate = typedArray.getFloat(attr, 0);
                    break;
                case R.styleable.MainMenuItem_imageResource:
                    resourceId = typedArray.getResourceId(attr, R.drawable.menu_edu);
                    break;
                case R.styleable.MainMenuItem_backgroundColor:
                    backgroundColor = typedArray.getString(attr);
                    break;
                case R.styleable.MainMenuItem_index:
                    index = typedArray.getInteger(attr, 0);
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
        // 去锯齿
        mPaint.setAntiAlias(true);
        // 设置实心图形
        mPaint.setStyle(Paint.Style.FILL);
        // 设置paint的颜色
        mPaint.setColor(Color.parseColor(backgroundColor));


        // 画三角形
        Path path = new Path();
        path.moveTo(0, DensityUtils.dp2px(getContext(), getHeight() / 2));
        path.lineTo(DensityUtils.dp2px(getContext(), getHeight() / 3 * 2), DensityUtils.dp2px(getContext(), maringTop));
        path.lineTo(DensityUtils.dp2px(getContext(), getHeight() / 3 * 2), DensityUtils.dp2px(getContext(), getHeight() - maringTop));
        path.close();
        canvas.drawPath(path, mPaint);

        // 画圆
        canvas.drawCircle(DensityUtils.dp2px(mContext, getHeight() / 9 * 8), DensityUtils.dp2px(mContext, getHeight() / 2),
                DensityUtils.dp2px(mContext, getHeight() / 2), mPaint);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(DensityUtils.dp2px(mContext, getHeight() / 9 * 8), DensityUtils.dp2px(mContext, getHeight() / 2),
                DensityUtils.dp2px(mContext, getHeight() / 2 / 5 * 4), mPaint);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);


        // 定义矩阵对象
        Matrix matrix = new Matrix();
        // 缩放原图
        if (index == 0) {
            matrix.postScale(0.8f, 0.8f);
        } else if (index == 1) {
            matrix.postScale(0.8f, 0.8f);
        } else if (index == 2) {
            matrix.postScale(0.85f, 0.85f);
        } else if (index == 3) {
            matrix.postScale(0.8f, 0.8f);
        } else if (index == 4) {
            matrix.postScale(0.7f, 0.7f);
        }
        // 向左旋转45度，参数为正则向右旋转
        //  matrix.postRotate(rotate, bitmap.getWidth()/2, bitmap.getHeight()/2);
        matrix.postRotate(rotate);
        matrix.postTranslate(10, 0);
        //bmp.getWidth(), 500分别表示重绘后的位图宽高
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        // 在画布上绘制旋转后的位图
        if (index == 0) {
            canvas.drawBitmap(dstbmp, DensityUtils.dp2px(getContext(), getHeight() / 5 * 3), 14, null);
        } else if (index == 1) {
            canvas.drawBitmap(dstbmp, DensityUtils.dp2px(getContext(), getHeight() / 5 * 3) - 4, 11, null);
        } else if (index == 2) {
            canvas.drawBitmap(dstbmp, DensityUtils.dp2px(getContext(), getHeight() / 5 * 3) - 3, 16, null);
        } else if (index == 3) {
            canvas.drawBitmap(dstbmp, DensityUtils.dp2px(getContext(), getHeight() / 5 * 3) - 9, 8, null);
        } else if (index == 4) {
            canvas.drawBitmap(dstbmp, DensityUtils.dp2px(getContext(), getHeight() / 5 * 3) -2 , 10, null);
        }
    }

}
