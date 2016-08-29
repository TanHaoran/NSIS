package com.jerry.nsis.entity;

import java.io.Serializable;

/**
 * Created by Jerry on 2016/2/18.
 */
public class Patient implements Serializable {

    public static final String IN_HOS = "入院";
    public static final String EXCHANGE_IN = "转入";
    public static final String OUT = "出院";
    public static final String OPERATION = "手术";


    /**
     * 住院号
     */
    private String hosId;
    /**
     * 住院次数
     */
    private int inTimes;
    /**
     * 床号
     */
    private String bedNo;
    /**
     * 科室编号
     */
    private String officeId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 民族
     */
    private String nation;
    /**
     * 护理等级
     */
    private String level;
    /**
     * 争端
     */
    private String diagnosis;
    /**
     * 住院日期
     */
    private String inDate;
    /**
     * 主治医生
     */
    private String doc;
    /**
     * 责任护士
     */
    private String nurse;
    /**
     * 病情状况
     */
    private String state;
    /**
     * 出院日期
     */
    private String outDate;
    /**
     * 住院状态
     */
    private int inState;

    /**
     * 医保类型
     */
    private String insurance;

    /**
     * 事件
     */
    private String event;

    /**
     * 护理项目的id
     */
    private String detailId;

    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }

    public int getInTimes() {
        return inTimes;
    }

    public void setInTimes(int inTimes) {
        this.inTimes = inTimes;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public int getInState() {
        return inState;
    }

    public void setInState(int inState) {
        this.inState = inState;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }


    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
