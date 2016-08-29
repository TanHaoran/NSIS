package com.jerry.nsis.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.CommonAdapter;
import com.jerry.nsis.adapter.OrderAdapter;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.Cost;
import com.jerry.nsis.entity.Order;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class PatientActivity extends Activity {

    private Bed mBed;
    private Patient mPatient;
    private Cost mCost;

    private ImageView mClose;

    private TextView mTextName;
    private TextView mTextAge;
    private TextView mTextBed;
    private TextView mTextHosId;
    private TextView mTextInTime;
    private TextView mTextDiag;
    private TextView mTextLevel;
    private TextView mTextDoc;
    private TextView mTextType;
    private TextView mTextPre;
    private TextView mTextAll;
    private TextView mTextRemain;
    private TextView mTextPhone;

    private TextView mType;

    private TextView mLongBtn;
    private TextView mTempBtn;

    private ListView mListView;

    private OrderAdapter mAdapter;

    private List<Order> mLongList;
    private List<Order> mTempList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ActivityUtil.makeActivity2Dialog(this);

        initView();
        mBed = (Bed) getIntent().getSerializableExtra("bed");
        if (mBed != null) {
            mPatient = mBed.getPatient();
            loadPatientCostData(mPatient.getHosId(), mPatient.getInTimes());
            loadOrderData(mPatient.getHosId(), mPatient.getInTimes(), Order.LONG_ORDER);
            loadOrderData(mPatient.getHosId(), mPatient.getInTimes(), Order.TEMP_ORDER);
        }
        setData();
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (mBed != null && mPatient != null) {
            mTextName.setText(mPatient.getName());
            mTextAge.setText(DateUtil.getAge(mPatient.getBirthday()) + "岁");
            mTextBed.setText(mBed.getNo());
            mTextHosId.setText(String.valueOf(mPatient.getHosId()));
            if (mPatient.getInDate().split(" ").length == 2) {
                mTextInTime.setText(String.valueOf(mPatient.getInDate().split(" ")[0].substring(5) + "  " + mPatient.getInDate().split(" ")[1].substring(0, 5)));
            } else {
                mTextInTime.setText(String.valueOf(mPatient.getInDate()));
            }
            mTextDiag.setText(mPatient.getDiagnosis());
            mTextLevel.setText(mPatient.getLevel());
            mTextDoc.setText(mPatient.getDoc());
        }
    }

    private void initView() {
        mClose = (ImageView) findViewById(R.id.iv_close);
        mTextName = (TextView) findViewById(R.id.tv_col4);
        mTextAge = (TextView) findViewById(R.id.tv_col2);
        mTextBed = (TextView) findViewById(R.id.tv_bed);
        mTextHosId = (TextView) findViewById(R.id.tv_hosid);
        mTextInTime = (TextView) findViewById(R.id.tv_intime);
        mTextDiag = (TextView) findViewById(R.id.tv_diagnosis);
        mTextLevel = (TextView) findViewById(R.id.tv_level);
        mTextDoc = (TextView) findViewById(R.id.tv_doc);
        mTextType = (TextView) findViewById(R.id.tv_type);
        mTextPre = (TextView) findViewById(R.id.tv_pre);
        mTextAll = (TextView) findViewById(R.id.tv_all);
        mTextRemain = (TextView) findViewById(R.id.tv_remain);
        mTextPhone = (TextView) findViewById(R.id.tv_phone);

        mType = (TextView) findViewById(R.id.tv_ordertype);

        mLongBtn = (TextView) findViewById(R.id.tv_long);
        mTempBtn = (TextView) findViewById(R.id.tv_temp);

        mListView = (ListView) findViewById(R.id.listview);

        mLongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongBtn.setBackgroundColor(Color.parseColor("#1abb9b"));
                mTempBtn.setBackgroundColor(Color.parseColor("#8b8b8b"));
                mType.setText("长期医嘱");
                setOrderData(mLongList);
            }
        });

        mTempBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLongBtn.setBackgroundColor(Color.parseColor("#8b8b8b"));
                mTempBtn.setBackgroundColor(Color.parseColor("#1abb9b"));
                mType.setText("临时医嘱");
                setOrderData(mTempList);
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeListData(List<Order> orderList) {
        mAdapter.setDatas(orderList);
    }

    /**
     * 获取病人花费信息
     */
    private void loadPatientCostData(String hosId, int inTimes) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.QUERY_PATIENT_MONEY + "?inhosid=" +
                hosId + "&inhosTimes=" + inTimes;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mCost = JsonUtil.getCostFromJson(json);
                setCostData();
            }
        };
        getUtil.doGet(this, url, false, "病人花费信息");
    }


    /**
     * 获取医嘱信息
     */
    private void loadOrderData(final String officeId, int inTimes, final String type) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.QUERY_MEDORD_BYINHOSID + "?inhosid=" +
                    officeId + "&inhosTimes=" + inTimes + "&methodType=" + URLEncoder.encode(type, "UTF-8");
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    if (type.equals(Order.LONG_ORDER)) {
                        mLongList = JsonUtil.getOrderListFromJson(json);
                        L.i("共读取到长期医嘱：" + mLongList.size());
                        setOrderData(mLongList);
                    } else if (type.equals(Order.TEMP_ORDER)) {
                        mTempList = JsonUtil.getOrderListFromJson(json);
                        L.i("共读取到临时医嘱：" + mTempList.size());
                    }
                }
            };
            getUtil.doGet(this, url, false, "医嘱信息");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setOrderData(List<Order> list) {
        mAdapter = new OrderAdapter(this, list, R.layout.item_order);
        mAdapter.setStyle(CommonAdapter.STYLE.UP_DOWN_SIDE);
        mListView.setAdapter(mAdapter);
    }


    /**
     * 设置花费的信息
     */
    private void setCostData() {
        mTextPre.setText(String.valueOf(mCost.getPre()));
        mTextAll.setText(String.valueOf(mCost.getAll()));
        mTextRemain.setText(String.valueOf(mCost.getRemain()));
        mTextType.setText(String.valueOf(mCost.getType()));
        mTextPhone.setText(String.valueOf(mCost.getPhone()));

    }
}
