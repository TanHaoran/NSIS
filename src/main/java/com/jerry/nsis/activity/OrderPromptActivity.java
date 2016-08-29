package com.jerry.nsis.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.OrderPromptAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.NursingNote;
import com.jerry.nsis.entity.Prompt;
import com.jerry.nsis.entity.PromptBed;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.view.TriangleArrow;

import java.util.List;

public class OrderPromptActivity extends Activity {

    private TriangleArrow mArrow;
    private TextView mTextTitle;
    private ImageView mClose;

    private TextView mTextNumber;

    private GridView mGridView;
    private OrderPromptAdapter mAdapter;

    private List<NursingNote> mNoteList;

    private Prompt mPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_prompt);
        ActivityUtil.makeActivity2Dialog(this);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        mArrow = (TriangleArrow) findViewById(R.id.arrow);
        mTextNumber = (TextView) findViewById(R.id.tv_number);
        mTextTitle = (TextView) findViewById(R.id.tv_title);
        mGridView = (GridView) findViewById(R.id.gridview);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mPrompt = (Prompt) intent.getSerializableExtra("prompt");
        if (mPrompt != null) {
            mArrow.setColor(mPrompt.getColor());
            mTextTitle.setTextColor(Color.parseColor(mPrompt.getColor()));
            mTextTitle.setText(mPrompt.getTitle());
            mTextNumber.setText(String.valueOf(mPrompt.getBedList().size()));
        }
        if (mPrompt != null && !JsonUtil.isEmpty(mPrompt.getBedList())) {
            loadNursingDetail(LoginInfo.OFFICE_ID, mPrompt.getItemId());
        }
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 读取popupwindow备注信息
     *
     * @param officeId
     * @param itemId
     */
    private void loadNursingDetail(String officeId, String itemId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_NURSING_DETAIL_BY_ITEM_ID
                + "?officeid=" + officeId + "&ItemID=" + itemId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mNoteList = JsonUtil.getNursingNoteFromJson(json);
                L.i("共读取到备注信息数量：" + mNoteList.size());
                setData();
            }
        };
        getUtil.doGet(this, url, "备注信息");
    }

    private void setData() {
        for (PromptBed pb : mPrompt.getBedList()) {
            for (NursingNote nn : mNoteList) {
                if (nn.getId().equals(pb.getDetailId())) {
                    pb.setTag(nn.getContent());
                }
            }
        }
        mAdapter = new OrderPromptAdapter(this, mPrompt.getBedList(), R.layout.item_order_bed,mGridView);
        mGridView.setAdapter(mAdapter);
    }
}
