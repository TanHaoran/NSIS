package com.jerry.nsis.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.CustmoAllAdapter;
import com.jerry.nsis.adapter.CustmoNowAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.entity.Prompt;
import com.jerry.nsis.entity.PromptBed;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.T;
import com.jerry.nsis.view.TriangleArrow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CustomPromptActivity extends Activity {


    private TriangleArrow mArrow;
    private TextView mTextTitle;
    private ImageView mClose;

    private GridView mGridViewAll;
    private GridView mGridViewNow;

    private CustmoAllAdapter mAllAdapter;
    private CustmoNowAdapter mNowAdapter;

    private List<PromptBed> mPromptList;

    private Prompt mPrompt;

    public static final int NURSING_DETAIL_UPDATE = 101;


    private boolean mChange = false;

    private List<Patient> mPatientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_custom);
        ActivityUtil.makeActivity2Dialog(this);
        initView();
        mPatientList = new ArrayList<>();
        for (Patient p : LoginInfo.mPatientList) {
            mPatientList.add(p);
        }
        setData();
    }

    private void initView() {
        mArrow = (TriangleArrow) findViewById(R.id.arrow);
        mTextTitle = (TextView) findViewById(R.id.tv_title);
        mClose = (ImageView) findViewById(R.id.iv_close);

        mGridViewAll = (GridView) findViewById(R.id.gv_all);
        mGridViewNow = (GridView) findViewById(R.id.gv_now);


        Intent intent = getIntent();
        mArrow = (TriangleArrow) findViewById(R.id.arrow);
        mTextTitle = (TextView) findViewById(R.id.tv_title);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mPrompt = (Prompt) intent.getSerializableExtra("prompt");
        if (mPrompt != null) {
            mArrow.setColor(mPrompt.getColor());
            mTextTitle.setTextColor(Color.parseColor(mPrompt.getColor()));
            mTextTitle.setText(mPrompt.getTitle());
            mPromptList = mPrompt.getBedList();
        }

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChange) {
                    setResult(NURSING_DETAIL_UPDATE);
                }
                finish();
            }
        });


        // 左侧GridView
        mGridViewAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChange = true;
                Patient p = mPatientList.get(position);
                PromptBed pb = new PromptBed();
                pb.setBed(p.getBedNo());
                pb.setName(p.getName());
                pb.setSex(p.getSex());
                pb.setHosId(p.getHosId());
                pb.setInTimes(p.getInTimes());
                pb.setDetailId(p.getDetailId());
                saveToServer(LoginInfo.OFFICE_ID, mPrompt.getItemId(), mPrompt.getFrequencyId(), position, pb, false);
            }
        });

        // 右侧GridView
        mGridViewNow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChange = true;
                PromptBed pb = mPromptList.get(position);
                L.i(pb.toString());
                Patient p = new Patient();
                p.setBedNo(pb.getBed());
                p.setName(pb.getName());
                p.setSex(pb.getSex());
                p.setHosId(pb.getHosId());
                p.setInTimes(pb.getInTimes());
                p.setDetailId(pb.getDetailId());
                deleteToServer(LoginInfo.OFFICE_ID, mPromptList.get(position).getDetailId(), position, p, false);

            }
        });
    }

    /**
     * 删除护理项目明细
     */
    private void deleteToServer(String officeId, String detailId, final int position, final Patient p, boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
                ServiceConstant.DELETE_ITEM_RECORD_INFO + "?officeid=" + officeId
                + "&DetailID=" + detailId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                String result = JsonUtil.getResultFromJson(json);
                L.i("返回值" + result);
                if (result.equals("1")) {
                    mPromptList.remove(position);
                    mPatientList.add(p);
                    mAllAdapter.notifyDataSetChanged();
                    mNowAdapter.notifyDataSetChanged();
                } else {
                    T.showLong(CustomPromptActivity.this, "移除失败");
                }

            }
        };
        getUtil.doGet(this, url, isUpdate, "删除护理项目");

    }

    /**
     * 保存新的护理项目明细
     */
    private void saveToServer(String officeId, String itemId, String frequencyId,
                              final int position, final PromptBed pb, boolean isUpdate) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.SAVE_NURSE_ITEM +
                    "?officeid=" + officeId
                    + "&ItemID=" + itemId + "&PeriodID=" + frequencyId + "&InhosId=" + pb.getHosId()
                    + "&InhosTimes=" + pb.getInTimes() + "&BedID=" + pb.getBed() + "&ItemContent=" +
                    URLEncoder.encode(pb.getName(), "UTF-8");
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    PromptBed prompt = JsonUtil.getPbFromJson(json);
                    pb.setDetailId(prompt.getDetailId());
                    L.i("返回值" + json);
                    mPatientList.remove(position);
                    mPromptList.add(pb);
                    mAllAdapter.notifyDataSetChanged();
                    mNowAdapter.notifyDataSetChanged();
                }
            };
            getUtil.doGet(this, url, isUpdate, "保存护理项目");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        for (PromptBed pb : mPromptList) {
            for (int i = 0; i < mPatientList.size(); i++) {
                Patient p = mPatientList.get(i);
                if (p.getBedNo().equals(pb.getBed()) && (p.getHosId().equals(pb.getHosId()))
                        && (p.getInTimes() == pb.getInTimes())) {
                    mPatientList.remove(i);
                }
            }
        }
        mAllAdapter = new CustmoAllAdapter(this, mPatientList, R.layout.item_custom_all_bed);
        mNowAdapter = new CustmoNowAdapter(this, mPromptList, R.layout.item_custom_now_bed);

        mGridViewAll.setAdapter(mAllAdapter);
        mGridViewNow.setAdapter(mNowAdapter);
    }
}
