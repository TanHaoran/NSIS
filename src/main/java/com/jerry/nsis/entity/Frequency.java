package com.jerry.nsis.entity;

import java.io.Serializable;

/**
 * Created by Jerry on 2016/3/2.
 * 频率信息
 */
public class Frequency implements Serializable{
    /**
     * 频率编号
     */
    private String frequencyId;
    /**
     * 护理项目编号
     */
    private String itemId;
    /**
     * 显示顺序
     */
    private int displayOrder;
    /**
     * 是否显示病人姓名：0，否；1，是
     */
    private int showPatientName;
    /**
     * 内容颜色
     */
    private int contentColor;
    /**
     * 频率名称
     */
    private String frequencyName;
    /**
     * 执行次数
     */
    private int exeTimes;
    /**
     * 最大行数
     */
    private int maxLine;

    public String getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getShowPatientName() {
        return showPatientName;
    }

    public void setShowPatientName(int showPatientName) {
        this.showPatientName = showPatientName;
    }

    public int getContentColor() {
        return contentColor;
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }

    public String getFrequencyName() {
        return frequencyName;
    }

    public void setFrequencyName(String frequencyName) {
        this.frequencyName = frequencyName;
    }

    public int getExeTimes() {
        return exeTimes;
    }

    public void setExeTimes(int exeTimes) {
        this.exeTimes = exeTimes;
    }

    public int getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }
}
