package com.jerry.nsis.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Order;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jerry on 2016/2/25.
 */
public class OrderAdapter extends CommonAdapter<Order> {

    public OrderAdapter(Context context, List<Order> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Order item) {
        if (item.getStatus() == Order.STOP) {
            helper.setText(R.id.tv_state, Order.STOP_STRING);
        } else if (item.getStatus() == Order.ABANDON) {
            helper.setText(R.id.tv_state, Order.ABANDON_SRING);
        } else {
            helper.setText(R.id.tv_state, Order.EXE_SRING);
        }
        helper.setText(R.id.tv_starttime, item.getStartTime());
        helper.setText(R.id.tv_type, item.getType());
        helper.setText(R.id.tv_content, item.getContent());
        helper.setText(R.id.tv_way, item.getWay());
        helper.setText(R.id.tv_frequency, item.getFrequency());
        helper.setText(R.id.tv_dose, item.getDose() + item.getUnit());
        helper.setText(R.id.tv_note, item.getNote());
        helper.setText(R.id.tv_ensuretime, item.getEnsureTime());

        // 4作废，3停止
        if (item.getStatus() == Order.ABANDON) {
            ((TextView) (helper.getView(R.id.tv_state))).setTextColor(Color.parseColor("#e8586b"));
            helper.setText(R.id.tv_state, Order.ABANDON_SRING);
        } else if (item.getStatus() == Order.STOP) {
            ((TextView) (helper.getView(R.id.tv_state))).setTextColor(Color.parseColor("#f39756"));
            helper.setText(R.id.tv_state, Order.STOP_STRING);
        } else if (item.getOrderType().equals(Order.TEMP_ORDER) &&
                DateUtil.compareDate(item.getStartTime(), DateUtil.getYMDHMS()) < 0) {
            ((TextView) (helper.getView(R.id.tv_state))).setTextColor(Color.parseColor("#f39756"));
            helper.setText(R.id.tv_state, Order.STOP_STRING);
        } else {
            ((TextView) (helper.getView(R.id.tv_state))).setTextColor(Color.parseColor("#000000"));
            helper.setText(R.id.tv_state, Order.EXE_SRING);
        }
    }


    private void setTextColor(ViewHolder helper, String color) {
        ((TextView) helper.getView(R.id.tv_starttime)).setTextColor(Color.parseColor(color));
        ((TextView) helper.getView(R.id.tv_type)).setTextColor(Color.parseColor(color));
        ((TextView) helper.getView(R.id.tv_content)).setTextColor(Color.parseColor(color));
        ((TextView) helper.getView(R.id.tv_way)).setTextColor(Color.parseColor(color));
        ((TextView) helper.getView(R.id.tv_frequency)).setTextColor(Color.parseColor(color));
        ((TextView) helper.getView(R.id.tv_dose)).setTextColor(Color.parseColor(color));
        ((TextView) helper.getView(R.id.tv_note)).setTextColor(Color.parseColor(color));
        ((TextView) helper.getView(R.id.tv_ensuretime)).setTextColor(Color.parseColor(color));
    }
}
