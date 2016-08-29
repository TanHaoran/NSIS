package com.jerry.nsis.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jerry on 2016/3/2.
 *  护理项目
 */
public class Nursing implements Serializable{
    /**
     * 科室编号
     */
    private String officeId;
    /**
     * 护理项目编号
     */
    private String itemId;
    /**
     * 护理项目名称
     */
    private String itemName;
    /**
     * 护理项目类型（医嘱提示、自定义提示、消息提示、体温提示）
     */
    private String itemType;
    /**
     * 显示顺序
     */
    private int displayOrder;
    /**
     * 是否重点护理项目
     */
    private boolean importantShow;
    /**
     * 标题颜色
     */
    private int titleColor;
    /**
     * 内容颜色
     */
    private int contentColor;
    /**
     * 标题颜色（字符串型）
     */
    private String titleColorString;
    /**
     * 内容颜色（字符串型）
     */
    private String contentColorString;

    /**
     * 是否显示病人信息（如果是0，就读取到什么显示什么；如果是1，就通过住院号和住院次数查到病人的姓名来显示）
     */
    private int showPatientName;

    /**
     * 执行频率数组
     */
    private List<Frequency> frequencyList;

    public int getShowPatientName() {
        return showPatientName;
    }

    public void setShowPatientName(int showPatientName) {
        this.showPatientName = showPatientName;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isImportantShow() {
        return importantShow;
    }

    public void setImportantShow(boolean importantShow) {
        this.importantShow = importantShow;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public int getContentColor() {
        return contentColor;
    }

    public void setContentColor(int contentColor) {
        this.contentColor = contentColor;
    }

    public List<Frequency> getFrequencyList() {
        return frequencyList;
    }

    public String getTitleColorString() {
        return titleColorString;
    }

    public void setTitleColorString(String titleColorString) {
        this.titleColorString = titleColorString;
    }

    public String getContentColorString() {
        return contentColorString;
    }

    public void setContentColorString(String contentColorString) {
        this.contentColorString = contentColorString;
    }

    public void setFrequencyList(List<Frequency> frequencyList) {
        this.frequencyList = frequencyList;
    }


    @Override
    public String toString() {
        return "Nursing{" +
                "officeId='" + officeId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", displayOrder=" + displayOrder +
                ", importantShow=" + importantShow +
                ", titleColor=" + titleColor +
                ", contentColor=" + contentColor +
                ", titleColorString='" + titleColorString + '\'' +
                ", contentColorString='" + contentColorString + '\'' +
                ", showPatientName=" + showPatientName +
                ", frequencyList=" + frequencyList +
                '}';
    }
}
