package com.jerry.nsis.entity;

import java.io.Serializable;

/**
 * Created by Jerry on 2016/3/7.
 */
public class Bed implements Serializable {
    /**
     * 科室编号
     */
    private String officeId;
    /**
     * HIS床位编号
     */
    private String hisBedNo;
    /**
     * 床号
     */
    private String no;
    /**
     * 是否加床？啥意思
     */
    private boolean add;

    /**
     * 是否选中
     */
    private boolean selected;
    /**
     * 病人信息
     */
    private Patient patient = new Patient();

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getHisBedNo() {
        return hisBedNo;
    }

    public void setHisBedNo(String hisBedNo) {
        this.hisBedNo = hisBedNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }



    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Bed{" +
                "officeId='" + officeId + '\'' +
                ", hisBedNo='" + hisBedNo + '\'' +
                ", no='" + no + '\'' +
                ", add=" + add +
                ", selected=" + selected +
                ", patient=" + patient +
                '}';
    }
}


