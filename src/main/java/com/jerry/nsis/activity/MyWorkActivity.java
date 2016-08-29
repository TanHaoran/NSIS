package com.jerry.nsis.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.BedScanAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.Cost;
import com.jerry.nsis.entity.Nurse;
import com.jerry.nsis.entity.NurseWork;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.entity.Schedule;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.StreamUtil;
import com.jerry.nsis.view.CircleImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyWorkActivity extends Activity {

    private CircleImageView mHead;

    private GridView mGridView;
    private BedScanAdapter mAdapter;
    private TextView mTextName;
    private TextView mTextLevel;
    private TextView mTextOffice;
    private TextView mTextNumber;

    private TextView textPrevious1;
    private TextView textPrevious2;
    private TextView textPrevious3;
    private TextView textPrevious4;
    private TextView textPrevious5;
    private TextView textPrevious6;
    private TextView textPrevious7;
    private TextView[] previousTextViews;

    private ImageView imagePrevious1;
    private ImageView imagePrevious2;
    private ImageView imagePrevious3;
    private ImageView imagePrevious4;
    private ImageView imagePrevious5;
    private ImageView imagePrevious6;
    private ImageView imagePrevious7;
    private ImageView[] previousImageViews;

    private TextView textThis1;
    private TextView textThis2;
    private TextView textThis3;
    private TextView textThis4;
    private TextView textThis5;
    private TextView textThis6;
    private TextView textThis7;
    private TextView[] thisTextViews;

    private ImageView imageThis1;
    private ImageView imageThis2;
    private ImageView imageThis3;
    private ImageView imageThis4;
    private ImageView imageThis5;
    private ImageView imageThis6;
    private ImageView imageThis7;
    private ImageView[] thisImageViews;

    private TextView textNext1;
    private TextView textNext2;
    private TextView textNext3;
    private TextView textNext4;
    private TextView textNext5;
    private TextView textNext6;
    private TextView textNext7;
    private TextView[] nextTextViews;

    private ImageView imageNext1;
    private ImageView imageNext2;
    private ImageView imageNext3;
    private ImageView imageNext4;
    private ImageView imageNext5;
    private ImageView imageNext6;
    private ImageView imageNext7;
    private ImageView[] nextImageViews;


    private Nurse mNurse;
    private ImageView mClose;
    private List<NurseWork> previousList;
    private List<NurseWork> workList;
    private List<NurseWork> nextList;

    private List<Schedule> mScheduleList;

    private List<Bed> mBedList;


    public static final int PREVIOUS_WEEK = -1;
    public static final int THIS_WEEK = 0;
    public static final int NEXT_WEEK = 1;
    private List<Cost> mCostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_my_work);
        ActivityUtil.makeActivity2Dialog(this);
        mNurse = (Nurse) getIntent().getSerializableExtra("nurse");
        initView();
        if (mNurse != null) {
            loadScheduleData(LoginInfo.OFFICE_ID);
            loadNurseBedData(LoginInfo.OFFICE_ID, mNurse.getId(), DateUtil.getYMD(WorkActivity.mDate));

        }
    }

    private void initView() {
        mHead = (CircleImageView) findViewById(R.id.iv_head);
        mGridView = (GridView) findViewById(R.id.gridview);
        mTextName = (TextView) findViewById(R.id.tv_col4);
        mTextLevel = (TextView) findViewById(R.id.tv_level);
        mTextOffice = (TextView) findViewById(R.id.tv_office);
        mTextNumber = (TextView) findViewById(R.id.tv_number);


        textPrevious1 = (TextView) findViewById(R.id.tv_pre_1);
        textPrevious2 = (TextView) findViewById(R.id.tv_pre_2);
        textPrevious3 = (TextView) findViewById(R.id.tv_pre_3);
        textPrevious4 = (TextView) findViewById(R.id.tv_pre_4);
        textPrevious5 = (TextView) findViewById(R.id.tv_pre_5);
        textPrevious6 = (TextView) findViewById(R.id.tv_pre_6);
        textPrevious7 = (TextView) findViewById(R.id.tv_pre_7);
        previousTextViews = new TextView[]{textPrevious1, textPrevious2, textPrevious3, textPrevious4, textPrevious5, textPrevious6, textPrevious7};

        imagePrevious1 = (ImageView) findViewById(R.id.iv_pre_1);
        imagePrevious2 = (ImageView) findViewById(R.id.iv_pre_2);
        imagePrevious3 = (ImageView) findViewById(R.id.iv_pre_3);
        imagePrevious4 = (ImageView) findViewById(R.id.iv_pre_4);
        imagePrevious5 = (ImageView) findViewById(R.id.iv_pre_5);
        imagePrevious6 = (ImageView) findViewById(R.id.iv_pre_6);
        imagePrevious7 = (ImageView) findViewById(R.id.iv_pre_7);
        previousImageViews = new ImageView[]{imagePrevious1, imagePrevious2, imagePrevious3, imagePrevious4, imagePrevious5, imagePrevious6, imagePrevious7};

        textThis1 = (TextView) findViewById(R.id.tv_this_1);
        textThis2 = (TextView) findViewById(R.id.tv_this_2);
        textThis3 = (TextView) findViewById(R.id.tv_this_3);
        textThis4 = (TextView) findViewById(R.id.tv_this_4);
        textThis5 = (TextView) findViewById(R.id.tv_this_5);
        textThis6 = (TextView) findViewById(R.id.tv_this_6);
        textThis7 = (TextView) findViewById(R.id.tv_this_7);
        thisTextViews = new TextView[]{textThis1, textThis2, textThis3, textThis4, textThis5, textThis6, textThis7};

        imageThis1 = (ImageView) findViewById(R.id.iv_this_1);
        imageThis2 = (ImageView) findViewById(R.id.iv_this_2);
        imageThis3 = (ImageView) findViewById(R.id.iv_this_3);
        imageThis4 = (ImageView) findViewById(R.id.iv_this_4);
        imageThis5 = (ImageView) findViewById(R.id.iv_this_5);
        imageThis6 = (ImageView) findViewById(R.id.iv_this_6);
        imageThis7 = (ImageView) findViewById(R.id.iv_this_7);
        thisImageViews = new ImageView[]{imageThis1, imageThis2, imageThis3, imageThis4, imageThis5, imageThis6, imageThis7};

        textNext1 = (TextView) findViewById(R.id.tv_next_1);
        textNext2 = (TextView) findViewById(R.id.tv_next_2);
        textNext3 = (TextView) findViewById(R.id.tv_next_3);
        textNext4 = (TextView) findViewById(R.id.tv_next_4);
        textNext5 = (TextView) findViewById(R.id.tv_next_5);
        textNext6 = (TextView) findViewById(R.id.tv_next_6);
        textNext7 = (TextView) findViewById(R.id.tv_next_7);
        nextTextViews = new TextView[]{textNext1, textNext2, textNext3, textNext4, textNext5, textNext6, textNext7};

        imageNext1 = (ImageView) findViewById(R.id.iv_next_1);
        imageNext2 = (ImageView) findViewById(R.id.iv_next_2);
        imageNext3 = (ImageView) findViewById(R.id.iv_next_3);
        imageNext4 = (ImageView) findViewById(R.id.iv_next_4);
        imageNext5 = (ImageView) findViewById(R.id.iv_next_5);
        imageNext6 = (ImageView) findViewById(R.id.iv_next_6);
        imageNext7 = (ImageView) findViewById(R.id.iv_next_7);
        nextImageViews = new ImageView[]{imageNext1, imageNext2, imageNext3, imageNext4, imageNext5, imageNext6, imageNext7};

        mTextOffice.setText(LoginInfo.OFFICE_NAME);

        mClose = (ImageView) findViewById(R.id.iv_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 读取班次信息
     */
    private void loadScheduleData(String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.FIND_ALL_SCHE_TYPE_BY_OFFICE_ID
                + "?officeID=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mScheduleList = JsonUtil.getScheduleFromJson(json);
                L.i("共读取到班次信息息数量：" + mScheduleList.size());
                if (!JsonUtil.isEmpty(mScheduleList)) {
                    if (mNurse != null && mNurse.getId() != null) {
                        if (!TextUtils.isEmpty(mNurse.getThumbnail())) {
                            mHead.setImageBitmap(StreamUtil.base64ToBitmap(mNurse.getThumbnail()));
                        }
                        mTextName.setText(mNurse.getName());
                        mTextLevel.setText(mNurse.getPosition());
                        mTextOffice.setText("神经内科");
                        loadNurseWeeklyData(LoginInfo.OFFICE_ID, mNurse.getId(), PREVIOUS_WEEK);
                        loadNurseWeeklyData(LoginInfo.OFFICE_ID, mNurse.getId(), THIS_WEEK);
                        loadNurseWeeklyData(LoginInfo.OFFICE_ID, mNurse.getId(), NEXT_WEEK);
                    }
                }
            }
        };
        getUtil.doGet(this, url, "班次信息");
    }


    /**
     * 读取护士管床信息
     */
    private void loadNurseBedData(String officeId, String userId, String date) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.GET_SCHE_BED_FILE_ALL
                + "?OfficeID=" + officeId + "&UserID=" + userId + "&appDate=" + date;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mBedList = JsonUtil.getChargeBedlFromJson(json);
                L.i("共读取到护士管床数量：" + mBedList.size());
                if (!JsonUtil.isEmpty(mBedList)) {
                    loadAllPatientCostData(LoginInfo.OFFICE_ID);
                }
            }
        };
        getUtil.doGet(this, url, "护士管床");
    }

    /**
     * 读取所有病人花费
     */
    private void loadAllPatientCostData(final String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_PATIENT_INFO_SPEND_BY_OFFICE_ID + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mCostList = JsonUtil.getCostListFromJson(json);
                L.i("共读病人花费数量：" + mCostList.size());
                if (!JsonUtil.isEmpty(mBedList)) {
                    setCostData();
                    setBedData();
                }
            }

        };
        getUtil.doGet(this, url, false, "读取所有病人花费");
    }

    private void setCostData() {
        for (Bed b : mBedList) {
            for (Cost c : mCostList) {
                if (b.getPatient() != null) {
                    Patient p = b.getPatient();
                    if (p != null) {
                        if (p.getHosId().equals(c.getHosId()) && p.getInTimes() == c.getInTimes()) {
                            p.setInsurance(c.getType());
                        }
                    }
                }
            }
        }
    }


    private void setBedData() {
        mTextNumber.setText(String.valueOf(mBedList.size()));
        mAdapter = new BedScanAdapter(this, mBedList, R.layout.item_bedscan);
        mGridView.setAdapter(mAdapter);
    }


    /**
     * 读取护士一周排班信息
     */
    private void loadNurseWeeklyData(String officeId, String nurseId, final int week) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (week == PREVIOUS_WEEK) {
            c.add(Calendar.DAY_OF_YEAR, -7);
        } else if (week == THIS_WEEK) {
        } else if (week == NEXT_WEEK) {
            c.add(Calendar.DAY_OF_YEAR, 7);
        }
        date = new Date(c.getTimeInMillis());
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.FIND_SCHDULING_WEEK
                + "?officeID=" + officeId + "&UserID=" + nurseId + "&appDate=" + DateUtil.getYMD(date);
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                if (week == PREVIOUS_WEEK) {
                    previousList = JsonUtil.getNurseWorkFromJson(json);
                    L.i("共读取到护士上周排班信息：" + previousList.size());
                    if (!JsonUtil.isEmpty(previousList)) {
                        setPreviousWorkData();
                    }
                } else if (week == THIS_WEEK) {
                    workList = JsonUtil.getNurseWorkFromJson(json);
                    L.i("共读取到护士本周排班信息：" + workList.size());
                    if (!JsonUtil.isEmpty(workList)) {
                        setThisWorkData();
                    }
                } else if (week == NEXT_WEEK) {
                    nextList = JsonUtil.getNurseWorkFromJson(json);
                    L.i("共读取到护士下周排班信息：" + nextList.size());
                    if (!JsonUtil.isEmpty(nextList)) {
                        setNextWorkData();
                    }
                }
            }
        };
        getUtil.doGet(this, url, "护士一周排班");
    }


    private boolean checkIfRest(NurseWork w) {
        for (Schedule s : mScheduleList) {
            if (s.getScheduleName().equals(w.getContent())) {
                if (s.getTypeName().equals(Schedule.VOCATION)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置下周的排班数据
     */
    private void setNextWorkData() {
        for (int i = 0; i < nextTextViews.length; i++) {
            NurseWork w = nextList.get(i);
            if (!checkIfRest(w)) {
                nextTextViews[i].setVisibility(View.VISIBLE);
                nextTextViews[i].setText(w.getContent());
                nextImageViews[i].setVisibility(View.INVISIBLE);
            } else {
                nextTextViews[i].setVisibility(View.INVISIBLE);
                nextImageViews[i].setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置上周的排班数据
     */
    private void setPreviousWorkData() {
        for (int i = 0; i < previousTextViews.length; i++) {
            NurseWork w = previousList.get(i);
            if (!checkIfRest(w)) {
                previousTextViews[i].setText(w.getContent());
                previousTextViews[i].setVisibility(View.VISIBLE);
                previousImageViews[i].setVisibility(View.INVISIBLE);
            } else {
                previousTextViews[i].setVisibility(View.INVISIBLE);
                previousImageViews[i].setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置本周的排班数据
     */
    private void setThisWorkData() {
        for (int i = 0; i < thisTextViews.length; i++) {
            NurseWork w = workList.get(i);
            if (!checkIfRest(w)) {
                thisTextViews[i].setText(w.getContent());
                thisTextViews[i].setVisibility(View.VISIBLE);
                thisImageViews[i].setVisibility(View.INVISIBLE);
            } else {
                thisTextViews[i].setVisibility(View.INVISIBLE);
                thisImageViews[i].setVisibility(View.VISIBLE);
            }
        }
    }

}
