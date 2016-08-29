package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/3/9.
 */
public class Exchange {
    private String id;
    private String officeId;
    private String oldBed;
    private String  newBed;
    private String date;
    private String name;
    private String hosId;
    private int inTimes;
    private String oldOfficeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOldBed() {
        return oldBed;
    }

    public void setOldBed(String oldBed) {
        this.oldBed = oldBed;
    }

    public String getNewBed() {
        return newBed;
    }

    public void setNewBed(String newBed) {
        this.newBed = newBed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String time) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOldOfficeId() {
        return oldOfficeId;
    }

    public void setOldOfficeId(String oldOfficeId) {
        this.oldOfficeId = oldOfficeId;
    }
}
