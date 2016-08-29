package com.jerry.nsis.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.activity.EducationActivity;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Education;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.StreamUtil;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;

/**
 * Created by Jerry on 2016/2/26.
 */
public class EducationAdapter extends CommonAdapter<Education> {

    private Context mContext;


    public EducationAdapter(Context context, List<Education> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        mContext = context;
    }

    @Override
    public void convert(ViewHolder helper, Education item) {
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tv_negative, String.valueOf(item.getNegative()));
        helper.setText(R.id.tv_positive, String.valueOf(item.getPositive()));
        helper.setText(R.id.tv_read, String.valueOf(item.getRead()));
        helper.setText(R.id.tv_time, item.getTime());
        if (!TextUtils.isEmpty(item.getImg())) {
            helper.setImageBitmap(R.id.iv_img, StreamUtil.base64ToBitmap(item.getImg()));
        } else {
            helper.setImageResource(R.id.iv_img, R.drawable.nurseeducationitem);
        }
        if (item.isRecommend()) {
            helper.getView(R.id.iv_recommend).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.iv_recommend).setVisibility(View.INVISIBLE);
        }

        helper.getView(R.id.ll_positive).setOnClickListener(new ClickListener(helper, item));
        helper.getView(R.id.ll_negative).setOnClickListener(new ClickListener(helper, item));
    }

    class ClickListener implements View.OnClickListener {

        private Education edu;
        private ViewHolder helper;

        public ClickListener(ViewHolder helper, Education edu) {
            this.helper = helper;
            this.edu = edu;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_positive:
                    int positive = edu.getPositive() + 1;
                    edu.setPositive(positive);
                    helper.setText(R.id.tv_positive, String.valueOf(positive));
                    setPositive(edu.getId());
                    break;
                case R.id.ll_negative:
                    int negative = edu.getNegative() + 1;
                    edu.setNegative(negative);
                    helper.setText(R.id.tv_negative, String.valueOf(negative));
                    setNegative(edu.getId());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 点赞
     */
    private void setPositive(String eduId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.MODIFY_NURSING_EDUCATION_BY_PRAISE_NUMBER +
                "?EducationID=" + eduId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                L.i("点赞返回值：" + json);
            }

        };
        getUtil.doGet(mContext, url, "点赞");
    }

    /**
     * 点差
     */
    private void setNegative(String eduId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.MODIFY_NURSING_EDUCATION_BY_BAD_REVIEW_NUMBER +
                "?EducationID=" + eduId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                L.i("点差返回值：" + json);
            }

        };
        getUtil.doGet(mContext, url, "点差");
    }
}
