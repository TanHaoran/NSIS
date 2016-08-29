package com.jerry.nsis.utils;

import android.text.TextUtils;

import com.jerry.nsis.entity.Bed;
import com.jerry.nsis.entity.Cost;
import com.jerry.nsis.entity.Doctor;
import com.jerry.nsis.entity.Duty;
import com.jerry.nsis.entity.DutyGroup;
import com.jerry.nsis.entity.Education;
import com.jerry.nsis.entity.Exchange;
import com.jerry.nsis.entity.ExchangeBed;
import com.jerry.nsis.entity.Frequency;
import com.jerry.nsis.entity.Note;
import com.jerry.nsis.entity.Notice;
import com.jerry.nsis.entity.Nurse;
import com.jerry.nsis.entity.NurseWork;
import com.jerry.nsis.entity.Nursing;
import com.jerry.nsis.entity.NursingDetail;
import com.jerry.nsis.entity.NursingNote;
import com.jerry.nsis.entity.Office;
import com.jerry.nsis.entity.Order;
import com.jerry.nsis.entity.Patient;
import com.jerry.nsis.entity.PromptBed;
import com.jerry.nsis.entity.Schedule;
import com.jerry.nsis.entity.SortModel;
import com.jerry.nsis.entity.Temper;
import com.jerry.nsis.entity.TemperDetail;
import com.jerry.nsis.entity.VideoUrl;
import com.jerry.nsis.entity.WeekWork;
import com.jerry.nsis.entity.Work;
import com.jerry.nsis.entity.WorkTitle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jerry on 2016/3/2.
 */
public class JsonUtil {

    /**
     * 将json字符串解析为护理项目集合
     *
     * @param json
     * @return
     */
    public static List<Nursing> getNursingItemFromJson(String json) {
        json = filterJson(json);
        List<Nursing> nursingList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Nursing item = new Nursing();
                JSONObject o = data.getJSONObject(i);
                item.setOfficeId(o.getString("OfficeID"));
                item.setItemId(o.getString("ItemID"));
                item.setItemName(o.getString("ItemName"));
                item.setItemType(o.getString("ItemType"));
                item.setDisplayOrder(Integer.parseInt(o.getString("DisplayOrder")));
                item.setImportantShow(Boolean.parseBoolean(o.getString("IsImportantShow")));
                item.setTitleColor(Integer.parseInt(o.getString("TitleColor")));
                item.setContentColor(Integer.parseInt(o.getString("ContentColor")));
                if (TextUtils.isEmpty(o.getString("TitleColorStr"))) {
                    item.setTitleColorString("0000ff");
                } else {
                    item.setTitleColorString(o.getString("TitleColorStr"));
                }
                if (TextUtils.isEmpty(o.getString("ContentColorStr"))) {
                    item.setContentColorString("0000FF");
                } else {
                    item.setContentColorString(o.getString("ContentColorStr"));
                }
                item.setShowPatientName(Integer.parseInt(o.getString("IsShowPatentName")));
                JSONArray frequencyArray = new JSONArray(o.getString("PeriodItems"));
                List<Frequency> frequencyList = new ArrayList<>();
                for (int j = 0; j < frequencyArray.length(); j++) {
                    Frequency frequency = new Frequency();
                    JSONObject f = frequencyArray.getJSONObject(j);
                    frequency.setFrequencyId(f.getString("PeriodID"));
                    frequency.setItemId(f.getString("ItemID"));
                    frequency.setDisplayOrder(Integer.parseInt(f.getString("DisplayOrder")));
                    frequency.setShowPatientName(Integer.parseInt(f.getString("IsShowPatientName")));
                    frequency.setFrequencyName(f.getString("PeriodName"));
                    frequency.setExeTimes(Integer.parseInt(f.getString("PerfromTimes")));
                    frequency.setMaxLine(Integer.parseInt(f.getString("MaxLine")));
                    frequencyList.add(frequency);
                }
                item.setFrequencyList(frequencyList);
                nursingList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nursingList;
    }

    /**
     * 将json字符串解析为护理明细集合
     *
     * @param json
     * @return
     */
    public static List<NursingDetail> getNursingDetailFromJson(String json) {
        json = filterJson(json);
        List<NursingDetail> nursingDetailList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                NursingDetail item = new NursingDetail();
                JSONObject o = data.getJSONObject(i);
                item.setDetailId(o.getString("DetailID"));
                item.setOfficeId(o.getString("OfficeID"));
                item.setFrequencyId(o.getString("PeriodID"));
                item.setBedId(o.getString("BedID"));
                item.setContent(o.getString("ItemContent"));
                item.setExeTimes(Integer.parseInt(o.getString("PerfromTimes")));
                item.setExedTimes(Integer.parseInt(o.getString("PerformedTimes")));
                item.setItemId(o.getString("ItemID"));
                item.setItemType(o.getString("ItemType"));
                item.setHosId(o.getString("InhosId"));
                item.setInTimes(Integer.parseInt(o.getString("InhosTimes")));
                nursingDetailList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nursingDetailList;
    }

    /**
     * 将json字符串解析为视频集合
     *
     * @param json
     * @return
     */
    public static List<Education> getEducationFromJson(String json) {
        json = filterJson(json);
        List<Education> videoList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Education item = new Education();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("EducationID"));
                item.setTitle(o.getString("Title"));
                if (!o.getString("ImageString").equals("null")) {
                    item.setImg(o.getString("ImageString"));
                }
                item.setOfficeId(o.getString("OfficeID"));
                item.setType(o.getString("Classification"));
                item.setRead(Integer.parseInt(o.getString("ReadNumber")));
                item.setPositive(Integer.parseInt(o.getString("PraiseNumber")));
                item.setNegative(Integer.parseInt(o.getString("BadReviewNumber")));
                item.setOperatorId(o.getString("OperatorID"));
                item.setTime(o.getString("OperatorTime"));
                item.setRecommend(Boolean.parseBoolean(o.getString("IsRecommend")));
                item.setName(o.getString("FileName"));
                item.setContent(o.getString("EducationContent"));
                item.setDesc(o.getString("TextContent"));
                item.setEduType(o.getString("EducationType"));
                item.setFileType(o.getString("FileType"));
                item.setUrl(o.getString("Url"));
                videoList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoList;
    }

    /**
     * 将json字符串解析为班次集合
     *
     * @param json
     * @return
     */
    public static List<Schedule> getScheduleFromJson(String json) {
        json = filterJson(json);
        List<Schedule> scheduleList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Schedule item = new Schedule();
                JSONObject o = data.getJSONObject(i);
                item.setScheduleId(o.getString("ScheTypeID"));
                item.setScheduleName(o.getString("ScheName"));
                item.setTypeName(o.getString("TypeName"));
                item.setColor(o.getString("ScheColorStr"));
                scheduleList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return scheduleList;
    }


    /**
     * 将json字符串解析为护士集合
     *
     * @param json
     * @return
     */
    public static List<Nurse> getNurseFromJson(String json) {
        json = filterJson(json);
        List<Nurse> nurseList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Nurse item = new Nurse();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("UserID"));
                item.setName(o.getString("Name"));
                item.setSex(o.getString("Sex"));
                String head = o.getString("Thumbnail");
                if (!head.equals("null")) {
                    item.setThumbnail(head);
                }
                item.setBirthday(o.getString("Birthday"));
                item.setPosition(o.getString("Position"));
                nurseList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nurseList;
    }

    /**
     * 将json字符串解析为管床集合
     *
     * @param json
     * @return
     */
    public static List<Patient> getChargePatientListFromJson(String json) {
        json = filterJson(json);
        List<Patient> patientList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject o = data.getJSONObject(i);
                String pString = o.getString("PatientData");
                if (!pString.equals("null")) {
                    JSONObject p = new JSONObject(pString);
                    Patient patient = new Patient();
                    patient.setHosId(p.getString("InhosID"));
                    patient.setInTimes(Integer.parseInt(p.getString("InHosTimes")));
                    patient.setBedNo(p.getString("BedNo"));
                    patient.setOfficeId(p.getString("OfficeID"));
                    patient.setName(p.getString("Name"));
                    patient.setSex(p.getString("Sex"));
                    patient.setBirthday(p.getString("Birthday"));
                    patient.setNation(p.getString("Nation"));
                    patient.setLevel(p.getString("NurseLevel"));
                    patient.setDiagnosis(p.getString("Diagnosis"));
                    patient.setInDate(p.getString("InHosDate"));
                    patient.setDoc(p.getString("Doctor"));
                    patient.setNurse(p.getString("Nurse"));
                    patient.setState(p.getString("IllnessState"));
                    patient.setOutDate(p.getString("OutHosDate"));
                    patient.setInState(Integer.parseInt(p.getString("InHosState")));
                    patientList.add(patient);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patientList;
    }


    /**
     * 将json字符串解析为文件内容
     *
     * @param json
     * @return
     */
    public static String getFileContentFromJson(String json) {
        json = filterJson(json);
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");
            String str = data.getString("EducationContent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 将json字符串解析为排班信息
     *
     * @param json
     * @return
     */
    public static List<Work> getWorkFromJson(String json) {
        json = filterJson(json);
        List<Work> workList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Work item = new Work();
                JSONObject o = data.getJSONObject(i);
                item.setScheduleId(o.getString("ScheTypeID"));
                item.setUserId(o.getString("UserID"));
                item.setUserType(o.getString("Type"));
                workList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workList;
    }


    /**
     * 将json字符串解析为责任分组
     *
     * @param json
     * @return
     */
    public static List<DutyGroup> getDutyGroupFromJson(String json) {
        json = filterJson(json);
        List<DutyGroup> groupList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                DutyGroup item = new DutyGroup();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("ResponsibilityID"));
                item.setName(o.getString("GroupName"));
                item.setDesc(o.getString("BedNoDesc"));
                item.setOrder(o.getString("OrderIndex"));

                String bedInfo = o.getString("BedInfos");
                if (!bedInfo.equals("null")) {
                    List<String> bedList = new ArrayList<>();
                    JSONArray beds = new JSONArray(bedInfo);
                    for (int j = 0; j < beds.length(); j++) {
                        JSONObject b = beds.getJSONObject(j);
                        String no = b.getString("BedNo");
                        bedList.add(no);
                    }
                    item.setNoList(bedList);
                }
                groupList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groupList;
    }


    /**
     * 将json字符串解析为护士一周排班信息
     *
     * @param json
     * @return
     */
    public static List<NurseWork> getNurseWorkFromJson(String json) {
        json = filterJson(json);
        List<NurseWork> workList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                NurseWork item = new NurseWork();
                JSONObject o = data.getJSONObject(i);
                item.setDate(o.getString("AppDate"));
                item.setContent(o.getString("ScheName"));
                workList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workList;
    }


    /**
     * 将json字符串解析为视频地址
     *
     * @param json
     * @return
     */
    public static VideoUrl getVideoUrlFromJson(String json) {
        json = filterJson(json);
        VideoUrl item = null;
        try {
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");
            item = new VideoUrl();
            item.setId(data.getString("System_ConfigID"));
            item.setName(data.getString("System_ConfigName"));
            item.setUrl(data.getString("System_ConfigValue"));
            item.setModule(data.getString("System_Module"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 将json字符串解析为床位集合
     *
     * @param json
     * @return
     */
    public static List<Bed> getBedInfolFromJson(String json) {
        json = filterJson(json);
        List<Bed> bedList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Bed item = new Bed();
                JSONObject o = data.getJSONObject(i);
                item.setOfficeId(o.getString("OfficeID"));
                item.setHisBedNo(o.getString("HISBedNo"));
                item.setAdd(Boolean.parseBoolean(o.getString("BedAdded")));
                item.setNo(o.getString("BedNo"));
                String s = o.getString("PatientData");
                if (!s.equals("null")) {
                    JSONObject p = new JSONObject(s);
                    Patient patient = new Patient();
                    patient.setHosId(p.getString("InhosID"));
                    patient.setInTimes(Integer.parseInt(p.getString("InHosTimes")));
                    patient.setBedNo(p.getString("BedNo"));
                    patient.setOfficeId(p.getString("OfficeID"));
                    patient.setName(p.getString("Name"));
                    patient.setSex(p.getString("Sex"));
                    patient.setBirthday(p.getString("Birthday"));
                    patient.setNation(p.getString("Nation"));
                    patient.setLevel(p.getString("NurseLevel"));
                    patient.setDiagnosis(p.getString("Diagnosis"));
                    patient.setInDate(p.getString("InHosDate"));
                    patient.setDoc(p.getString("Doctor"));
                    patient.setNurse(p.getString("Nurse"));
                    patient.setState(p.getString("IllnessState"));
                    patient.setOutDate(p.getString("OutHosDate"));
                    patient.setInState(Integer.parseInt(p.getString("InHosState")));
                    patient.setEvent(p.getString("EventState"));

                    item.setPatient(patient);
                } else {
                    item.setPatient(null);
                }
                bedList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bedList;
    }


    /**
     * 将json字符串解析为管床集合
     *
     * @param json
     * @return
     */
    public static List<Bed> getChargeBedlFromJson(String json) {
        json = filterJson(json);
        List<Bed> bedList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Bed item = new Bed();
                JSONObject o = data.getJSONObject(i);
                item.setOfficeId(o.getString("OfficeID"));
                item.setHisBedNo(o.getString("HISBedNo"));
                item.setAdd(Boolean.parseBoolean(o.getString("BedAdded")));
                item.setNo(o.getString("BedNo"));
                String s = o.getString("PatientData");
                if (!s.equals("null")) {
                    JSONObject p = new JSONObject(s);
                    Patient patient = new Patient();
                    patient.setHosId(p.getString("InhosID"));
                    patient.setInTimes(Integer.parseInt(p.getString("InHosTimes")));
                    patient.setBedNo(p.getString("BedNo"));
                    patient.setOfficeId(p.getString("OfficeID"));
                    patient.setName(p.getString("Name"));
                    patient.setSex(p.getString("Sex"));
                    patient.setBirthday(p.getString("Birthday"));
                    patient.setNation(p.getString("Nation"));
                    patient.setLevel(p.getString("NurseLevel"));
                    patient.setDiagnosis(p.getString("Diagnosis"));
                    patient.setInDate(p.getString("InHosDate"));
                    patient.setDoc(p.getString("Doctor"));
                    patient.setNurse(p.getString("Nurse"));
                    patient.setState(p.getString("IllnessState"));
                    patient.setOutDate(p.getString("OutHosDate"));
                    patient.setInState(Integer.parseInt(p.getString("InHosState")));
                    patient.setEvent(p.getString("EventState"));
                    item.setPatient(patient);
                } else {
                    item.setPatient(null);
                }
                bedList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bedList;
    }

    /**
     * 将json字符串解析为视频集合
     *
     * @param json
     * @return
     */
    public static List<Duty> getOndutyFromJson(String json) {
        json = filterJson(json);
        List<Duty> dutyList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Duty item = new Duty();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("DocscheId"));
                item.setName(o.getString("ScheName"));
                item.setOrder(Integer.parseInt(o.getString("Reorder")));
                item.setColor(o.getString("ScheColor"));
                item.setOfficeId(o.getString("OfficeId"));
                item.setOrention(Integer.parseInt(o.getString("Broadwise")));
                item.setContent(o.getString("ScheValue"));
                dutyList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dutyList;
    }

    /**
     * 将json字符串解析为一周排班标题
     *
     * @param json
     * @return
     */
    public static List<WorkTitle> getWeekTitleFromJson(String json) {
        json = filterJson(json);
        List<WorkTitle> titleList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                WorkTitle item = new WorkTitle();
                JSONObject o = data.getJSONObject(i);
                item.setIndex(o.getString("编号"));
                item.setTitle(o.getString("列名"));
                titleList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return titleList;
    }


    /**
     * 将json字符串解析为科室
     *
     * @param json
     * @return
     */
    public static List<Office> getOfficeFromJson(String json) {
        json = filterJson(json);
        List<Office> officeList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Office item = new Office();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("OFFICEID"));
                item.setName(o.getString("OFFICENAME"));
                item.setSpell(o.getString("SPELLNO"));
                item.setHisId(o.getString("HISID"));
                officeList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return officeList;
    }


    /**
     * 将json字符串解析为科室
     *
     * @param json
     * @return
     */
    public static String getQrCodeFromJson(String json) {
        json = filterJson(json);
        String qrCode = null;
        try {
            JSONObject object = new JSONObject(json);
            qrCode = object.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return qrCode;
    }

    /**
     * 将json字符串解析为是否注册成功
     *
     * @param json
     * @return
     */
    public static String getRegisterInfoJson(String json) {
        json = filterJson(json);
        String result = null;
        try {
            JSONObject object = new JSONObject(json);
            result = object.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 将json字符串解析为医院信息
     *
     * @param json
     * @return
     */
    public static String getHospitalName(String json) {
        json = filterJson(json);
        String name = null;
        try {
            JSONObject object = new JSONObject(json);
            name = object.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }


    /**
     * 将json字符串解析为一周排班备注
     *
     * @param json
     * @return
     */
    public static String getWeekNoteFromJson(String json) {
        json = filterJson(json);
        String note = "";
        try {
            JSONObject object = new JSONObject(json);
            note = object.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return note;
    }

    /**
     * 将json字符串解析为一周排班
     *
     * @param json
     * @return
     */
    public static List<WeekWork> getWeekWorkFromJson(String json) {
        json = filterJson(json);
        List<WeekWork> workList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                WeekWork item = new WeekWork();
                JSONObject o = data.getJSONObject(i);

                item.setBigGroup(o.getString("1"));
                item.setSmallGroup(o.getString("2"));

                item.setPro1(o.getString("3"));
                item.setPro2(o.getString("4"));
                item.setPro3(o.getString("5"));
                item.setPro4(o.getString("6"));
                item.setMonday(o.getString("7"));
                item.setTuesday(o.getString("8"));
                item.setWednesday(o.getString("9"));
                item.setThursday(o.getString("10"));
                item.setFriday(o.getString("11"));
                item.setSaturday(o.getString("12"));
                item.setSunday(o.getString("13"));
                if (o.getString("14") != null) {
                    item.setWeek(o.getString("14"));
                }
                if (o.getString("15") != null) {
                    item.setTotal(o.getString("15"));
                }
                workList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return workList;
    }

    /**
     * 将json字符串解析为体温集合
     *
     * @param json
     * @return
     */
    public static List<Temper> getTemperFromJson(String json) {
        json = filterJson(json);
        List<Temper> temperList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Temper item = new Temper();
                JSONObject o = data.getJSONObject(i);
                item.setOfficeId(o.getString("OfficeID"));
                item.setFrequencyId(o.getString("ConditionID"));
                item.setItemId(o.getString("ItemID"));
                item.setFrequency(o.getString("ItemSetName"));
                item.setOrder(Integer.parseInt(o.getString("DisplayOrder")));
                item.setPriority(Integer.parseInt(o.getString("Priority")));
                item.setMaxLine(Integer.parseInt(o.getString("MaxLine")));
                item.setTextSize(o.getString("ItemSize"));
                item.setShowPatientName(Integer.parseInt(o.getString("IsShowPatientName")));
                temperList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temperList;
    }

    /**
     * 将json字符串解析为体温详细集合
     *
     * @param json
     * @return
     */
    public static List<TemperDetail> getTemperDetailFromJson(String json) {
        json = filterJson(json);
        List<TemperDetail> temperDetailList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                TemperDetail item = new TemperDetail();
                JSONObject o = data.getJSONObject(i);
                item.setHosId(o.getString("InhosID"));
                item.setInTimes(Integer.parseInt(o.getString("InhosTimes")));
                item.setEvent(o.getString("EventStatus"));
                item.setFrequencyId(o.getString("ConditionID"));
                item.setOfficeId(o.getString("OfficeID"));
                item.setValue(Float.parseFloat(o.getString("SignsValue")));
                item.setSigns(o.getString("SignsName"));
                item.setNurse(o.getString("NurseName"));
                item.setTime(o.getString("StartTime"));
                temperDetailList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temperDetailList;
    }

    /**
     * 将json字符串解析为患者信息集合
     *
     * @param json
     * @return
     */
    public static List<Patient> getPatientFromJson(String json) {
        json = filterJson(json);
        List<Patient> patientList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Patient item = new Patient();
                JSONObject o = data.getJSONObject(i);
                item.setHosId(o.getString("InhosID"));
                item.setInTimes(Integer.parseInt(o.getString("InHosTimes")));
                item.setBedNo(o.getString("BedNo"));
                item.setOfficeId(o.getString("OfficeID"));
                item.setName(o.getString("Name"));
                item.setSex(o.getString("Sex"));
                item.setBirthday(o.getString("Birthday"));
                item.setNation(o.getString("Nation"));
                item.setLevel(o.getString("NurseLevel"));
                item.setDiagnosis(o.getString("Diagnosis"));
                item.setInDate(o.getString("InHosDate"));
                item.setDoc(o.getString("Doctor"));
                item.setNurse(o.getString("Nurse"));
                item.setState(o.getString("IllnessState"));
                item.setOutDate(o.getString("OutHosDate"));
                item.setInState(Integer.parseInt(o.getString("InHosState")));
                patientList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patientList;
    }



    /**
     * 将json字符串解析为出院患者信息集合
     *
     * @param json
     * @return
     */
    public static Patient getOutPatientFromJson(String json) {
        json = filterJson(json);
        Patient item = new Patient();
        try {
            JSONObject object = new JSONObject(json);
            String data = object.getString("data");
            JSONObject o = new JSONObject(data);

                item.setHosId(o.getString("InhosID"));
                item.setInTimes(Integer.parseInt(o.getString("InHosTimes")));
                item.setBedNo(o.getString("BedNo"));
                item.setOfficeId(o.getString("OfficeID"));
                item.setName(o.getString("Name"));
                item.setSex(o.getString("Sex"));
                item.setBirthday(o.getString("Birthday"));
                item.setNation(o.getString("Nation"));
                item.setLevel(o.getString("NurseLevel"));
                item.setDiagnosis(o.getString("Diagnosis"));
                item.setInDate(o.getString("InHosDate"));
                item.setDoc(o.getString("Doctor"));
                item.setNurse(o.getString("Nurse"));
                item.setState(o.getString("IllnessState"));
                item.setOutDate(o.getString("OutHosDate"));
                item.setInState(Integer.parseInt(o.getString("InHosState")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }




    /**
     * 将json字符串解析为医生集合
     *
     * @param json
     * @return
     */
    public static List<Doctor> getDoctorFromJson(String json) {
        json = filterJson(json);
        List<Doctor> docList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Doctor item = new Doctor();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("DoctorID"));
                item.setOfficeId(o.getString("OfficeID"));
                item.setHisId(o.getString("HISID"));
                item.setName(o.getString("Name"));
                item.setSex(o.getString("Sex"));
                docList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return docList;
    }

    /**
     * 将json字符串解析为备注信息集合
     *
     * @param json
     * @return
     */
    public static List<NursingNote> getNursingNoteFromJson(String json) {
        json = filterJson(json);
        List<NursingNote> nursingNoteList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                NursingNote item = new NursingNote();
                JSONObject o = data.getJSONObject(i);
                item.setRecordId(o.getString("RecordDetailID"));
                item.setId(o.getString("DetailId"));
                item.setContent(o.getString("DetailContent"));
                item.setValue(o.getString("DetailValue"));
                item.setOrder(Integer.parseInt(o.getString("OrderIndex")));
                nursingNoteList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nursingNoteList;
    }

    /**
     * 将json字符串解析为换床信息集合
     *
     * @param json
     * @return
     */
    public static List<Exchange> getExchangeFromJson(String json) {
        json = filterJson(json);
        List<Exchange> exchangeList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Exchange item = new Exchange();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("TranID"));
                item.setOfficeId(o.getString("OfficeID"));
                item.setOldBed(o.getString("OldBedID"));
                item.setNewBed(o.getString("NewBedID"));
                item.setDate(o.getString("TranDate"));
                item.setName(o.getString("PatientName"));
                item.setHosId(o.getString("InhosID"));
                item.setInTimes(Integer.parseInt(o.getString("InHosTimes")));
                item.setOldOfficeId(o.getString("OldOfficeID"));
                exchangeList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return exchangeList;
    }

    /**
     * 将json字符串解析为联系人集合
     *
     * @param json
     * @return
     */
    public static List<SortModel> getSortModelFromJson(String json) {
        json = filterJson(json);
        List<SortModel> contactList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                SortModel item = new SortModel();

                JSONObject o = data.getJSONObject(i);
                item.setName(o.getString("Name"));
                item.setPhone(o.getString("Phone"));

                contactList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    /**
     * 将json字符串解析为便签集合
     *
     * @param json
     * @return
     */
    public static List<Note> getNoteFromJson(String json) {
        json = filterJson(json);
        List<Note> noteList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Note item = new Note();

                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("MemoID"));
                item.setContent(o.getString("MemoContent"));
                item.setOfficeId(o.getString("OfficeID"));
                item.setTime(o.getString("MemoDate"));
                item.setSize(o.getString("FontSize"));

                noteList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noteList;
    }

    /**
     * 将json字符串解析为换床床位信息集合
     *
     * @param json
     * @return
     */
    public static List<ExchangeBed> getExchangeBedFromJson(String json) {
        json = filterJson(json);
        List<ExchangeBed> bedList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                ExchangeBed item = new ExchangeBed();
                JSONObject o = data.getJSONObject(i);
                item.setOfficeId(o.getString("OfficeID"));
                item.setHisBedNo(o.getString("HISBedNo"));
                item.setBedNo(o.getString("BedNo"));
                item.setStatus(Integer.parseInt(o.getString("BedStatus")));
                bedList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bedList;
    }


    /**
     * 将json字符串解析为花费信息
     *
     * @param json
     * @return
     */
    public static Cost getCostFromJson(String json) {
        json = filterJson(json);
        Cost item = new Cost();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject o = object.getJSONObject("data");
            item.setPre(Float.parseFloat(o.getString("Advance_Money")));
            item.setAll(Float.parseFloat(o.getString("Total_Consume_Money")));
            item.setRemain(Float.parseFloat(o.getString("Balance_Money")));
            item.setType(o.getString("Medical_Insurance"));
            item.setPhone(o.getString("Contact_Pone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 将json字符串解析为PromptBed
     *
     * @param json
     * @return
     */
    public static PromptBed getPbFromJson(String json) {
        json = filterJson(json);
        PromptBed item = new PromptBed();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject o = object.getJSONObject("data");
            item.setDetailId(o.getString("DetailID"));
            item.setInTimes(Integer.parseInt(o.getString("InhosTimes")));
            item.setBed(o.getString("BedID"));
            item.setHosId(o.getString("InhosId"));
            item.setName(o.getString("ItemContent"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }


    /**
     * 获取返回数据
     * @param json
     * @return
     */
    public static String getResultFromJson(String json) {
        json = filterJson(json);
        String result = "";
        try {
            JSONObject object = new JSONObject(json);
            result= object.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 将json字符串解析为医嘱信息
     *
     * @param json
     * @return
     */
    public static List<Cost> getCostListFromJson(String json) {
        json = filterJson(json);
        List<Cost> costList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Cost item = new Cost();
                JSONObject o = data.getJSONObject(i);
                item.setHosId(o.getString("InhosID"));
                item.setInTimes(Integer.parseInt(o.getString("InHosTimes")));
                item.setPre(Float.parseFloat(o.getString("Advance_Money")));
                item.setAll(Float.parseFloat(o.getString("Total_Consume_Money")));
                item.setRemain(Float.parseFloat(o.getString("Balance_Money")));
                if (!o.getString("Medical_Insurance").equals("null")) {
                    item.setType(o.getString("Medical_Insurance"));
                } else {
                    item.setType(null);
                }
                item.setPhone(o.getString("Contact_Pone"));
                costList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return costList;
    }

    /**
     * 将json字符串解析为医嘱信息
     *
     * @param json
     * @return
     */
    public static List<Order> getOrderListFromJson(String json) {
        json = filterJson(json);
        List<Order> orderList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Order item = new Order();
                JSONObject o = data.getJSONObject(i);
                item.setStartTime(o.getString("START_DATE_TIME"));
                item.setOrderType(o.getString("REPEAT_INDICATOR"));
                item.setType(o.getString("REPEAT_INDICATORTYPE"));
                item.setContent(o.getString("ORDER_TEXT"));
                item.setWay(o.getString("ADMINISARATION"));
                item.setFrequency(o.getString("FREQUENCY"));
                item.setDose(o.getString("DOSAGE"));
                item.setUnit(o.getString("DOSAGE_UNITS"));
                item.setNote(o.getString("NOTE"));
                item.setEnsureTime(o.getString("MODIFYDATETIME"));
                item.setStatus(Integer.parseInt(o.getString("ORDER_STATUS")));
                orderList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderList;
    }


    /**
     * 将json字符串解析为通知集合
     *
     * @param json
     * @return
     */
    public static List<Notice> getNoticeFromJson(String json) {
        json = filterJson(json);
        List<Notice> noticeList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Notice item = new Notice();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("NoticeID"));
                item.setTitle(o.getString("NoticeTitle"));
                item.setContent(o.getString("NoticeContent"));
                item.setType(o.getString("NoticeType"));
                item.setLast(Integer.parseInt(o.getString("NoticeStopTime")));
                item.setFileName(o.getString("NoticeFileName"));
                item.setStartTime(o.getString("NoticeStaTime"));
                item.setEndTime(o.getString("NoticeEndTime"));
                item.setOffice(o.getString("OfficeName"));
                item.setMd5(o.getString("MD5"));
                item.setUrl(o.getString("Url"));
                noticeList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noticeList;
    }


    public static String filterJson(String json) {
        String result = "";
        if (json.length() > 0) {
            if (json.startsWith("\"")) {
                result = json.substring(1, json.length() - 1);
            }
            result = result.replace("\\", "");
        }
        return result;
    }


    public static boolean isEmpty(List list) {
        return (list == null || list.size() == 0);
    }

    public static boolean isEmpty(Map map) {
        return (map == null || map.size() == 0);
    }


}
