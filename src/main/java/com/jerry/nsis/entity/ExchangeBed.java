package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/3/16.
 * 换床的床位信息
 */
public class ExchangeBed {

    private String officeId;
    private String hisBedNo;
    private String bedNo;
    private int status;

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

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
