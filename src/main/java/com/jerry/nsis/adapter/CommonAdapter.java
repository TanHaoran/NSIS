package com.jerry.nsis.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.ViewHolder;

/**
 * 所有适配器的父类
 *
 * @param <T>
 * @author Jerry
 */
@SuppressWarnings("hiding")
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    private STYLE mStyle = STYLE.NONE;

    public enum STYLE {
        NONE, FOUR_SIDE, UP_DOWN_SIDE
    }

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        initAnim();
    }

    public void setStyle(STYLE style) {
        mStyle = style;
    }


    public void setDatas(List<T> datas) {
        if (mDatas != datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }
    }

    public List<T> getDatas() {
        return mDatas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);

        convert(viewHolder, getItem(position));
        if (mStyle == STYLE.FOUR_SIDE) {
            showGridAnimation(viewHolder);
        } else if (mStyle == STYLE.UP_DOWN_SIDE) {
            showListAnimation(position, viewHolder.getConvertView());
        }
        return viewHolder.getConvertView();
    }

    private void showListAnimation(int position, View convertView) {
        if (Math.random() >0.5) {
            slide_bottom_to_top.setDuration(1000);
            convertView.setAnimation(slide_bottom_to_top);
        } else {
            slide_top_to_bottom.setDuration(1000);
            convertView.setAnimation(slide_top_to_bottom);
        }
    }


    private void showGridAnimation(ViewHolder viewHolder) {
        Random ran = new Random();
        int rand = ran.nextInt(4);
        switch (rand) {
            case 0:
                viewHolder.getConvertView().setAnimation(taLeft);
                break;
            case 1:
                viewHolder.getConvertView().setAnimation(taRight);
                break;
            case 2:
                viewHolder.getConvertView().setAnimation(taTop);
                break;
            case 3:
                viewHolder.getConvertView().setAnimation(taBlow);
                break;
        }

    }

    public abstract void convert(ViewHolder helper, T item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }


    private TranslateAnimation taLeft, taRight, taTop, taBlow;
    private Animation slide_top_to_bottom, slide_bottom_to_top;

    private void initAnim() {
        // TODO Auto-generated method stub
        taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        taLeft.setDuration(1000);
        taRight.setDuration(1000);
        taTop.setDuration(1000);
        taBlow.setDuration(1000);

        slide_top_to_bottom = AnimationUtils.loadAnimation(mContext, R.anim.slide_top_to_bottom);
        slide_bottom_to_top = AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_to_top);
    }

}
