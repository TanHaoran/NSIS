package com.jerry.nsis.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.entity.PromptBed;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jerry on 2016/2/18.
 */
public class CustmoAllAdapter extends CommonAdapter<Patient> {

    private Context mContext;
    private List<Patient> mDatas;

    public CustmoAllAdapter(Context context, List<Patient> datas, int itemLayoutId) {
        super(context, datas, itemLayoutId);
        mContext = context;
        mDatas = datas;
    }

    @Override
    public void convert(ViewHolder helper, Patient item) {
        if (!TextUtils.isEmpty(item.getBedNo())) {
            helper.setText(R.id.tv_bed, item.getBedNo());
        } else {
            helper.setText(R.id.tv_bed, "空");
        }
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_sex, item.getSex());
        helper.setText(R.id.tv_hosid, item.getHosId());
    }


}
