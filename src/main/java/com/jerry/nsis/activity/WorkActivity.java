package com.jerry.nsis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Nurse;
import com.jerry.nsis.entity.Schedule;
import com.jerry.nsis.entity.Work;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.DensityUtils;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.view.MyWorkLine;
import com.jerry.nsis.view.NurseImageName;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_work)
public class WorkActivity extends Activity {

    @ViewInject(R.id.tv_office)
    private TextView mTextOffice;

    @ViewInject(R.id.ll_leftlayout)
    private LinearLayout mLeftLayout;
    @ViewInject(R.id.ll_rightlayout)
    private LinearLayout mRightLayout;
    @ViewInject(R.id.ll_thirdlayout)
    private LinearLayout mThirdLayout;
    @ViewInject(R.id.ll_night)
    private LinearLayout mNightLayout;
    @ViewInject(R.id.ll_vacation)
    private LinearLayout mVacationLayout;

    @ViewInject(R.id.tv_date)
    private TextView mTextDate;
    @ViewInject(R.id.tv_day)
    private TextView mTextDay;

    @ViewInject(R.id.ll_left)
    private LinearLayout mLeft;
    @ViewInject(R.id.ll_right)
    private LinearLayout mRight;

    private List<Schedule> mScheduleList;
    private List<Nurse> mNurseList;
    private List<Work> mWorkList;

    private List<MyWorkLine> mDayList;

    public static Date mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        initView();
        loadNurseData(LoginInfo.OFFICE_ID);
    }

    private void initView() {
        mDate = new Date();
        mTextOffice.setText(LoginInfo.OFFICE_NAME);
        mTextDate.setText(DateUtil.getYMD());
        mTextDay.setText(DateUtil.getDay());

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                mDate = new Date(calendar.getTimeInMillis());
                mTextDate.setText(DateUtil.getYMD(mDate));
                mTextDay.setText(DateUtil.getDay(mDate));
                loadOnedayData(LoginInfo.OFFICE_ID, DateUtil.getYMD(mDate));
            }
        });

        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                mDate = new Date(calendar.getTimeInMillis());
                mTextDate.setText(DateUtil.getYMD(mDate));
                mTextDay.setText(DateUtil.getDay(mDate));
                loadOnedayData(LoginInfo.OFFICE_ID, DateUtil.getYMD(mDate));
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
                LoginInfo.mScheduleList = mScheduleList;
                L.i("共读取到班次信息息数量：" + mScheduleList.size());
                if (!JsonUtil.isEmpty(mScheduleList)) {
                    loadOnedayData(LoginInfo.OFFICE_ID, DateUtil.getYMD());
                }
            }
        };
        getUtil.doGet(this, url, "班次信息");
    }


    /**
     * 读取所有护士信息
     */
    private void loadNurseData(String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.VIDEO_SERVICE + ServiceConstant.FIND_SCHEDULING_NURSE_BY_OFFICE_ID
                + "?officeID=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mNurseList = JsonUtil.getNurseFromJson(json);
                LoginInfo.mNurseList = mNurseList;
                L.i("共读取到护士信息息数量：" + mNurseList.size());
                loadScheduleData(LoginInfo.OFFICE_ID);
            }
        };
        getUtil.doGet(this, url, "护士信息");
    }

    /**
     * 读取当天排班信息
     */
    private void loadOnedayData(String officeId, String date) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.FIND_SCHEDILING_INFO_RECORD_BY_DATE_OFFICE_ID
                + "?officeID=" + officeId + "&appDate=" + date;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mWorkList = JsonUtil.getWorkFromJson(json);
                L.i("共读取到当天排班数量：" + mWorkList.size());
                if (!JsonUtil.isEmpty(mWorkList)) {
                    setWorkData();
                }
            }
        };
        getUtil.doGet(this, url, "当天排班");
    }


    /**
     * 将排班信息设置到界面上
     */
    private void setWorkData() {
        mLeftLayout.removeAllViews();
        mRightLayout.removeAllViews();
        mThirdLayout.removeAllViews();
        mNightLayout.removeAllViews();
        mVacationLayout.removeAllViews();

        mDayList = new ArrayList<>();
        for (Schedule s : mScheduleList) {
            if (s.getTypeName().equals(Schedule.DAY_WORK)) {
                MyWorkLine line = new MyWorkLine(this);
                line.setTitle(s.getScheduleName());
                line.setSchedule(s);
                mDayList.add(line);
            } else if (s.getTypeName().equals(Schedule.NIGHT_WORK)) {
                for (Work w : mWorkList) {
                    if (s.getScheduleId().equals(w.getScheduleId())) {
                        NurseImageName n = new NurseImageName(this);
                        Nurse nurse = getNurseById(w.getUserId());
                        n.setNurse(nurse);
                        n.setType(s.getScheduleName());
                        mNightLayout.addView(n);
                    }
                }
            } else if (s.getTypeName().equals(Schedule.VOCATION)) {
                for (Work w : mWorkList) {
                    if (s.getScheduleId().equals(w.getScheduleId())) {
                        NurseImageName n = new NurseImageName(this);
                        Nurse nurse = getNurseById(w.getUserId());
                        n.setNurse(nurse);
                        mVacationLayout.addView(n);
                    }
                }
            }
        }
        int count = 0;
        for (int i = 0; i < mDayList.size(); i++) {
            MyWorkLine line = mDayList.get(i);
            getDayWorkNurse(line);
            if (count <= 3) {
                mLeftLayout.addView(line);
            } else if (count <= 7) {
                mRightLayout.addView(line);
            } else {
                mThirdLayout.addView(line);
            }
            if (line.isHasNurse()) {
                count++;
            } else {
                line.setVisibility(View.GONE);
            }
        }
        // 设置每一行的宽度
        setWorkLineWidth(count);
    }

    /**
     * 设置每一行的宽度
     *
     * @param count 行数
     */
    private void setWorkLineWidth(int count) {
        int width = 0;
        L.i("白板共有：" + count + "组");
        if (count <= 3) {
            width = 800;
        } else if (count <= 7) {
            width = 800;
        } else {
            width = 550;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtils.dp2px(this, width), ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(DensityUtils.dp2px(this, 30), 0, 0, 0);
        mLeftLayout.setLayoutParams(lp);
        mRightLayout.setLayoutParams(lp);
        mThirdLayout.setLayoutParams(lp);
    }

    private Nurse getNurseById(String id) {
        for (Nurse n : mNurseList) {
            if (id.equals(n.getId())) {
                return n;
            }
        }
        return null;
    }

    private void getDayWorkNurse(MyWorkLine line) {
        for (Work w : mWorkList) {
            if (line.getSchedule().getScheduleId().equals(w.getScheduleId())) {
                NurseImageName n = new NurseImageName(this);
                Nurse nurse = getNurseById(w.getUserId());
                n.setNurse(nurse);
                line.addNurse(n);
            }
        }
    }


    @OnClick(R.id.tv_duty)
    public void onDuty(View v) {
        Intent intent = new Intent();
        intent.setClass(this, DutyGroupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_close)
    public void onClose(View v) {
        finish();
    }

    @OnClick(R.id.tv_weekwork)
    public void onWeekWork(View v) {
        Intent intent = new Intent();
        intent.setClass(this, WeeklyWorkActivity.class);
        startActivity(intent);
    }

}
