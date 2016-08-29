package com.jerry.nsis.adapter;

import android.content.Context;
import android.view.View;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.ExchangeBed;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jerry on 2016/2/18.
 */
public class ExchangeBedAdapter extends CommonAdapter<ExchangeBed> {

    private Context mContext;
    private List<ExchangeBed> mDatas;

    public ExchangeBedAdapter(Context context, List<ExchangeBed> datas, int itemLayoutId) {
        super(context, datas, itemLayoutId);
        mContext = context;
        mDatas = datas;
    }

    @Override
    public void convert(ViewHolder helper, ExchangeBed item) {
        helper.setText(R.id.tv_no, item.getBedNo());
        if (item.getStatus() == 1) {
            helper.getView(R.id.iv_bed).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_bed).setVisibility(View.INVISIBLE);
        }
    }
}
