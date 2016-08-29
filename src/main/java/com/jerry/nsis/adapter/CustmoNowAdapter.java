package com.jerry.nsis.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.PromptBed;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jerry on 2016/2/18.
 */
public class CustmoNowAdapter extends CommonAdapter<PromptBed> {

    private Context mContext;
    private List<PromptBed> mDatas;

    public CustmoNowAdapter(Context context, List<PromptBed> datas, int itemLayoutId) {
        super(context, datas, itemLayoutId);
        mContext = context;
        mDatas = datas;
    }

    @Override
    public void convert(ViewHolder helper, PromptBed item) {
        if (!TextUtils.isEmpty(item.getBed())) {
            helper.setText(R.id.tv_bed, item.getBed());
        } else {
            helper.setText(R.id.tv_bed, "空");
        }
        helper.setText(R.id.tv_name, item.getName());
    }
}
