package com.jerry.nsis.utils;

import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.entity.Patient;

/**
 * Created by Jerry on 2016/3/11.
 */
public class NsisUtil {

    /**
     * 根据住院号和住院次数查到病人姓名
     *
     * @param hosId
     * @param inTimes
     * @return
     */
    public static String getPatientNameByHosIdAndInTimes(String hosId, int inTimes) {
        for (Patient p : LoginInfo.mPatientList) {
            if (hosId.equals(p.getHosId()) && inTimes == p.getInTimes()) {
                return p.getName();
            }
        }
        L.i("住院号：" + hosId + "，住院次数为：" + inTimes + "没有查到！");
        return "";
    }

    public static Patient getPatientByHosIdAndInTimes(String hosId, int inTimes) {
        for (Patient p : LoginInfo.mPatientList) {
            if (hosId.equals(p.getHosId()) && inTimes == p.getInTimes()) {
                return p;
            }
        }
        L.i("从" + LoginInfo.mPatientList.size() + "个人中查住院号：" + hosId + "，住院次数为：" + inTimes + "没有查到！");
        return null;
    }
}
