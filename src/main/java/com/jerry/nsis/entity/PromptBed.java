package com.jerry.nsis.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jerry on 2016/3/11.
 */
public class PromptBed implements Serializable {

    private String bed;
    private String name;
    private String hosId;
    private String sex;
    private String detailId;
    private String tag;
    private int inTimes;


    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }


    public int getInTimes() {
        return inTimes;
    }

    public void setInTimes(int inTimes) {
        this.inTimes = inTimes;
    }


    @Override
    public String toString() {
        return "PromptBed{" +
                "bed='" + bed + '\'' +
                ", name='" + name + '\'' +
                ", hosId='" + hosId + '\'' +
                ", sex='" + sex + '\'' +
                ", detailId='" + detailId + '\'' +
                ", tag='" + tag + '\'' +
                ", inTimes=" + inTimes +
                '}';
    }
}

