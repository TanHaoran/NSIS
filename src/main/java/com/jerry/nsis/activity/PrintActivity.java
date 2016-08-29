package com.jerry.nsis.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.OfficeAdapter;
import com.jerry.nsis.entity.Print;
import com.jerry.nsis.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 护理项目打印界面
 */
public class PrintActivity extends Activity {

    private LinearLayout mPrintLayout;
    private GridView mGridView;

    private ImageView mClose;

    private Button mPrintButton;
    private Button mExitButton;

    private OfficeAdapter mAdapter;

    private List<Print> mPrintList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_print);
        ActivityUtil.makeActivity2Dialog(this);
        initView();
        setListener();
        loadData();
    }

    private void loadData() {
        mPrintList = new ArrayList<Print>();
        for (int i = 0; i < 28; i++) {
            mPrintList.add(new Print("精神病科", 28, false));
            mPrintList.add(new Print("皮肤科", 12, true));
            mPrintList.add(new Print("神经病科", 2, false));
        }
        mAdapter = new OfficeAdapter(this, null, R.layout.item_office);
        mGridView.setAdapter(mAdapter);
    }


    private void initView() {
        mPrintLayout = (LinearLayout) findViewById(R.id.ll_print);
        mGridView = (GridView) findViewById(R.id.gv_print);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mPrintButton = (Button) findViewById(R.id.btn_print);
        mExitButton = (Button) findViewById(R.id.btn_exit);
    }

    private void setListener() {
        mPrintLayout.setOnClickListener(mOnClickListener);
        mClose.setOnClickListener(mOnClickListener);
        mPrintButton.setOnClickListener(mOnClickListener);
        mExitButton.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_print:
                    break;
                case R.id.gv_print:
                    break;
                case R.id.iv_close:
                    finish();
                    break;
                case R.id.btn_print:
                    break;
                case R.id.btn_exit:
                    finish();
                    break;
            }
        }
    };
}
