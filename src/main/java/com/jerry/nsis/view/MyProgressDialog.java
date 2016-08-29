package com.jerry.nsis.view;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.nsis.R;

/**
 * Created by Jerry on 2016/3/8.
 */
public class MyProgressDialog extends ProgressDialog {

    private ImageView mImageView;
    private TextView mTextLoading;

    public MyProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        RotateAnimation animation = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1000);//设置动画持续时间
        animation.setRepeatCount(-1); // 动画不停止
        mImageView.startAnimation(animation);
    }


    private void initView() {
        setContentView(R.layout.progress_dialog);
        mImageView = (ImageView) findViewById(R.id.iv_loading);
        mTextLoading = (TextView) findViewById(R.id.tv_loading);
    }

    /**
     * 设置等待框的文字
     * @param msg
     */
    public void setContent(String msg) {
        mTextLoading.setText(msg);
    }



}
