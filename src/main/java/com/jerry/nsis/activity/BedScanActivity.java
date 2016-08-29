package com.jerry.nsis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.BedScanAdapter;
import com.jerry.nsis.adapter.CommonAdapter;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.Cost;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.view.ScanButton;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_bedscan)
public class BedScanActivity extends Activity {

    @ViewInject(R.id.gv_bedscan)
    private GridView mGridView;

    private List<Bed> mBedList;
    private List<Cost> mCostList;
    private BedScanAdapter mAdapter;

    @ViewInject(R.id.btn_all)
    private ScanButton btnAll;
    @ViewInject(R.id.btn_serious)
    private ScanButton btnSerious;
    @ViewInject(R.id.btn_danger)
    private ScanButton btnDanger;
    @ViewInject(R.id.btn_special)
    private ScanButton btnSpecial;
    @ViewInject(R.id.btn_first)
    private ScanButton btnFirst;
    @ViewInject(R.id.btn_second)
    private ScanButton btnSecond;
    @ViewInject(R.id.btn_third)
    private ScanButton btnThird;
    @ViewInject(R.id.btn_empty)
    private ScanButton btnEmpty;
    @ViewInject(R.id.btn_now)
    private ScanButton btnNow;
    @ViewInject(R.id.btn_in)
    private ScanButton btnIn;
    @ViewInject(R.id.btn_wait)
    private ScanButton btnWait;

    private ScanButton[] mBtns;


    /**
     * 病重
     */
    private List<Bed> mSeriousList;
    /**
     * 病危
     */
    private List<Bed> mDangerList;
    /**
     * 特级
     */
    private List<Bed> mSpecialList;
    /**
     * 一级
     */
    private List<Bed> mFirstList;
    /**
     * 二级
     */
    private List<Bed> mSecondList;
    /**
     * 三级
     */
    private List<Bed> mThirdList;
    /**
     * 空床
     */
    private List<Bed> mEmptyList;
    /**
     * 现
     */
    private List<Bed> mNowList;
    /**
     * 入
     */
    private List<Bed> mInList;
    /**
     * 待
     */
    private List<Bed> mWaitList;

    private enum TYPE {
        ALL, SERIOUS, DANGER, SPECIAL, FIRST, SECOND, THIRD, EMPTY, NOW, IN, WAIT
    }

    private TYPE mType = TYPE.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NsisApplication.getInstance().addActivity(this);

        initView();

        loadBedScanData(LoginInfo.OFFICE_ID, false, mType);
    }

    private void initView() {
        mBtns = new ScanButton[]{btnAll, btnSerious, btnDanger, btnSpecial, btnFirst, btnSecond,
                btnThird, btnEmpty, btnNow, btnIn, btnWait};
        btnAll.setFocus(true);
    }


    /**
     * 获取所有床位信息
     */
    private void loadBedScanData(final String officeId, boolean isUpdate, final TYPE type) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_BED_FILE_EXIST_EMPTY_ALL + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mBedList = JsonUtil.getBedInfolFromJson(json);
                loadAllPatientCostData(officeId, type);
            }

        };
        getUtil.doGet(this, url, isUpdate, "读取床位");
    }

    /**
     * 读取所有病人花费
     */
    private void loadAllPatientCostData(final String officeId, final TYPE type) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE + ServiceConstant.FIND_PATIENT_INFO_SPEND_BY_OFFICE_ID + "?officeid=" + officeId;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                mCostList = JsonUtil.getCostListFromJson(json);
                L.i("共读病人花费数量：" + mCostList.size());
                if (!JsonUtil.isEmpty(mBedList)) {
                    setCostData();
                    classifyData();
                    setAllBtnNumber();
                    setBedData(type);
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
                    if (p.getHosId().equals(c.getHosId()) && p.getInTimes() == c.getInTimes()) {
                        p.setInsurance(c.getType());
                    }
                }
            }
        }
    }


    private void setAllBtnNumber() {
        setBtnNumber(mBedList, btnAll);
        setBtnNumber(mSeriousList, btnSerious);
        setBtnNumber(mDangerList, btnDanger);
        setBtnNumber(mSpecialList, btnSpecial);
        setBtnNumber(mFirstList, btnFirst);
        setBtnNumber(mSecondList, btnSecond);
        setBtnNumber(mThirdList, btnThird);
        setBtnNumber(mEmptyList, btnEmpty);
        setBtnNumber(mNowList, btnNow);
        setBtnNumber(mInList, btnIn);
        setBtnNumber(mWaitList, btnWait);
    }

    private void setBtnNumber(List<Bed> list, ScanButton btn) {
        btn.setNumber(list.size());
    }

    @OnClick({R.id.btn_all, R.id.btn_serious, R.id.btn_special, R.id.btn_danger, R.id.btn_first, R.id.btn_second,
            R.id.btn_third, R.id.btn_empty, R.id.btn_in, R.id.btn_now, R.id.btn_wait})
    public void btnClick(View v) {
        resetBtnState(v);
        switch (v.getId()) {
            case R.id.btn_all:
//                setBedData(TYPE.ALL);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.ALL);
                break;
            case R.id.btn_serious:
//                setBedData(TYPE.SERIOUS);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.SERIOUS);
                break;
            case R.id.btn_danger:
//                setBedData(TYPE.DANGER);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.DANGER);
                break;
            case R.id.btn_special:
//                setBedData(TYPE.SPECIAL);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.SPECIAL);
                break;
            case R.id.btn_first:
//                setBedData(TYPE.FIRST);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.FIRST);
                break;
            case R.id.btn_second:
//                setBedData(TYPE.SECOND);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.SECOND);
                break;
            case R.id.btn_third:
//                setBedData(TYPE.THIRD);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.THIRD);
                break;
            case R.id.btn_empty:
//                setBedData(TYPE.EMPTY);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.EMPTY);
                break;
            case R.id.btn_now:
//                setBedData(TYPE.NOW);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.NOW);
                break;
            case R.id.btn_in:
//                setBedData(TYPE.IN);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.IN);
                break;
            case R.id.btn_wait:
//                setBedData(TYPE.WAIT);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.WAIT);
                break;
            default:
//                setBedData(TYPE.ALL);
                loadBedScanData(LoginInfo.OFFICE_ID, false, TYPE.ALL);
                break;
        }
    }

    private void resetBtnState(View v) {
        ScanButton b = (ScanButton) v;
        if (b.getFocus()) {
            return;
        } else {
            for (ScanButton btn : mBtns) {
                btn.setFocus(false);
                b.setFocus(true);
            }
        }
    }

    /**
     * 床位信息分类
     */
    private void classifyData() {
        mSeriousList = new ArrayList<>();
        mDangerList = new ArrayList<>();
        mSpecialList = new ArrayList<>();
        mFirstList = new ArrayList<>();
        mSecondList = new ArrayList<>();
        mThirdList = new ArrayList<>();
        mEmptyList = new ArrayList<>();
        mNowList = new ArrayList<>();
        mInList = new ArrayList<>();
        mWaitList = new ArrayList<>();
        for (Bed b : mBedList) {
            Patient p = b.getPatient();
            if (p != null) {
                if (p.getState().equals("病重")) {
                    mSeriousList.add(b);
                } else if (p.getState().equals("病危")) {
                    mDangerList.add(b);
                }
                if (p.getLevel().equals("Ⅰ级护理")) {
                    mFirstList.add(b);
                } else if (p.getLevel().equals("Ⅱ级护理")) {
                    mSecondList.add(b);
                } else if (p.getLevel().equals("特级护理")) {
                    mSpecialList.add(b);
                } else {
                    mThirdList.add(b);
                }
                if (p.getBedNo().equals("")) {
                    mWaitList.add(b);
                } else {
                    mNowList.add(b);
                }
                // 筛选今日入院的或者今日转入的
                if ((p.getInDate().length() > 0 && p.getInDate().split(" ")[0].equals(DateUtil.getYMD())) ||
                        (p.getEvent() != null && p.getEvent().contains("转入"))) {
                    mInList.add(b);
                }
            } else {
                mEmptyList.add(b);
            }
        }

    }


    @OnItemClick(R.id.gv_bedscan)
    public void onGridClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, PatientActivity.class);
        Bed b = mAdapter.getDatas().get(position);
        if (b != null && b.getPatient() != null) {
            intent.putExtra("bed", b);
            startActivity(intent);
        }
    }


    private void setBedData(TYPE type) {
        List<Bed> list = new ArrayList<>();
        switch (type) {
            case ALL:
                list = mBedList;
                break;
            case SERIOUS:
                list = mSeriousList;
                break;
            case DANGER:
                list = mDangerList;
                break;
            case SPECIAL:
                list = mSpecialList;
                break;
            case FIRST:
                list = mFirstList;
                break;
            case SECOND:
                list = mSecondList;
                break;
            case THIRD:
                list = mThirdList;
                break;
            case EMPTY:
                list = mEmptyList;
                break;
            case NOW:
                list = mNowList;
                break;
            case IN:
                list = mInList;
                break;
            case WAIT:
                list = mWaitList;
                break;
            default:
                list = mBedList;
                break;
        }
        mAdapter = new BedScanAdapter(this, list, R.layout.item_bedscan);
        mAdapter.setStyle(CommonAdapter.STYLE.FOUR_SIDE);
        mGridView.setAdapter(mAdapter);
    }


    @OnClick(R.id.ll_close)
    public void onClose(View v) {
        finish();
    }


}
