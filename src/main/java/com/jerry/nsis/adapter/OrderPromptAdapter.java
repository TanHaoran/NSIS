package com.jerry.nsis.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.entity.PromptBed;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jerry on 2016/2/18.
 */
public class OrderPromptAdapter extends CommonAdapter<PromptBed> {

    private Context mContext;
    private List<PromptBed> mDatas;

    private GridView mGridView;


    public OrderPromptAdapter(Context context, List<PromptBed> datas, int itemLayoutId, GridView gridView) {
        super(context, datas, itemLayoutId);
        mContext = context;
        mDatas = datas;
        mGridView = gridView;
    }

    @Override
    public void convert(ViewHolder helper, PromptBed item) {

        if (!TextUtils.isEmpty(item.getBed())) {
            helper.setText(R.id.tv_bed, item.getBed());
        } else {
            helper.setText(R.id.tv_bed, "空");
        }
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_sex, item.getSex());
        helper.setText(R.id.tv_hosid, item.getHosId());
        if (item.getTag() != null) {
            String tag = item.getTag().replace("rn", "\n");
            helper.setText(R.id.tv_tag, tag);
        }
        helper.getView(R.id.scrollView).setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View arg0, MotionEvent arg1) {
                mGridView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

}
