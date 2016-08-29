package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/3/18.
 */
public class Cost {

    public static final String INSURANCE = "医保";
    public static final String COUNTRY = "新农合";
    public static final String CASH = "现金";

    private String hosId;
    private int inTimes;
    private String type;
    private float pre;
    private float all;
    private float remain;
    private String phone;

    public float getPre() {
        return pre;
    }

    public void setPre(float pre) {
        this.pre = pre;
    }

    public float getAll() {
        return all;
    }

    public void setAll(float all) {
        this.all = all;
    }

    public float getRemain() {
        return remain;
    }

    public void setRemain(float remain) {
        this.remain = remain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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
}
