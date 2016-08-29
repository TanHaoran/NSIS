package com.jerry.nsis.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Nurse;
import com.jerry.nsis.entity.WeekWork;
import com.jerry.nsis.entity.WorkTitle;
import com.jerry.nsis.utils.ActivityUtil;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.view.WeeklyGroup;
import com.jerry.nsis.view.WeeklyLine;
import com.jerry.nsis.view.WeeklyNote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeeklyWorkActivity extends Activity {

    private ImageView mClose;
    private LinearLayout mForm;

    private TextView mOffice;
    private TextView mPre;
    private TextView mThis;
    private TextView mNext;

    private TextView mNumberText;
    private TextView mDateText;

    private TextView mTextCol1;
    private TextView mTextCol2;
    private TextView mTextCol3;
    private TextView mTextCol4;
    private TextView[] mTextCols;

    private List<WeekWork> mWorkList;

    private List<String> mGroupNames;
    private List<String> mSmallGroupNames;
    private List<WorkTitle> mTitleList;

    private Date mDate;

    private TAG mTag = TAG.THIS;


    private enum TAG {
        PREVIOUS, THIS, NEXT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_weekly_work);
        ActivityUtil.makeActivity2Dialog(this);
        initView();
        loadWeekTitle(LoginInfo.OFFICE_ID);
    }

    private void initView() {
        mForm = (LinearLayout) findViewById(R.id.ll_main);
        mOffice = (TextView)findViewById(R.id.tv_office);
        mPre = (TextView) findViewById(R.id.tv_pre);
        mThis = (TextView) findViewById(R.id.tv_this);
        mNext = (TextView) findViewById(R.id.tv_next);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mNumberText = (TextView) findViewById(R.id.tv_number);
        mDateText = (TextView) findViewById(R.id.tv_date);

        mTextCol1 = (TextView) findViewById(R.id.tv_col1);
        mTextCol2 = (TextView) findViewById(R.id.tv_col2);
        mTextCol3 = (TextView) findViewById(R.id.tv_col3);
        mTextCol4 = (TextView) findViewById(R.id.tv_col4);
        mTextCols = new TextView[]{mTextCol1, mTextCol2, mTextCol3, mTextCol4};

        mOffice.setText(LoginInfo.OFFICE_NAME);

        mPre.setOnClickListener(mOnClickListener);
        mThis.setOnClickListener(mOnClickListener);
        mNext.setOnClickListener(mOnClickListener);
        mClose.setOnClickListener(mOnClickListener);

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pre:
                    changeTag(TAG.PREVIOUS);
                    break;
                case R.id.tv_this:
                    changeTag(TAG.THIS);
                    break;
                case R.id.tv_next:
                    changeTag(TAG.NEXT);
                    break;
                case R.id.iv_close:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void changeTag(TAG tag) {
        if (mTag != tag) {
            resetBtn(tag);
        }
    }

    private void resetBtn(TAG tag) {
        mPre.setTextColor(Color.parseColor("#606060"));
        mThis.setTextColor(Color.parseColor("#606060"));
        mNext.setTextColor(Color.parseColor("#606060"));
        mPre.setBackgroundColor(Color.parseColor("#ffffff"));
        mThis.setBackgroundColor(Color.parseColor("#ffffff"));
        mNext.setBackgroundColor(Color.parseColor("#ffffff"));
        if (tag == TAG.PREVIOUS) {
            mPre.setTextColor(Color.parseColor("#ffffff"));
            mPre.setBackgroundColor(Color.parseColor("#1abb9b"));
        } else if (tag == TAG.THIS) {
            mThis.setTextColor(Color.parseColor("#ffffff"));
            mThis.setBackgroundColor(Color.parseColor("#1abb9b"));
        } else {
            mNext.setTextColor(Color.parseColor("#ffffff"));
            mNext.setBackgroundColor(Color.parseColor("#1abb9b"));
        }
        mTag = tag;

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (mTag == TAG.PREVIOUS) {
            c.add(Calendar.DAY_OF_YEAR, -7);
        } else if (mTag == TAG.PREVIOUS) {
        } else if (mTag == TAG.NEXT) {
            c.add(Calendar.DAY_OF_YEAR, 7);
        }
        mDate = new Date(c.getTimeInMillis());
        loadWeekWork(LoginInfo.OFFICE_ID, DateUtil.getYMD(mDate));
    }

    /**
     * 读取一周排班
     *
     * @param officeId
     * @param date
     */
    private void loadWeekWork(final String officeId, final String date) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.GET_ORDER_CLASS_TABLE
                + "?OfficeID=" + officeId + "&currentDate=" + date;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mWorkList = JsonUtil.getWeekWorkFromJson(json);
                L.i("共读取到一周排班：" + mWorkList.size());
                mForm.removeAllViews();
                setDateDate(date);
                if (!JsonUtil.isEmpty(mWorkList)) {
                    setData();
                    loadWeekNote(officeId, date);
                } else {
                    mNumberText.setText("0");
                }
            }
        };
        getUtil.doGet(this, url, "一周排班");
    }


    /**
     * 读取标题
     *
     * @param officeId
     */
    private void loadWeekTitle(String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.GET_ORDER_CLASS_COLUMN
                + "?OfficeID=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mTitleList = JsonUtil.getWeekTitleFromJson(json);
                L.i("共读取到一周排班标题：" + mTitleList.size());
                if (!JsonUtil.isEmpty(mTitleList)) {
                    setTitleData();
                }
                loadWeekWork(LoginInfo.OFFICE_ID, DateUtil.getYMD());
            }
        };
        getUtil.doGet(this, url, "一周排班标题");
    }

    /**
     * 读取一周排班备注信息
     *
     * @param officeId
     */
    private void loadWeekNote(String officeId, String date) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SCHEDULE_SERVICE + ServiceConstant.FIND_SCHEDULING_INFO_BY_REMARK
                + "?OfficeID=" + officeId + "&appDate=" + date;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                String note = JsonUtil.getWeekNoteFromJson(json);
                L.i("备注信息" + note);
                addNoteView(note);
            }
        };
        getUtil.doGet(this, url, "一周排班备注");
    }

    private void addNoteView(String note) {
        WeeklyNote noteView = new WeeklyNote(this);
        noteView.setNote(note);
        mForm.addView(noteView);
    }

    private void setTitleData() {
        for (int i = 0; i < mTitleList.size(); i++) {
            if (i >= 2 && i <= 5) {
                mTextCols[i - 2].setText(mTitleList.get(i).getTitle());
            }
        }
    }

    private int rows = 0;
    private List<WeeklyGroup> mGroupList = new ArrayList<>();

    private void setData() {
        mNumberText.setText(String.valueOf(mWorkList.size()));
        mGroupNames = new ArrayList<>();
        mSmallGroupNames = new ArrayList<>();
        WeeklyGroup group = null;
        for (int i = 0; i < mWorkList.size(); i++) {
            WeekWork w = mWorkList.get(i);
            L.i("-----------------遍历第" + i + "个--------------------" + w.getPro4());
            if (!mGroupNames.contains(w.getBigGroup())) {
                if (group != null) {
                    mForm.addView(group);
                }
                mGroupNames.add(w.getBigGroup());
                add2Small(group, w, false);
                group = new WeeklyGroup(this);
                mGroupList.add(group);
                group.setGroup(w.getBigGroup());
                setLineData(group, w);
            } else {
                add2Small(group, w, true);
                setLineData(group, w);
            }
        }
        if (rows != 0) {
            group.addSmallGroup(mSmallGroupNames.get(mSmallGroupNames.size() - 1), rows);
            rows = 0;
        }
        if (group != null) {
            mForm.addView(group);
        }

    }

    private void add2Small(WeeklyGroup group, WeekWork w, boolean exist) {
        if (!TextUtils.isEmpty(w.getSmallGroup())) {
            L.i("是否存在小组名称？" + w.getSmallGroup());
            if (!mSmallGroupNames.contains(w.getSmallGroup())) {
                L.i("不存在！");
                if (rows != 0) {
                    group.addSmallGroup(mSmallGroupNames.get(mSmallGroupNames.size() - 1), rows);
                    rows = 0;
                }
                mSmallGroupNames.add(w.getSmallGroup());
                L.i("添加小组名称：" + w.getSmallGroup());
                rows++;
                L.i("添加后行数：" + rows);
            } else {
                L.i("存在！");
                rows++;
                L.i("添加后行数：" + rows);
            }
        } else {
            if (rows != 0) {
                group.addSmallGroup(mSmallGroupNames.get(mSmallGroupNames.size() - 1), rows);
                rows = 0;
            }
        }
    }

    private void setLineData(WeeklyGroup group, WeekWork w) {
        WeeklyLine line = new WeeklyLine(this);
        line.setColContent(0, w.getPro1());
        line.setColContent(1, w.getPro2());
        line.setColContent(2, w.getPro3());
        line.setColContent(3, w.getPro4());
        line.setWeekWork(w);
        group.addLine(line);
    }

    /**
     * 通过传入的日期，设置当前周的起始和结束
     *
     * @param date
     */
    private void setDateDate(String date) {
        StringBuilder sb = new StringBuilder();

        String start = DateUtil.getMondayDate(DateUtil.getDateByString(date));
        String end = DateUtil.getSundayDate(DateUtil.getDateByString(date));

        sb.append(start);
        sb.append("~");
        sb.append(end);

        mDateText.setText(sb.toString());
    }

    private Nurse getNurseById(String id) {
        for (Nurse n : LoginInfo.mNurseList) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return null;
    }


}
