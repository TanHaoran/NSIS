package com.jerry.nsis.adapter;

import android.content.Context;
import android.view.View;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Office;
import com.jerry.nsis.entity.Print;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jerry on 2016/2/25.
 */
public class OfficeAdapter extends CommonAdapter<Office> {

    public OfficeAdapter(Context context, List<Office> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Office item) {
        helper.setText(R.id.tv_name, item.getName());
        if (item.isCheck()) {
            helper.getView(R.id.rl_check).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.rl_check).setVisibility(View.INVISIBLE);
        }
    }
}
