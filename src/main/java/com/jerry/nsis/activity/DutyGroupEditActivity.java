package com.jerry.nsis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.CommonAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.DutyGroup;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.ViewHolder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DutyGroupEditActivity extends Activity {

    private TextView mTextName;
    private ImageView mClose;
    private GridView mGridView;
    private Button mSave;
    private List<Bed> mBedList;


    private DutyGroup group;
    private List<String> noList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_duty_group_edit);
        ActivityUtil.makeActivity2Dialog(this);

        getInfo();

        initView();
        loadBedScanData(LoginInfo.OFFICE_ID, false);
    }

    /**
     * 获取所有床位信息
     */
    private void loadBedScanData(final String officeId, boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_BED_FILE_EXIST_EMPTY_ALL + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mBedList = JsonUtil.getBedInfolFromJson(json);
                setBedData();
            }

        };
        getUtil.doGet(this, url, isUpdate, "读取床位");
    }

    /**
     * 保存责任分组信息
     */
    private void saveDutyBedData(final String officeId, String groupId, List<Bed> bedList) {
        StringBuilder sb = new StringBuilder();
        for (Bed b : bedList) {
            if (b.isSelected()) {
                sb.append(b.getHisBedNo() + ",");
            }
        }
        String nos;
        if (sb.toString().endsWith(",")) {
            nos = sb.substring(0, sb.length() - 1);
        } else {
            nos = sb.toString();
        }
        L.i("所有床：" + nos);

        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.SAVE_WEB_RESPONSIBILITY_GROUP +
                    "?officeid=" + officeId + "&responsibilityID=" + groupId + "&bedinfos=" + URLEncoder.encode(nos, "UTF-8");
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    L.i("保存返回值" + json);
                    Intent intent = new Intent();
                    intent.setAction(DutyGroupActivity.GROUP_UPDATE);
                    sendBroadcast(intent);
                    finish();
                }

            };
            getUtil.doGet(this, url, "读取床位");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setBedData() {
        if (noList != null) {
            for (Bed b : mBedList) {
                for (String no : noList) {
                    if (no.equals(b.getNo())) {
                        b.setSelected(true);
                        break;
                    }
                }
            }
        }

        mGridView.setAdapter(new CommonAdapter<Bed>(this, mBedList, R.layout.item_duty_group_bed) {
            @Override
            public void convert(ViewHolder helper, Bed item) {
                helper.setText(R.id.tv_no, item.getNo());
                if (item.isSelected()) {
                    helper.getView(R.id.iv_select).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.iv_select).setVisibility(View.INVISIBLE);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView selectView = (ImageView) view.findViewById(R.id.iv_select);
                if (mBedList.get(position).isSelected()) {
                    selectView.setVisibility(View.INVISIBLE);
                    mBedList.get(position).setSelected(false);
                } else {
                    selectView.setVisibility(View.VISIBLE);
                    mBedList.get(position).setSelected(true);
                }
            }
        });
    }

    private void initView() {
        mTextName = (TextView) findViewById(R.id.tv_name);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mGridView = (GridView) findViewById(R.id.gridview);
        mSave = (Button) findViewById(R.id.btn_save);

        if (group != null) {
            mTextName.setText(group.getName());
        }

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (group != null) {
                    saveDutyBedData(LoginInfo.OFFICE_ID, group.getId(), mBedList);
                }
            }
        });
    }


    public void getInfo() {
        group = (DutyGroup) getIntent().getSerializableExtra("group");
        if (group != null) {
            noList = group.getNoList();
        }
    }
}
