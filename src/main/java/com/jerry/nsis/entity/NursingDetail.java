package com.jerry.nsis.entity;

import java.io.Serializable;

/**
 * Created by Jerry on 2016/3/3.
 *  护理项目明细
 */
public class NursingDetail implements Serializable {
    /**
     * 科室编号
     */
    private String officeId;
    /**
     * 护理项目明细编号
     */
    private String detailId;
    /**
     * 频率编号
     */
    private String frequencyId;
    /**
     * 床号
     */
    private String bedId;
    /**
     * 内容
     */
    private String content;
    /**
     * 执行次数
     */
    private int exeTimes;
    /**
     * 已执行次数
     */
    private int exedTimes;
    /**
     * 护理项目编号
     */
    private String itemId;
    /**
     * 护理项目类型
     */
    private String itemType;
    /**
     * 住院号
     */
    private String hosId;
    /**
     * 住院次数
     */
    private int inTimes;


    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getExeTimes() {
        return exeTimes;
    }

    public void setExeTimes(int exeTimes) {
        this.exeTimes = exeTimes;
    }

    public int getExedTimes() {
        return exedTimes;
    }

    public void setExedTimes(int exedTimes) {
        this.exedTimes = exedTimes;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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
