package com.jerry.nsis.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.Random;

/**
 * Created by Jerry on 2016/2/18.
 */
public class AnimationUtil {

    private AnimationUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 主界面重要护理项目的显示和隐藏动画
     *
     * @param hide 隐藏的布局
     * @param show 显示的布局
     */
    public static void showTagAnimation(final View hide, final View show) {
        PropertyValuesHolder alphaHide = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        PropertyValuesHolder scaleXHide = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder scaleYHide = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        ObjectAnimator hideAnim = ObjectAnimator.ofPropertyValuesHolder(hide, alphaHide, scaleXHide, scaleYHide).setDuration(500);

        PropertyValuesHolder alphaShow = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder scaleX2Show = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder scaleY2Show = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        final ObjectAnimator showAnim = ObjectAnimator.ofPropertyValuesHolder(show, alphaShow, scaleX2Show, scaleY2Show).setDuration(500);

        hideAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hide.setVisibility(View.INVISIBLE);
                show.setVisibility(View.VISIBLE);
                showAnim.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        hideAnim.start();
    }

    /**
     * 主界面中心菜单的显示
     *
     * @param view 中心菜单
     */
    public static void menuShow(View view) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        final ObjectAnimator showAnim = ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY).setDuration(500);
        showAnim.setInterpolator(new OvershootInterpolator());
        showAnim.start();
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 主界面中心菜单的隐藏
     *
     * @param view 中心菜单
     */
    public static void menuHide(final View view) {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);
        final ObjectAnimator showAnim = ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY).setDuration(500);
        showAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        showAnim.start();
    }

    /**
     * 设置标签的放大缩小动画效果
     *
     * @param zoomIn  放大的控件
     * @param zoomOut 缩小的控件
     */
    public static void eduZoomAnim(View zoomIn, View... zoomOut) {
        PropertyValuesHolder scaleXZoomIn = PropertyValuesHolder.ofFloat("scaleX", 1F, 1.4F);
        PropertyValuesHolder scaleYZoomIn = PropertyValuesHolder.ofFloat("scaleY", 1F, 1.4F);
        ObjectAnimator.ofPropertyValuesHolder(zoomIn, scaleXZoomIn, scaleYZoomIn).setDuration(500).start();

        for (View v : zoomOut) {
            if (v.getScaleX() != 1.0) {
                PropertyValuesHolder scaleXZoomOut = PropertyValuesHolder.ofFloat("scaleX", 1.4F, 1F);
                PropertyValuesHolder scaleYZoomOut = PropertyValuesHolder.ofFloat("scaleY", 1.4F, 1F);
                ObjectAnimator.ofPropertyValuesHolder(v, scaleXZoomOut, scaleYZoomOut).setDuration(500).start();
            }
        }
    }

    /**
     * 设置标签的放大缩小动画效果
     *
     * @param zoomIn  放大的控件
     * @param zoomOut 缩小的控件
     */
    public static void eduZoomAnim(View zoomIn, float size, View... zoomOut) {
        if (zoomIn.getScaleY() != 1.0f) {
            return;
        }
        PropertyValuesHolder scaleYZoomIn = PropertyValuesHolder.ofFloat("scaleY", 1F, size);
        ObjectAnimator.ofPropertyValuesHolder(zoomIn, scaleYZoomIn).setDuration(500).start();

        for (View v : zoomOut) {
            if (v.getScaleY() != 1.0) {
                PropertyValuesHolder scaleYZoomOut = PropertyValuesHolder.ofFloat("scaleY", size, 1F);
                ObjectAnimator.ofPropertyValuesHolder(v, scaleYZoomOut).setDuration(500).start();
            }
        }
    }


    /**
     * 随机获取一个从0到360之间的度数
     *
     * @return
     */
    public static int getRandomRotate() {
        Random random = new Random();
        return random.nextInt(360);
    }
}
