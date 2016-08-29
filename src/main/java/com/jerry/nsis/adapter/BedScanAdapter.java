package com.jerry.nsis.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.Cost;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.ViewHolder;

import java.util.List;
import java.util.Random;

/**
 * Created by Jerry on 2016/2/18.
 */
public class BedScanAdapter extends CommonAdapter<Bed> {

    private Context mContext;
    private List<Bed> mDatas;

    public BedScanAdapter(Context context, List<Bed> datas, int itemLayoutId) {
        super(context, datas, itemLayoutId);
        mContext = context;
        mDatas = datas;
    }

    @Override
    public void convert(ViewHolder helper, Bed item) {
        helper.setText(R.id.tv_no, item.getNo());
        ((TextView) helper.getView(R.id.tv_col4)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        Patient p = item.getPatient();
        if (p != null) {
            setNormalPatientData(helper, p);
            if (p.getLevel().equals("Ⅰ级护理")) {
                setLevelPatientColor(helper, "#FFf2f2f2", "#FF2BA1FD", "#FFff737e");
            } else if (p.getLevel().equals("Ⅱ级护理")) {
                setLevelPatientColor(helper, "#FFf2f2f2", "#FF2BA1FD", "#FF61bafe");
            } else if (p.getLevel().equals("特级护理")) {
                setLevelPatientColor(helper, "#FFf2f2f2", "#FF2BA1FD", "#FFff737e");
            } else {
                setLevelPatientColor(helper, "#FFf2f2f2", "#FF2BA1FD", "#FF1dc800");
            }
            if (p.getState().equals("病重") || p.getState().equals("病危")) {
                setStatePatientColor(helper, "#FFff737e", "#ffffff", "#FFff737e");
            }
            if (p.getInsurance() != null) {
                helper.setText(R.id.tv_pay, p.getInsurance());
                if (p.getInsurance().contains(Cost.COUNTRY)) {
                    helper.setImageResource(R.id.iv_pay, R.drawable.icon_nh);
                    helper.getView(R.id.iv_pay).setVisibility(View.VISIBLE);
                } else if (p.getInsurance().contains(Cost.INSURANCE)) {
                    helper.setImageResource(R.id.iv_pay, R.drawable.icon_yb);
                    helper.getView(R.id.iv_pay).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.iv_pay).setVisibility(View.INVISIBLE);
                }
            } else {
                helper.getView(R.id.iv_pay).setVisibility(View.INVISIBLE);
            }
        } else {
            setEmptyData(helper);
            setStatePatientColor(helper, "#FFf2f2f2", "#FF2ba2fe", "#FF2BA1FD");
        }


    }


    /**
     * 设置护理等级的颜色信息
     *
     * @param helper
     * @param bedBgColor
     * @param bedColor
     * @param stateColor
     */
    private void setLevelPatientColor(ViewHolder helper, String bedBgColor, String bedColor, String stateColor) {
        helper.getView(R.id.tv_no).setBackgroundColor(Color.parseColor(bedBgColor));
        ((TextView) helper.getView(R.id.tv_no)).setTextColor(Color.parseColor(bedColor));
        ((TextView) helper.getView(R.id.tv_level)).setTextColor(Color.parseColor(stateColor));
    }

    /**
     * 设置病情的颜色信息
     *
     * @param helper
     * @param bedBgColor
     * @param bedColor
     * @param stateColor
     */
    private void setStatePatientColor(ViewHolder helper, String bedBgColor, String bedColor, String stateColor) {
        helper.getView(R.id.tv_no).setBackgroundColor(Color.parseColor(bedBgColor));
        ((TextView) helper.getView(R.id.tv_no)).setTextColor(Color.parseColor(bedColor));
        ((TextView) helper.getView(R.id.tv_state)).setTextColor(Color.parseColor(stateColor));
    }

    /**
     * 空床信息
     *
     * @param helper
     */
    private void setEmptyData(ViewHolder helper) {
        helper.setText(R.id.tv_col4, "");
        helper.setText(R.id.tv_col2, "");
        helper.setText(R.id.tv_sex, "");
        helper.setText(R.id.tv_hosid, "");
        helper.setText(R.id.tv_pay, "");
        helper.setText(R.id.tv_doc, "");
        helper.setText(R.id.tv_date, "");
        helper.setText(R.id.tv_level, "");
        helper.setText(R.id.tv_state, "");
        helper.getView(R.id.iv_pay).setVisibility(View.INVISIBLE);
    }

    /**
     * 普通病人的颜色信息等
     *
     * @param helper
     * @param p
     */
    private void setNormalPatientData(ViewHolder helper, Patient p) {
        helper.setText(R.id.tv_col4, p.getName());
        helper.setText(R.id.tv_col2, String.valueOf(DateUtil.getAge(p.getBirthday())));
        helper.setText(R.id.tv_sex, p.getSex());
        helper.setText(R.id.tv_hosid, p.getHosId());
        helper.setText(R.id.tv_pay, "");
        helper.setText(R.id.tv_doc, p.getDoc());
        helper.setText(R.id.tv_diagnosis, p.getDiagnosis());
        helper.setText(R.id.tv_state, p.getState());
        String date = p.getInDate();
        if (date.length() > 0) {
            helper.setText(R.id.tv_date, date.split(" ")[0]);
        } else {
            helper.setText(R.id.tv_date, date);
        }
        if (p.getLevel().length() > 1) {
            String level = p.getLevel().substring(0, 1);
            helper.setText(R.id.tv_level, level);
        } else {
            helper.setText(R.id.tv_level, "Ⅲ");
        }
        helper.getView(R.id.tv_event_in_hos).setVisibility(View.GONE);
        helper.getView(R.id.tv_event_exchange_in).setVisibility(View.GONE);
        helper.getView(R.id.tv_event_operation).setVisibility(View.GONE);
        helper.getView(R.id.tv_event_out).setVisibility(View.GONE);
        if (p.getEvent() != null) {
            if (p.getEvent().contains(Patient.IN_HOS)) {
                helper.getView(R.id.tv_event_in_hos).setVisibility(View.VISIBLE);
            }
            if (p.getEvent().contains(Patient.EXCHANGE_IN)) {
                helper.getView(R.id.tv_event_exchange_in).setVisibility(View.VISIBLE);
            }
            if (p.getEvent().contains(Patient.OPERATION)) {
                helper.getView(R.id.tv_event_operation).setVisibility(View.VISIBLE);
            }
            if (p.getEvent().contains(Patient.OUT)) {
                helper.getView(R.id.tv_event_out).setVisibility(View.VISIBLE);
            }
        }
    }
}
