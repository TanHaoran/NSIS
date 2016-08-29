package com.jerry.nsis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.ExchangeBedAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Exchange;
import com.jerry.nsis.entity.ExchangeBed;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.view.ExchangeBedLine;

import java.util.ArrayList;
import java.util.List;

public class ExchangeBedActivity extends Activity {


    // 控制标签
    private static int EXCHANGE_ALL = 0;
    private static int EXCHANGE_USE = 1;
    private static int EXCHANGE_EMPTY = 2;
    private int EXCHANGE_TAG = EXCHANGE_ALL;

    private ImageView mClose;

    private LinearLayout mExchangeLayout;

    private TextView mTextAll;
    private TextView mTextUse;
    private TextView mTextEmpty;
    private TextView mTexts[];

    private TextView mBedFrom;
    private TextView mBedTo;

    private Button mSave;

    // 0表示在from的位置，1表示to的位置
    private int mCursor = 0;

    private List<ExchangeBed> mAllBedList;
    private List<ExchangeBed> mEmptyBedList;
    private List<ExchangeBed> mUseBedList;

    private GridView mGridView;
    private ExchangeBedAdapter mAdapter;

    public static boolean mChange = false;

    public static final int EXCHANGE_UPDATE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_exchangebed);
        ActivityUtil.makeActivity2Dialog(this);

        initView();
        initListener();
        initData();
    }

    private void initData() {
        for (Exchange ex : LoginInfo.mExchangeList) {
            ExchangeBedLine line = new ExchangeBedLine(this);
            line.setFrom(ex.getOldBed());
            line.setTo(ex.getNewBed());
            line.setTransId(ex.getId());
            mExchangeLayout.addView(line);
        }
        loadExchangeBed(LoginInfo.OFFICE_ID);
    }


    /**
     * 初始化布局
     */
    private void initView() {
        mClose = (ImageView) findViewById(R.id.iv_close);
        mExchangeLayout = (LinearLayout) findViewById(R.id.ll_exchange);

        mTextAll = (TextView) findViewById(R.id.tv_all);
        mTextUse = (TextView) findViewById(R.id.tv_use);
        mTextEmpty = (TextView) findViewById(R.id.tv_empty);
        mTexts = new TextView[]{mTextAll, mTextUse, mTextEmpty};

        mBedFrom = (TextView) findViewById(R.id.tv_from);
        mBedTo = (TextView) findViewById(R.id.tv_to);

        mSave = (Button) findViewById(R.id.btn_save);

        mGridView = (GridView) findViewById(R.id.gv_bed);
    }

    private void initListener() {
        mClose.setOnClickListener(mOnClickListener);
        mTextAll.setOnClickListener(mOnClickListener);
        mTextUse.setOnClickListener(mOnClickListener);
        mTextEmpty.setOnClickListener(mOnClickListener);
        mBedFrom.setOnClickListener(mOnClickListener);
        mBedTo.setOnClickListener(mOnClickListener);

        mSave.setOnClickListener(mOnClickListener);

        mGridView.setOnItemClickListener(mItemClickListener);
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mCursor == 0) {
                fillBedData(mBedFrom, position);
                mBedTo.performClick();
                mCursor = 1;
            } else {
                fillBedData(mBedTo, position);
            }
        }
    };

    /**
     * 向EditText中填充数据
     *
     * @param editText
     * @param position
     */
    private void fillBedData(TextView editText, int position) {
        L.i("标签是：" + EXCHANGE_TAG);
        if (EXCHANGE_TAG == EXCHANGE_ALL) {
            editText.setText(mAllBedList.get(position).getBedNo());
        } else if (EXCHANGE_TAG == EXCHANGE_EMPTY) {
            editText.setText(mEmptyBedList.get(position).getBedNo());
        } else if (EXCHANGE_TAG == EXCHANGE_USE) {
            editText.setText(mUseBedList.get(position).getBedNo());
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_close:
                    if (mChange) {
                        L.i("已设置好返回值！");
                        // 发送广播，通知主界面修改UI
                        Intent intent = new Intent();
                        intent.setAction(MainActivity.EXCHANGE_UPDATE);
                        sendBroadcast(intent);
                    }
                    finish();
                    break;
                case R.id.tv_all:
                    changeLable(EXCHANGE_ALL);
                    if (!JsonUtil.isEmpty(mAllBedList)) {
                        mAdapter.setDatas(mAllBedList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.tv_use:
                    changeLable(EXCHANGE_USE);
                    if (!JsonUtil.isEmpty(mAllBedList)) {
                        mAdapter.setDatas(mUseBedList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.tv_empty:
                    changeLable(EXCHANGE_EMPTY);
                    if (!JsonUtil.isEmpty(mAllBedList)) {
                        mAdapter.setDatas(mEmptyBedList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.btn_save:
                    if (!TextUtils.isEmpty(mBedFrom.getText().toString()) && !TextUtils.isEmpty(mBedTo.getText().toString())) {
                        insertExchangeBed(LoginInfo.OFFICE_ID, mBedFrom.getText().toString(), mBedTo.getText().toString());
                    } else {
                        L.i("请填写完整床位信息");
                    }
                    break;
                case R.id.tv_from:
                    changeCursor2From();
                    break;
                case R.id.tv_to:
                    changeCursor2To();
                    break;
                default:
                    break;
            }

        }
    };

    private void changeCursor2To() {
        mCursor = 1;
        mBedTo.setBackgroundResource(R.drawable.round_exchangebed_edit);
        mBedFrom.setBackgroundResource(R.drawable.round_exchangebed_normal);
    }

    private void changeCursor2From() {
        mCursor = 0;
        mBedFrom.setBackgroundResource(R.drawable.round_exchangebed_edit);
        mBedTo.setBackgroundResource(R.drawable.round_exchangebed_normal);
    }


    /**
     * 切换标签
     *
     * @param showIndex
     * @return
     */
    private void changeLable(int showIndex) {
        if (EXCHANGE_TAG != showIndex) {
            // 重置按钮状态
            resetLeftLayout(showIndex);
            EXCHANGE_TAG = showIndex;
        }
    }


    /**
     * 重置按钮
     */
    private void resetLeftLayout(int flag) {
        // 首先重置状态
        for (TextView textView : mTexts) {
            textView.setTextColor(getResources().getColor(R.color.exchangebed_bgcolor));
        }
        mTexts[EXCHANGE_ALL].setBackgroundResource(R.drawable.exchange_left_white);
        mTexts[EXCHANGE_USE].setBackgroundColor(getResources().getColor(R.color.exchange_slideline_bg));
        mTexts[EXCHANGE_EMPTY].setBackgroundColor(getResources().getColor(R.color.exchange_slideline_bg));

        // 然后设定新的状态
        mTexts[flag].setTextColor(getResources().getColor(R.color.white));
        if (flag == EXCHANGE_ALL) {
            mTexts[flag].setBackgroundResource(R.drawable.exchange_left_green);
        } else if (flag == EXCHANGE_USE) {
            mTexts[flag].setBackgroundColor(getResources().getColor(R.color.exchangebed_bgcolor));
        } else if (flag == EXCHANGE_EMPTY) {
            mTexts[flag].setBackgroundResource(R.drawable.exchange_right_green);
        }
    }


    /**
     * 查询所有床位（含状态）
     *
     * @param officeId
     */
    private void loadExchangeBed(final String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_BED_FILE_ALL_BY_OFFICE_ID
                + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mAllBedList = JsonUtil.getExchangeBedFromJson(json);
                L.i("所有换床床位数量：" + mAllBedList.size());
                if (!JsonUtil.isEmpty(mAllBedList)) {
                    classifyData();
                    setData();
                }
            }

        };
        getUtil.doGet(this, url, "换床床位信息");
    }

    /**
     * 新增换床信息
     *
     * @param officeId
     */
    private void insertExchangeBed(final String officeId, String oldBed, String newBed) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.INSERT_TRAN_BED_RECORD
                + "?officeid=" + officeId + "&oldBed=" + oldBed + "&newBed=" + newBed;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                L.i("新增换床信息返回值：" + json);
                mChange = true;
                changeCursor2From();
                loadExchangeData(LoginInfo.OFFICE_ID, false);
            }

        };
        getUtil.doGet(this, url, "新增换床信息");
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
                loadExchangeData(LoginInfo.OFFICE_ID, false);
            }

        };
        getUtil.doGet(this, url, "删除换床信息");
    }

    /**
     * 将床位信息分类
     */
    private void classifyData() {
        mEmptyBedList = new ArrayList<>();
        mUseBedList = new ArrayList<>();
        for (ExchangeBed b : mAllBedList) {
            // 1表示有人占床
            if (b.getStatus() == 1) {
                mUseBedList.add(b);
            } else {
                mEmptyBedList.add(b);
            }
        }
    }

    /**
     * 设置数据
     */
    private void setData() {
        mAdapter = new ExchangeBedAdapter(this, mAllBedList, R.layout.item_exchange_bed);
        mGridView.setAdapter(mAdapter);
    }

    /**
     * 获取换床信息
     *
     * @param officeId
     */
    private void loadExchangeData(String officeId, boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_TRANS_BED_RECORD_BY_TODAY
                + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                LoginInfo.mExchangeList = JsonUtil.getExchangeFromJson(json);
                L.i("共读取到换床信息数量：" + LoginInfo.mExchangeList.size());
                if (!JsonUtil.isEmpty(LoginInfo.mExchangeList)) {
                    setExchangeData();
                }
            }

        };
        getUtil.doGet(this, url, isUpdate, "换床信息");
    }

    /**
     * 填充换床信息
     */
    private void setExchangeData() {
        mExchangeLayout.removeAllViews();
        for (Exchange ex : LoginInfo.mExchangeList) {

            ExchangeBedLine line = new ExchangeBedLine(ExchangeBedActivity.this);
            line.setFrom(ex.getOldBed());
            line.setTo(ex.getNewBed());
            line.setTransId(ex.getId());
            mExchangeLayout.addView(line);
        }
    }


}
