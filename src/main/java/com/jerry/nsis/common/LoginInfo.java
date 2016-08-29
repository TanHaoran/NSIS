package com.jerry.nsis.common;

import com.jerry.nsis.entity.Doctor;
import com.jerry.nsis.entity.Exchange;
import com.jerry.nsis.entity.Nurse;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.entity.Schedule;
import com.jerry.nsis.entity.VideoUrl;

import java.util.List;

/**
 * Created by Jerry on 2016/3/7.
 */
public class LoginInfo {

    public static String HOSPITAL_NAME = "";
    public static String OFFICE_ID = "";
    public static String OFFICE_NAME = "";
    public static String TEMPER_ID = "";

    public static List<Patient> mPatientList;
    public static List<Doctor> mDocList;
    public static List<Exchange> mExchangeList;
    public static  List<Schedule> mScheduleList;
    public static List<Nurse> mNurseList;
    public static VideoUrl mFileUrl;
}
