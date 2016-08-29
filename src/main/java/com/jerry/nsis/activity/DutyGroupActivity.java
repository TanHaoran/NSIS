package com.jerry.nsis.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.DutyGroup;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.view.DutyGroupBed;
import com.jerry.nsis.view.DutyGroupLine;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任分组页面
 */
@ContentView(R.layout.activity_duty_group)
public class DutyGroupActivity extends Activity {

    @ViewInject(R.id.tv_office)
    private TextView mTextOffice;

    @ViewInject(R.id.ll_leftlayout)
    private LinearLayout mLeftLayout;
    @ViewInject(R.id.ll_rightlayout)
    private LinearLayout mRightLayout;
    private List<DutyGroup> mDutyGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        initView();
        loadOnedayData(LoginInfo.OFFICE_ID);
        registerReceiver(mReceiver, mFilter);
    }

    private void initView() {
        mTextOffice.setText(LoginInfo.OFFICE_NAME);
    }


    /**
     * 读取责任分组
     */
    private void loadOnedayData(String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.FIND_WEB_RESPONSIBILITY_GROUP_BY_OFFICE_ID
                + "?officeID=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mDutyGroupList = JsonUtil.getDutyGroupFromJson(json);
                L.i("共读取到责任分组数量：" + mDutyGroupList.size());
                if (!JsonUtil.isEmpty(mDutyGroupList)) {
                    setDutyGroupData();
                }
            }
        };
        getUtil.doGet(this, url, "责任分组");
    }


    private void setDutyGroupData() {
        mLeftLayout.removeAllViews();
        mRightLayout.removeAllViews();
        for (int i = 0; i < mDutyGroupList.size(); i++) {
            DutyGroup dutyGroup = mDutyGroupList.get(i);
            DutyGroupLine line = new DutyGroupLine(this);
            if (!JsonUtil.isEmpty(dutyGroup.getNoList())) {
                List<DutyGroupBed> bedList = new ArrayList<>();
                ArrayList<String> noList = new ArrayList();
                for (String no : dutyGroup.getNoList()) {
                    DutyGroupBed bed = new DutyGroupBed(this);
                    bed.setText(no);
                    bedList.add(bed);
                    noList.add(no);
                }
                line.setBedList(bedList);
            }
            line.setName(dutyGroup.getName());
            line.setGroup(dutyGroup);
            if (i <= 7) {
                mLeftLayout.addView(line);
            } else {
                mRightLayout.addView(line);
            }
        }
    }

    @OnClick(R.id.iv_close)
    public void onClose(View v) {
        finish();
    }

    public static final String GROUP_UPDATE = "group_update";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.i("检测到责任分组变化了！");
            loadOnedayData(LoginInfo.OFFICE_ID);
        }
    };

    private IntentFilter mFilter = new IntentFilter(GROUP_UPDATE);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
