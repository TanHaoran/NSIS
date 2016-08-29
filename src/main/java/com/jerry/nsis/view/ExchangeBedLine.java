package com.jerry.nsis.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.activity.ExchangeBedActivity;
import com.jerry.nsis.activity.MainActivity;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.L;

/**
 * Created by Jerry on 2016/2/17.
 */
public class ExchangeBedLine extends RelativeLayout {

    private Context mContext;

    private TextView mTextFrom;
    private TextView mTextTo;

    private ImageView mDelete;

    private String transId;

    public ExchangeBedLine(Context context) {
        this(context, null);
    }

    public ExchangeBedLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExchangeBedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_exchangebed_line, this);
        mTextFrom = (TextView) findViewById(R.id.tv_from);
        mTextTo = (TextView) findViewById(R.id.tv_to);
        mDelete = (ImageView) findViewById(R.id.iv_delete);

        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeBedActivity.mChange = true;
                deleteExchangeBed(LoginInfo.OFFICE_ID, transId);
                ExchangeBedLine.this.setVisibility(GONE);
            }
        });
    }

    public void setFrom(String from) {
        mTextFrom.setText(from);
    }

    public void setTo(String to) {
        mTextTo.setText(to);
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    /**
     * 删除换床信息
     *
     * @param officeId
     */
    private void deleteExchangeBed(final String officeId, String transId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.DELETE_TRAN_BED_RECORD
                + "?officeid=" + officeId + "&TranID=" + transId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                L.i("删除换床信息返回值：" + json);
                // 发送广播，通知主界面修改UI
                Intent intent = new Intent();
                intent.setAction(MainActivity.EXCHANGE_UPDATE);
                mContext.sendBroadcast(intent);
            }

        };
        getUtil.doGet(mContext, url, "删除换床信息");
    }

}
