package com.jerry.nsis.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.CommonValues;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.entity.Doctor;
import com.jerry.nsis.entity.Duty;
import com.jerry.nsis.entity.Exchange;
import com.jerry.nsis.entity.Frequency;
import com.jerry.nsis.entity.Note;
import com.jerry.nsis.entity.Notice;
import com.jerry.nsis.entity.Nursing;
import com.jerry.nsis.entity.NursingDetail;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.entity.Temper;
import com.jerry.nsis.entity.TemperDetail;
import com.jerry.nsis.utils.AnimationUtil;
import com.jerry.nsis.utils.BedComparator;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.DensityUtils;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.NsisUtil;
import com.jerry.nsis.view.BottomBlock;
import com.jerry.nsis.view.ContactPopupWindow;
import com.jerry.nsis.view.MainMenuView;
import com.jerry.nsis.view.MyProgressDialog;
import com.jerry.nsis.view.NoteView;
import com.jerry.nsis.view.NoticeView;
import com.jerry.nsis.view.TopBlock;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 主界面
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {

    // 右上角更新时间的常量
    private static final int UPDATE_TIME = 0;
    // 更新左侧界面信息的常量
    private static final int UPDATE_DATA = 100;
    // 更新右侧菜单信息的常量
    private static final int UPDATE_RIGHT_DATA = 101;
    // 更新通知消息的常量
    private static final int UPDATE_NOTICE_DATA = 102;
    // 更新左侧数据的时间间隔
    private static final long LOAD_UPDATE_TIME = 1 * 60 * 1000;
    // 更新右侧菜单的时间间隔
    private static final long LOAD_UPDATE_RIGHT_TIME = 2 * 60 * 1000;
    // 更新通知消息时间间隔
    private static final long LOAD_UPDATE_NOTICE_TIME = 10 * 60 * 1000;

    // 提醒项每一个的偏移量
    private static final float NOTE_OFFSET = 240.0f;
    // 展开提醒项的最大个数
    private static final int NOTE_MAX = 6;

    // 初始显示的第一个模块
    private TAG mTag = TAG.FIRST;

    // 左上角控制显示区域标志
    public enum TAG {
        FIRST, SECOND, THIRD, FORTH
    }

    private List<NoteView> mNoteViewList;

    // 标志提醒项打开关闭
    private boolean mNoteLayoutOpen = false;


    @ViewInject(R.id.tv_hospital)
    private TextView mTextHospital;

    @ViewInject(R.id.tv_office)
    private TextView mTextOffice;

    @ViewInject(R.id.rl_main)
    private RelativeLayout mMainLayout;

    @ViewInject(R.id.rl_note)
    private RelativeLayout mNoteLayout;

    // 主菜单
    @ViewInject(R.id.main_menu)
    private MainMenuView mMainMenu;

    // 右上角时间控件
    @ViewInject(R.id.tv_time)
    private TextView mTimeText;

    // 左上部四个TextView控件
    @ViewInject(R.id.tv_left1)
    private TextView mLeftText1;
    @ViewInject(R.id.tv_left2)
    private TextView mLeftText2;
    @ViewInject(R.id.tv_left3)
    private TextView mLeftText3;
    @ViewInject(R.id.tv_left4)
    private TextView mLeftText4;
    private TextView[] mLeftTexts;

    // 左上部分主要显示区域
    @ViewInject(R.id.tb_layout1)
    private TopBlock mTopBlock1;
    @ViewInject(R.id.tb_layout2)
    private TopBlock mTopBlock2;
    @ViewInject(R.id.tb_layout3)
    private TopBlock mTopBlock3;
    @ViewInject(R.id.tb_layout4)
    private TopBlock mTopBlock4;
    private TopBlock[] mTopBlocks;

    // 左下大控件
    @ViewInject(R.id.ll_bottom_layout)
    private LinearLayout mBottomLayout;

    // 今日值班
    @ViewInject(R.id.tv_date)
    private TextView mDateText;
    @ViewInject(R.id.tv_firstline)
    private TextView mTextFirstLine;
    @ViewInject(R.id.tv_secondline)
    private TextView mTextSecondLine;
    @ViewInject(R.id.tv_thirdline)
    private TextView mTextThirdLine;
    @ViewInject(R.id.tv_allline)
    private TextView mTextAllLine;
    @ViewInject(R.id.et_firstline)
    private EditText mEditFirstline;
    @ViewInject(R.id.et_secondline)
    private EditText mEditSecondline;
    @ViewInject(R.id.et_thirdline)
    private EditText mEditThirdline;
    @ViewInject(R.id.et_allline)
    private EditText mEditAllline;

    // 换床信息
    @ViewInject(R.id.tv_exchangefrom1)
    private TextView mExchangeFrom1;
    @ViewInject(R.id.tv_exchangefrom2)
    private TextView mExchangeFrom2;
    @ViewInject(R.id.tv_exchangefrom3)
    private TextView mExchangeFrom3;
    private TextView[] mExchangeFroms;

    @ViewInject(R.id.tv_exchangeto1)
    private TextView mExchangeTo1;
    @ViewInject(R.id.tv_exchangeto2)
    private TextView mExchangeTo2;
    @ViewInject(R.id.tv_exchangeto3)
    private TextView mExchangeTo3;
    private TextView[] mExchangeTos;

    @ViewInject(R.id.tv_exchangearrow1)
    private TextView mExchangeArrow1;
    @ViewInject(R.id.tv_exchangearrow2)
    private TextView mExchangeArrow2;
    @ViewInject(R.id.tv_exchangearrow3)
    private TextView mExchangeArrow3;
    private TextView[] mExchangeArrows;

    @ViewInject(R.id.rl_edit)
    private RelativeLayout mNoteEditLayout;

    @ViewInject(R.id.noticeview)
    private NoticeView mNoticeView;

    // 全部的护理项目
    private List<Nursing> mNursingList;
    private List<Nursing> mImportantList;
    private List<Nursing> mUnImportantList;
    // 护理项目详细
    private transient List<NursingDetail> mNurnsingDetailList;

    // 下部分的内容
    private List<BottomBlock> mBottomBlockList;

    // 今日值班集合
    private List<Duty> mDutyList;
    // 换床信息集合
    private List<Exchange> mExchangeList;
    // 提醒项集合
    private List<Note> mNoteList;

    // 体温护理项
    private List<Temper> mTemperList;
    // 体温护理项详细
    private transient List<TemperDetail> mTemperDetailList;

    private List<Patient> mPatientList;
    private List<Doctor> mDocList;


    private MyProgressDialog dialog;

    // 用来记录前一次的更新时间
    public long updateTime = 0;
    // 用来记录是否更新护理项目
    private boolean mUpdateNursing = false;
    // 用来记录是否更新右侧菜单
    private boolean mUpdateMenu = false;
    // 用来记录是否更新通知消息
    private boolean mUpdateNotice = false;

    private boolean mNoteEditState = false;

    private int mShowNoteIndex;

    /**
     * 所有的通知消息
     */
    private List<Notice> mNoticeList = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 更新时间
                case UPDATE_TIME:
                    int x = (int) mMainMenu.getX();
                    int y = (int) mMainMenu.getY();
                    mTimeText.setText(DateUtil.getTime());
                    mDateText.setText(DateUtil.getMD());
                    if (DateUtil.getTime().equals("12:00:00") || DateUtil.getTime().equals("00:00:00")) {
                        Intent intent = new Intent(MainActivity.this, ScreenSaverActivity.class);
                        startActivity(intent);
                    }
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(x, y, -1000, -1000);
                    mMainMenu.setLayoutParams(lp);
                    break;
                // 更新左侧数据
                case UPDATE_DATA:
                    L.i("更新护理项目时间：" + DateUtil.getYMDHMS());
                    updateTime = System.currentTimeMillis();
                    loadAllPatient(LoginInfo.OFFICE_ID, true);
                    mUpdateNursing = false;
                    break;
                // 更新右侧菜单数据
                case UPDATE_RIGHT_DATA:
                    L.i("更新右侧菜单时间：" + DateUtil.getYMDHMS());
                    mUpdateMenu = false;
                    break;
                // 通信通知消息
                case UPDATE_NOTICE_DATA:
                    L.i("更新通知消息时间：" + DateUtil.getYMDHMS());
                    loadNotice(LoginInfo.OFFICE_ID);
                    mUpdateNotice = false;
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        loadSetting();
        // 初始化界面
        initView();
        // 读取病患、医生、主界面、菜单界面等数据
        loadData();
        // 开启左侧数据刷新
        startUpdateThread();
        // 开启定时读取通知消息
        startUpdateNotice();
        // 注册提醒项接收器
        registerReceiver(noteReceiver, noteFilter);
        // 注册换床信息接收器
        registerReceiver(exchangeReceiver, exchangeFilter);
    }


    /**
     * 读取服务IP、医院名称、科室名称等信息
     */
    public void loadSetting() {
        SharedPreferences sp = getSharedPreferences(RegisterActivity.SETTING_NAME, MODE_PRIVATE);
        String ip = sp.getString("ip", "http://192.168.0.100");
        String hospitalName = sp.getString("hospital", "巴斯光年医院");
        String officeName = sp.getString("office_name", "巴斯光年");
        String officeId = sp.getString("office_id", "0000000130");
        LoginInfo.HOSPITAL_NAME = hospitalName;
        LoginInfo.OFFICE_NAME = officeName;
        LoginInfo.OFFICE_ID = "0000000243";
        ServiceConstant.SERVICE_IP = ip;
        L.i("读取到的医院：" + LoginInfo.HOSPITAL_NAME);
        L.i("读取到的科室名称：" + LoginInfo.OFFICE_NAME);
        L.i("读取到的科室Id：" + LoginInfo.OFFICE_ID);
        L.i("读取到的IP：" + ServiceConstant.SERVICE_IP);
    }

    /**
     * 开启后台每2分钟更新一次信息的线程
     */
    private void startUpdateThread() {
        updateTime = System.currentTimeMillis();
        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!mUpdateNursing) {
                        long secondTime = System.currentTimeMillis();
                        if (secondTime - updateTime > LOAD_UPDATE_TIME) {
                            L.i("时间到了刷新！");
                            mUpdateNursing = true;
                            Message msg = mHandler.obtainMessage();
                            msg.what = UPDATE_DATA;
                            msg.sendToTarget();
                        }
                    }
                }
            }
        });
        updateThread.start();
    }


    /**
     * 开启后台每分钟更新一次右侧信息的线程
     */
    private void startUpdateRightThread() {
        Thread updateRightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mUpdateMenu) {
                    try {
                        Thread.sleep(LOAD_UPDATE_RIGHT_TIME);
                        Message msg = mHandler.obtainMessage();
                        msg.what = UPDATE_RIGHT_DATA;
                        msg.sendToTarget();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        updateRightThread.start();
    }


    /**
     * 读取各项数据
     */
    private void loadData() {
        loadAllPatient(LoginInfo.OFFICE_ID, false);
        loadAllDoctor(false);
        loadVideoUrl();
    }

    /**
     * 读取全院医生信息
     *
     * @param isUpdate
     */
    private void loadAllDoctor(boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_DOCTOR_BY_ALL;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mDocList = JsonUtil.getDoctorFromJson(json);
                LoginInfo.mDocList = mDocList;
                L.i("共读取到医生信息数量：" + mDocList.size());
            }
        };
        getUtil.doGet(this, url, isUpdate, "医生信息");
    }


    /**
     * 读取所有病人信息
     *
     * @param officeId
     */
    private void  loadAllPatient(String officeId, final boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_PATIENT_INFO_BY_OFFICE
                + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mPatientList = JsonUtil.getPatientFromJson(json);
                LoginInfo.mPatientList = mPatientList;
                L.i("共读取到患者信息数量：" + mPatientList.size());
                if (isUpdate) {
                    loadNursingDetail(LoginInfo.OFFICE_ID, isUpdate);
                    loadTemperDetail(LoginInfo.OFFICE_ID, LoginInfo.TEMPER_ID, isUpdate);
                    loadOndutyData(LoginInfo.OFFICE_ID, isUpdate);
                    loadExchangeData(LoginInfo.OFFICE_ID, isUpdate);
                } else {
                    loadOndutyData(LoginInfo.OFFICE_ID, isUpdate);
                    loadNote(LoginInfo.OFFICE_ID, isUpdate);
                    startTimeThread();
                    loadNursingItem(LoginInfo.OFFICE_ID, isUpdate);
                    loadExchangeData(LoginInfo.OFFICE_ID, isUpdate);
                }
            }
        };
        getUtil.doGet(this, url, isUpdate, "患者信息");
    }


    /**
     * 读取所有便签
     *
     * @param officeId
     */
    private void loadNote(String officeId, boolean isUpdate) {

        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_MEMORANDUM
                + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mNoteList = JsonUtil.getNoteFromJson(json);
                L.i("共读取到便签信息数量：" + mNoteList.size());
                if (!JsonUtil.isEmpty(mNoteList)) {
                    setNoteData();
                } else {
                    for (NoteView nv : mNoteViewList) {
                        mMainLayout.removeView(nv);
                    }
                    mNoteViewList.clear();
                }
            }

        };
        getUtil.doGet(this, url, isUpdate, "便签信息");
    }


    /**
     * 设置便签的内容
     */
    private void setNoteData() {
        //  添加前先删除
        for (NoteView nv : mNoteViewList) {
            mMainLayout.removeView(nv);
        }
        mNoteViewList.clear();

        for (Note note : mNoteList) {
            addNote(note, false);
        }
        makeLatestRed();
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
                mExchangeList = JsonUtil.getExchangeFromJson(json);
                LoginInfo.mExchangeList = mExchangeList;
                L.i("共读取到换床信息数量：" + mExchangeList.size());
                clearExchangeBed();
                if (!JsonUtil.isEmpty(mExchangeList)) {
                    setExchangeData();
                }
            }

        };
        getUtil.doGet(this, url, isUpdate, "换床信息");
    }

    /**
     * 清除换床信息
     */
    private void clearExchangeBed() {
        for (int i = 0; i < 3; i++) {
            mExchangeFroms[i].setText("");
            mExchangeTos[i].setText("");
            mExchangeArrows[i].setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 设置换床信息
     */
    private void setExchangeData() {
        for (int i = 0; i < mExchangeList.size(); i++) {
            Exchange ex = mExchangeList.get(i);
            if (i > 2) {
                return;
            } else {
                mExchangeFroms[i].setText(ex.getOldBed());
                mExchangeTos[i].setText(ex.getNewBed());
                mExchangeArrows[i].setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 读取体温的频率等信息
     *
     * @param officeId
     * @param itemId
     */
    private void loadTemperData(final String officeId, final String itemId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_CONDITIONS_ITEM_SET_BY_ITEM_ID
                + "?officeid=" + officeId + "&itemId=" + itemId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mTemperList = JsonUtil.getTemperFromJson(json);
                L.i("共读取到体温项目数量：" + mTemperList.size());
                if (!JsonUtil.isEmpty(mTemperList)) {
                    setTemperFrequency();
                    loadTemperDetail(officeId, itemId, false);
                }
            }

        };
        getUtil.doGet(this, url, "体温项目");
    }


    /**
     * 获取体温明细信息
     *
     * @param officeId
     * @param itemId
     */
    private void loadTemperDetail(final String officeId, final String itemId, boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.Find_Records_By_Office_ID
                + "?officeid=" + officeId + "&itemID=" + itemId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mTemperDetailList = JsonUtil.getTemperDetailFromJson(json);
                L.i("共读取到体温项目详细数量：" + mTemperDetailList.size());
                if (!JsonUtil.isEmpty(mTemperDetailList)) {
                    setTemperData();
                }
            }

        };
        getUtil.doGet(this, url, isUpdate, "体温项目详细");
    }

    /**
     * 设置体温详细信息
     */
    private void setTemperData() {
        for (int i = 0; i < mImportantList.size(); i++) {
            if (mImportantList.get(i).getItemType().equals(CommonValues.TYPE_TEMPER)) {
                // 设置数据之前先清空之前的数据
                mTopBlocks[mImportantList.get(i).getDisplayOrder() - 1].clearAllBedInfo();
                for (int k = 0; k < mTemperList.size(); k++) {
                    List<BedInfo> list = new ArrayList<>();
                    for (int j = 0; j < mTemperDetailList.size(); j++) {
                        if (mTemperDetailList.get(j).getFrequencyId().equals(mTemperList.get(k).getFrequencyId())) {
                            BedInfo info = new BedInfo();
                            Patient p = NsisUtil.getPatientByHosIdAndInTimes(mTemperDetailList.get(j).getHosId(),
                                    mTemperDetailList.get(j).getInTimes());
                            if (p != null) {
                                info.setNo(p.getBedNo());
                                info.setContent(p.getName());
                                info.setHosId(p.getHosId());
                                info.setInTimes(p.getInTimes());
                            } else {
                                info.setNo("空");
                                info.setContent("");
                                info.setHosId("");
                                info.setInTimes(0);
                            }
                            info.setEvent(mTemperDetailList.get(j).getEvent());
                            info.setNursing(mImportantList.get(i));
                            info.setColor("#" + mImportantList.get(i).getTitleColorString());
                            list.add(info);
                        }
                    }
//                    System.setProperty("java.com.jerry.festivalmessage.util.Arrays.useLegacyMergeSort", "true");
                    Collections.sort(list, new BedComparator());
                    mTopBlocks[mImportantList.get(i).getDisplayOrder() - 1].setBedInfoList(k, list);
                }
                return;
            }
        }
    }

    /**
     * 设置体温频率信息
     */
    private void setTemperFrequency() {
        for (int i = 0; i < mLeftTexts.length; i++) {
            if (mLeftTexts[i].getText().equals("体温")) {
                for (int j = 0; j < mTemperList.size(); j++) {
                    mTopBlocks[i].setFrequency(j, mTemperList.get(j).getFrequency());
                }
                return;
            }
        }
    }

    /**
     * 获取护理项目信息
     */
    private void loadNursingItem(final String officeId, boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_NURSE_ITEM + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mNursingList = JsonUtil.getNursingItemFromJson(json);
                L.i("共读取到护理项目数量：" + mNursingList.size());
                if (!JsonUtil.isEmpty(mNursingList)) {
                    classifyNursingData();
                    setImportantTitle();
                    setUnImportantTitle();
                    loadTemperData(officeId, LoginInfo.TEMPER_ID);
                    loadNursingDetail(officeId, false);
                }
            }

        };
        getUtil.doGet(this, url, isUpdate, "护理项目");
    }


    /**
     * 获取今日值班信息
     */
    private void loadOndutyData(final String officeId, boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_DOCTOR_SCHEDULING_BY_OFFICE_ID + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mDutyList = JsonUtil.getOndutyFromJson(json);
                L.i("共读取到值班信息数量：" + mDutyList.size());
                if (!JsonUtil.isEmpty(mDutyList)) {
                    setDutyData();
                }
            }

        };
        getUtil.doGet(this, url, isUpdate, "值班信息");
    }


    /**
     * 设置值班数据
     */
    private void setDutyData() {
        // 显示前先清空之前的数据
        mTextFirstLine.setText("");
        mEditFirstline.setText("");
        mTextSecondLine.setText("");
        mEditSecondline.setText("");
        mTextThirdLine.setText("");
        mEditThirdline.setText("");
        mTextAllLine.setText("");
        mEditAllline.setText("");
        for (Duty duty : mDutyList) {
            if (duty.getOrder() == 1) {
                mTextFirstLine.setText(duty.getName());
                mEditFirstline.setText(duty.getContent());
                mTextFirstLine.setVisibility(View.VISIBLE);
                mEditFirstline.setVisibility(View.VISIBLE);
            } else if (duty.getOrder() == 2) {
                mTextSecondLine.setText(duty.getName());
                mEditSecondline.setText(duty.getContent());
                mTextSecondLine.setVisibility(View.VISIBLE);
                mEditSecondline.setVisibility(View.VISIBLE);
            } else if (duty.getOrder() == 3) {
                mTextThirdLine.setText(duty.getName());
                mEditThirdline.setText(duty.getContent());
                mTextThirdLine.setVisibility(View.VISIBLE);
                mEditThirdline.setVisibility(View.VISIBLE);
            } else if (duty.getOrder() == 4) {
                mTextAllLine.setText(duty.getName());
                mEditAllline.setText(duty.getContent());
                mTextAllLine.setVisibility(View.VISIBLE);
                mEditAllline.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 读取护理信息明细
     */
    private void loadNursingDetail(String officeId, boolean isUpdate) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_NURSE_RECORD_BY_OFFICE_ID + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mNurnsingDetailList = JsonUtil.getNursingDetailFromJson(json);
                L.i("共读取到护理详细数量：" + mNurnsingDetailList.size());
                if (!JsonUtil.isEmpty(mNurnsingDetailList)) {
                    if (!JsonUtil.isEmpty(mImportantList)) {
                        setImportantData();
                    }
                    if (!JsonUtil.isEmpty(mUnImportantList)) {
                        setUnImportantContent();
                    }
                }
            }

        };
        getUtil.doGet(this, url, isUpdate, "护理详细");
    }

    /**
     * 设置下方区域所有内容
     */
    private void setUnImportantContent() {
        // 在设置信息前县清空所有的信息
        for (BottomBlock bb : mBottomBlockList) {
            bb.clearAllBedInfo();
        }
        for (Nursing nursing : mUnImportantList) {
            // 每9个分成一组
            int order = nursing.getDisplayOrder();
            int count = (order - 1) / 9;
            int index = (order - 1) % 9;
            List<BedInfo> infoList = new ArrayList<>();
            for (NursingDetail detail : mNurnsingDetailList) {
                if (nursing.getItemId().equals(detail.getItemId())) {
                    BedInfo info = new BedInfo();
                    info.setNo(detail.getBedId());
                    if (nursing.getShowPatientName() == 0) {
                        info.setContent(detail.getContent());
                    } else {
                        info.setContent(NsisUtil.getPatientNameByHosIdAndInTimes(detail.getHosId(), detail.getInTimes()));
                    }
                    info.setColor("#" + nursing.getTitleColorString());
                    info.setHosId(detail.getHosId());
                    info.setInTimes(detail.getInTimes());
                    info.setNursing(nursing);
                    info.setNursingDetail(detail);
                    infoList.add(info);
                }
            }
//            System.setProperty("java.com.jerry.festivalmessage.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(infoList, new BedComparator());
            mBottomBlockList.get(count).setBedInfoList(index, infoList);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case CustomPromptActivity.NURSING_DETAIL_UPDATE:
                L.i("更新护理详细时间：" + DateUtil.getYMDHMS());
                updateTime = System.currentTimeMillis();
                loadNursingDetail(LoginInfo.OFFICE_ID, true);
                break;
            case ExchangeBedActivity.EXCHANGE_UPDATE:
                L.i("更新床位时间：" + DateUtil.getYMDHMS());
                loadExchangeData(LoginInfo.OFFICE_ID, false);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置下方所有标题
     */
    private void setUnImportantTitle() {
        dialog.show();
        mBottomBlockList = new ArrayList<>();
        int num = (int) Math.ceil(getMaxUnImportantNursingOrder() / 9d);
        for (int i = 0; i < num; i++) {
            BottomBlock block = new BottomBlock(this);
            mBottomBlockList.add(block);
            mBottomLayout.addView(block);
        }
        for (Nursing nursing : mUnImportantList) {
            int order = nursing.getDisplayOrder();
            int count = (order - 1) / 9;
            int index = (order - 1) % 9;
            mBottomBlockList.get(count).setType(index, nursing.getItemName());
            mBottomBlockList.get(count).setColor(index, "#" + nursing.getTitleColorString());
            mBottomBlockList.get(count).setNursing(index, nursing);
        }
        dialog.dismiss();
    }

    /**
     * 获取下部分显示的最大DisplayOrder序号
     *
     * @return
     */
    private int getMaxUnImportantNursingOrder() {
        int max = 0;
        for (Nursing nursing : mUnImportantList) {
            if (nursing.getDisplayOrder() > max) {
                max = nursing.getDisplayOrder();
            }
        }
        return max;
    }

    /**
     * 设置上方区域重要显示信息（标题）
     */
    private void setImportantTitle() {
        dialog.show();
        for (Nursing nursing : mImportantList) {
            mLeftTexts[nursing.getDisplayOrder() - 1].setText(nursing.getItemName());
            mLeftTexts[nursing.getDisplayOrder() - 1].setClickable(true);
        }
        for (TextView v : mLeftTexts) {
            if (!v.isClickable()) {
                v.setVisibility(View.GONE);
            }
        }
        setImportantFrequency();
        dialog.dismiss();
    }

    /**
     * 设置上方区域重要显示信息——频率
     */
    private void setImportantFrequency() {
        for (Nursing nursing : mImportantList) {
            for (int i = 0; i < nursing.getFrequencyList().size(); i++) {
                if (!nursing.getItemType().equals(CommonValues.TYPE_TEMPER)) {
                    mTopBlocks[nursing.getDisplayOrder() - 1].setFrequency(i, nursing.getFrequencyList().get(i).getFrequencyName());
//                    mTopBlocks[nursing.getDisplayOrder() - 1].setNursing(i, nursing);
                }
            }
        }
    }

    /**
     * 设置上方重要数据
     */
    private void setImportantData() {
        int tempIndex = -1;
        for (int i = 0; i < mImportantList.size(); i++) {
            if (mImportantList.get(i).getItemType().equals(CommonValues.TYPE_TEMPER)) {
                tempIndex = i;
                break;
            }
        }

        // 在设置信息前清空所有的信息
        for (int i = 0; i < mTopBlocks.length; i++) {
            if (i != tempIndex) {
                mTopBlocks[i].clearAllBedInfo();
            }
        }
        // 先l现存的护理项目类别
        for (Nursing nursing : mImportantList) {
            for (int i = 0; i < nursing.getFrequencyList().size(); i++) {
                List<BedInfo> list = new ArrayList<>();
                Frequency f = nursing.getFrequencyList().get(i);
                for (int j = 0; j < mNurnsingDetailList.size(); j++) {
                    NursingDetail detail = mNurnsingDetailList.get(j);
                    if (detail.getItemId().equals(f.getItemId()) && detail.getFrequencyId().equals(f.getFrequencyId())) {
                        BedInfo info = new BedInfo();
                        // 0不显示病人信息，1显示病人信息
                        info.setNo(detail.getBedId());
                        if (nursing.getShowPatientName() == 0) {
                            info.setContent(detail.getContent());
                        } else {
                            info.setContent(NsisUtil.getPatientNameByHosIdAndInTimes(detail.getHosId(), detail.getInTimes()));
                        }
                        info.setColor("#" + nursing.getTitleColorString());
                        info.setHosId(detail.getHosId());
                        info.setInTimes(detail.getInTimes());
                        info.setNursing(nursing);
                        info.setNursingDetail(detail);
                        list.add(info);
                    }
                }
//                System.setProperty("java.com.jerry.festivalmessage.util.Arrays.useLegacyMergeSort", "true");
                Collections.sort(list, new BedComparator());
                mTopBlocks[nursing.getDisplayOrder() - 1].setBedInfoList(i, list);
            }
        }
    }


    /**
     * 设置护理项目数据
     */
    private void classifyNursingData() {
        mImportantList = new ArrayList<>();
        mUnImportantList = new ArrayList<>();
        for (Nursing nursing : mNursingList) {
            if (nursing.isImportantShow()) {
                mImportantList.add(nursing);
            } else {
                mUnImportantList.add(nursing);
            }
            if (nursing.getItemName().equals("体温")) {
                LoginInfo.TEMPER_ID = nursing.getItemId();
            }
        }
        L.i("重要的护理项目数量：" + mImportantList.size());
        L.i("非重要的护理项目数量：" + mUnImportantList.size());
    }

    /**
     * 初始化布局页面
     */
    private void initView() {
        // 初始化等待框
        dialog = new MyProgressDialog(this);
        // 初始化Logo
        initLogoLayout();
        // 初始化换床信息
        initExchangebedLayout();
        // 初始化提醒项
        initNoteLayout();
        // 初始化值班布局
        initOndutyLayout();
        // 初始化左上部分的布局设置
        initTopMainLayout();
        // 中心菜单是否 可以移动
        setCenterMenuMovable();
    }

    /**
     * 初始化Titlebar内容
     */
    private void initLogoLayout() {
        mTextHospital.setText(LoginInfo.HOSPITAL_NAME);
        mTextOffice.setText(LoginInfo.OFFICE_NAME);
    }


    /**
     * 几个通讯录的点击事件
     */
    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                switch (v.getId()) {
                    case R.id.et_firstline:
                        updateOnDutyData(LoginInfo.OFFICE_ID, mDutyList.get(0).getId(), mEditFirstline.getText().toString());
                        break;
                    case R.id.et_secondline:
                        updateOnDutyData(LoginInfo.OFFICE_ID, mDutyList.get(1).getId(), mEditSecondline.getText().toString());
                        break;
                    case R.id.et_thirdline:
                        updateOnDutyData(LoginInfo.OFFICE_ID, mDutyList.get(2).getId(), mEditThirdline.getText().toString());
                        break;
                    case R.id.et_allline:
                        updateOnDutyData(LoginInfo.OFFICE_ID, mDutyList.get(3).getId(), mEditAllline.getText().toString());
                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 更新医生值班信息
     *
     * @param officeID
     * @param docscheID
     * @param scheValue
     */
    private void updateOnDutyData(String officeID, String docscheID, String scheValue) {
        try {
            String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.MODIFY_DOCTOR_SCHEDULING_VALUE
                    + "?officeID=" + officeID + "&DocscheID=" + docscheID + "&ScheValue=" + URLEncoder.encode(scheValue, "UTF-8");
            HttpGetUtil getUtil = new HttpGetUtil() {
                @Override
                public void success(String json) {
                    L.i("更新值班信息返回值：" + json);
                }
            };
            getUtil.doGet(this, url, false, "更新值班信息");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化值班布局
     */
    private void initOndutyLayout() {
        mEditFirstline.setOnFocusChangeListener(focusListener);
        mEditSecondline.setOnFocusChangeListener(focusListener);
        mEditThirdline.setOnFocusChangeListener(focusListener);
        mEditAllline.setOnFocusChangeListener(focusListener);
    }

    /**
     * 初始化右下角提醒项布局
     */
    private void initNoteLayout() {
        // 创建存放提醒项小布局的集合
        mNoteViewList = new ArrayList<>();
    }

    /**
     * 打开提醒项窗口
     */
    private void openNotes() {
        mNoteLayoutOpen = true;
        for (int i = 0; i <= mNoteViewList.size() - 1; i++) {
            if (i < NOTE_MAX) {
                NoteView view = mNoteViewList.get(i);
                view.setDeleteEnable(true);
                PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", 0f, DensityUtils.dp2px(this, -(i + 1) * NOTE_OFFSET));
                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f);
                PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f);
                final ObjectAnimator openAnim = ObjectAnimator.ofPropertyValuesHolder(view, translationX, scaleX, scaleY).setDuration(500);
                openAnim.setInterpolator(new OvershootInterpolator());
                openAnim.start();
            } else {
                mNoteViewList.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 关闭提醒项窗口
     */
    private void closeNotes() {
        mNoteLayoutOpen = false;
        for (int i = 0; i <= mNoteViewList.size() - 1; i++) {
            if (i < NOTE_MAX) {
                NoteView view = mNoteViewList.get(i);
                view.setDeleteEnable(false);
                PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", DensityUtils.dp2px(this, -(i + 1) * NOTE_OFFSET), 0f);
                PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1f);
                PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1f);
                final ObjectAnimator openAnim = ObjectAnimator.ofPropertyValuesHolder(view, translationX, scaleX, scaleY).setDuration(500);
                openAnim.setInterpolator(new AnticipateInterpolator());
                openAnim.start();
            }
        }
        mNoteLayout.bringToFront();
    }

    /**
     * 向提醒项布局添加一个提醒项
     */
    public void addNote(Note note, boolean isManual) {
        NoteView view = new NoteView(this);
        view.setNote(note);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                DensityUtils.dp2px(this, 270), DensityUtils.dp2px(this, 320));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        view.setLayoutParams(lp);
        mMainLayout.addView(view);
        mNoteViewList.add(view);
        addView2Note(view, isManual);
    }

    /**
     * 设置最新的一条信息的时间为红色
     */
    private void makeLatestRed() {
        int maxIndex = 0;
        String maxTime = null;
        for (int i = 0; i < mNoteViewList.size(); i++) {
            NoteView v = mNoteViewList.get(i);
            if (i == 0) {
                maxTime = v.getNote().getTime();
            } else {
                if (DateUtil.compareDate(v.getNote().getTime(), maxTime) == 1) {
                    maxIndex = i;
                    maxTime = v.getNote().getTime();
                }
            }
        }
        for (int i = 0; i < mNoteViewList.size(); i++) {
            if (i == maxIndex) {
                mNoteViewList.get(i).makeTimeRed();
            } else {
                mNoteViewList.get(i).makeTimeNormal();
            }
        }
    }

    private void addView2Note(NoteView v, final boolean isManual) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowNoteIndex = mNoteViewList.indexOf(v);
                L.i("便签显示序号是：" + mShowNoteIndex);
                mNoteLayoutOpen = false;
                v.bringToFront();
                mNoteLayout.bringToFront();
                if (isManual) {
                    if (mNoteViewList.indexOf(v) == mNoteViewList.size() - 1) {
                        //             ((NoteView) v).setTimeHighLight();
                    }
                } else {
                    if (mNoteViewList.indexOf(v) == 0) {
                        //        ((NoteView) v).setTimeHighLight();
                    }
                }
                closeNotes();

            }
        });
        if (isManual) {
            mNoteViewList.get(mNoteViewList.size() - 1).bringToFront();
            //      mNoteViewList.get(mNoteViewList.size() - 1).setTimeHighLight();
        } else {
            mNoteViewList.get(0).bringToFront();
            //        mNoteViewList.get(0).setTimeHighLight();
        }
        mNoteLayout.bringToFront();
    }


    /**
     * 控制中心菜单是否可以拖动
     */
    private void setCenterMenuMovable() {
        mMainMenu.getCenterButton().setOnTouchListener(touchListener);
    }

    private int downX;
    private int downY;

    private int x;
    private int y;


    /**
     * 主菜单的拖动事件
     */
    private View.OnTouchListener touchListener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            int eventAction = event.getAction();

            x = (int) event.getRawX();
            y = (int) event.getRawY();

            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    mMainMenu.setMove(false);
                    downX = (int) event.getX();
                    downY = y - mMainMenu.getTop();
                    mMainMenu.postInvalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mMainMenu.setMove(true);
                    mMainMenu.openTime = System.currentTimeMillis();
                    int left = x - downX;
                    int top = y - downY;
                    int right = left + mMainMenu.getWidth();
                    int bottom = top + mMainMenu.getHeight();
                    mMainMenu.layout(left - (mMainMenu.getWidth() - mMainMenu.getCenterButton().getWidth()) / 2, top,
                            right - (mMainMenu.getWidth() - mMainMenu.getCenterButton().getWidth()) / 2, bottom);
                    mMainMenu.postInvalidate();
                    return false;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return false;
        }
    };


    /**
     * 初始化页面主窗口上部分的内容
     */
    private void initTopMainLayout() {
        // 首先设置4个区域都不可以点击，方便后面通过检测是否可以点击来控制显示与否
        mLeftTexts = new TextView[]{mLeftText1, mLeftText2, mLeftText3, mLeftText4};
        for (TextView v : mLeftTexts) {
            v.setClickable(false);
        }
        mTopBlocks = new TopBlock[]{mTopBlock1, mTopBlock2, mTopBlock3, mTopBlock4};
    }

    /**
     * 初始化换床信息的内容
     */
    private void initExchangebedLayout() {
        mExchangeFroms = new TextView[]{mExchangeFrom1, mExchangeFrom2, mExchangeFrom3};
        mExchangeTos = new TextView[]{mExchangeTo1, mExchangeTo2, mExchangeTo3};
        mExchangeArrows = new TextView[]{mExchangeArrow1, mExchangeArrow2, mExchangeArrow3};
    }


    /**
     * 开始时间显示组件
     */
    private void startTimeThread() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(1000);
                                Message msg = mHandler.obtainMessage();
                                msg.what = UPDATE_TIME;
                                msg.sendToTarget();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }

    /**
     * 左侧4个按钮的点击事件
     *
     * @param v
     */
    @OnClick({R.id.tv_left1, R.id.tv_left2, R.id.tv_left3, R.id.tv_left4})
    public void onLeftMainClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left1:
                changeTopmain(TAG.FIRST);
                break;
            case R.id.tv_left2:
                changeTopmain(TAG.SECOND);
                break;
            case R.id.tv_left3:
                changeTopmain(TAG.THIRD);
                break;
            case R.id.tv_left4:
                changeTopmain(TAG.FORTH);
                break;
            default:
                break;
        }
    }

    /**
     * 切换标签
     *
     * @param tag
     * @return
     */
    private void changeTopmain(TAG tag) {
        if (mTag != tag) {
            // 重置按钮状态，刷新数据，显示动画
            resetLeftLayout(tag);
            mTag = tag;
            setImportantFrequency();
            //     AnimationUtil.startTopmainAmimation(mTopMainLayout);
        }
    }

    /**
     * 重置上方左侧的布局按钮
     */
    private void resetLeftLayout(TAG tag) {
        for (TextView textView : mLeftTexts) {
            textView.setBackgroundResource(R.color.slide_unselectColor);
            textView.setTextColor(getResources().getColor(R.color.exchangebed_textColor));
        }

        mLeftTexts[tag.ordinal()].setBackgroundResource(R.color.slide_selectColor);
        mLeftTexts[tag.ordinal()].setTextColor(getResources().getColor(R.color.white));
        AnimationUtil.showTagAnimation(mTopBlocks[mTag.ordinal()], mTopBlocks[tag.ordinal()]);
    }

    /**
     * 几个联系通讯录的点击事件
     *
     * @param v
     */
    @OnClick({R.id.ll_contact_food, R.id.iv_contact_food, R.id.ll_contact_property, R.id.iv_contact_property, R.id.ll_contact_repair, R.id.iv_contact_repair,
            R.id.ll_contact_emergency, R.id.iv_contact_emergency, R.id.ll_contact_hospital, R.id.iv_contact_hospital, R.id.ll_contact_others, R.id.iv_contact_others})
    public void onContact(View v) {
        ContactPopupWindow contactPopupWindow = new ContactPopupWindow(this);
        switch (v.getId()) {
            case R.id.ll_contact_food:
            case R.id.iv_contact_food:
                contactPopupWindow.showWindow(v, "订餐", getResources().getDrawable(R.drawable.icon_dc_2));
                break;
            case R.id.ll_contact_property:
            case R.id.iv_contact_property:
                contactPopupWindow.showWindow(v, "物业", getResources().getDrawable(R.drawable.icon_wy_2));
                break;
            case R.id.ll_contact_repair:
            case R.id.iv_contact_repair:
                contactPopupWindow.showWindow(v, "维修", getResources().getDrawable(R.drawable.icon_wx_2));
                break;
            case R.id.ll_contact_emergency:
            case R.id.iv_contact_emergency:
                contactPopupWindow.showWindow(v, "急诊", getResources().getDrawable(R.drawable.icon_jz_2));
                break;
            case R.id.ll_contact_hospital:
            case R.id.iv_contact_hospital:
                contactPopupWindow.showWindow(v, "全院", getResources().getDrawable(R.drawable.icon_ks_2));
                break;
            case R.id.ll_contact_others:
            case R.id.iv_contact_others:
                contactPopupWindow.showWindow(v, "其他", getResources().getDrawable(R.drawable.icon_qt_2));
                break;
            default:
                break;
        }
    }


    /**
     * 换床信息
     *
     * @param v
     */
    @OnClick(R.id.v_exchange)
    public void onExchangebed(View v) {
        Intent intent = new Intent();
        intent.setClass(this, ExchangeBedActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * 退出应用
     *
     * @param v
     */
    @OnClick(R.id.iv_time)
    public void onTimeImage(View v) {
        NsisApplication.getInstance().finishApplication();
    }


    public static final String NOTE_UPDATE = "note_update";

    private BroadcastReceiver noteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.i("检测到有提醒项变化了！");
            if (mNoteLayoutOpen) {
                closeNotes();
            }
            loadNote(LoginInfo.OFFICE_ID, false);
        }
    };

    public static final String EXCHANGE_UPDATE = "exchange_update";

    private BroadcastReceiver exchangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.i("检测到有换床变化了！");
            loadExchangeData(LoginInfo.OFFICE_ID, false);
        }
    };

    private IntentFilter noteFilter = new IntentFilter(NOTE_UPDATE);
    private IntentFilter exchangeFilter = new IntentFilter(EXCHANGE_UPDATE);

    /**
     * 便签展开或者关上的点击事件
     *
     * @param v
     */
    @OnClick(R.id.rl_note)
    public void onNoteLayout(View v) {
        if (mNoteEditState) {
            closeNoteEditLayout();
            return;
        }
        if (mNoteLayoutOpen) {
            closeNotes();
        } else {
            openNotes();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(noteReceiver);
        unregisterReceiver(exchangeReceiver);
    }


    /**
     * 设置配置的参数
     *
     * @param v
     */
    @OnClick(R.id.iv_logo)
    public void onSetting(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 编辑便签的点击事件
     *
     * @param v
     */
    @OnClick(R.id.v_edit)
    public void onNoteEditLayout(View v) {
        if (mNoteList.size() == 0) {
            return;
        }
        if (mNoteLayoutOpen) {
            closeNotes();
        } else {
            if (mNoteEditState) {
                closeNoteEditLayout();
            } else {
                openNoteEditLayout();
            }
        }
    }

    /**
     * 打开便签
     */
    private void openNoteEditLayout() {
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", DensityUtils.dp2px(this, -40F), 0f);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        ObjectAnimator openAnim = ObjectAnimator.ofPropertyValuesHolder(mNoteEditLayout, translationY, alpha).setDuration(500);
        openAnim.start();
        mNoteEditLayout.setVisibility(View.VISIBLE);
        mNoteEditState = true;
    }


    /**
     * 关闭便签
     */
    private void closeNoteEditLayout() {
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0f, DensityUtils.dp2px(this, -40F));
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator closeAnim = ObjectAnimator.ofPropertyValuesHolder(mNoteEditLayout, translationY, alpha).setDuration(500);
        closeAnim.start();
        closeAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mNoteEditLayout.setVisibility(View.INVISIBLE);
                mNoteEditState = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 编辑便签的点击事件
     *
     * @param v
     */
    @OnClick(R.id.iv_edit)
    public void onNoteEdit(View v) {
        closeNoteEditLayout();
        Intent intent = new Intent();
        intent.putExtra("note", mNoteViewList.get(mShowNoteIndex).getNote());
        intent.setClass(this, NoteActivity.class);
        startActivity(intent);
    }

    /**
     * 删除便签的点击事件
     *
     * @param v
     */
    @OnClick(R.id.iv_delete)
    public void onNoteDelete(View v) {
        closeNoteEditLayout();
        String id = mNoteViewList.get(mShowNoteIndex).getNote().getId();
        deleteNote(id);
    }

    /**
     * 删除便签
     *
     * @param noteId
     */
    private void deleteNote(String noteId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.DELETE_MEMORANDUM
                + "?MemoID=" + noteId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                L.i("删除便签返回值：" + json);
                loadNote(LoginInfo.OFFICE_ID, false);
            }
        };
        getUtil.doGet(this, url, false, "删除便签");
    }

    /**
     * 更新大屏消息通知
     */
    private void startUpdateNotice() {
        Thread updateNoticeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mUpdateNotice) {
                    try {
                        Message msg = mHandler.obtainMessage();
                        msg.what = UPDATE_NOTICE_DATA;
                        msg.sendToTarget();
                        Thread.sleep(LOAD_UPDATE_NOTICE_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        updateNoticeThread.start();
    }

    /**
     * 获取通知消息
     *
     * @param officeId
     */
    private void loadNotice(String officeId) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.CONTACT_SERVICE + ServiceConstant.FIND_NOTICE_BY_OFFICE
                + "?officeID=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                countNotice(json);
            }
        };
        getUtil.doGet(this, url, true, "获取通知消息");
    }


    /**
     * 统计通知的数量并添加到集合中
     *
     * @param json
     */
    private void countNotice(String json) {
        List<Notice> list = JsonUtil.getNoticeFromJson(json);
        boolean has = false;
        for (Notice n : list) {
            for (Notice ntc : mNoticeList) {
                if (ntc.getId().equals(n.getId())) {
                    has = true;
                }
            }
            if (!has) {
                L.i("添加了一条通知消息");
                mNoticeList.add(n);
                mNoticeView.addNotice(n);
            }
        }
        if (!mNoticeView.isStart()) {
            // 开启通知滚动查询
            mNoticeView.start();
        }
    }


    /**
     * 读取视频的URl地址
     */
    private void loadVideoUrl() {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.VIDEO_SERVICE + ServiceConstant.GET_MOVE_ADDRESS;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                LoginInfo.mFileUrl = JsonUtil.getVideoUrlFromJson(json);
                if (LoginInfo.mFileUrl != null) {
                    L.i("读取到视频URL地址：" + LoginInfo.mFileUrl.getUrl());
                }
            }
        };
        getUtil.doGet(this, url, "读取视频URL地址");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUpdateNursing = true;
        mUpdateMenu = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUpdateNursing = false;
        mUpdateMenu = false;
    }

    /**
     * 重写返回按钮的
     */
    @Override
    public void onBackPressed() {
    }

    @OnClick(R.id.tv_time)
    public void onScreenSaver(View v) {
        Intent intent = new Intent(this, ScreenSaverActivity.class);
        startActivity(intent);
    }


}
