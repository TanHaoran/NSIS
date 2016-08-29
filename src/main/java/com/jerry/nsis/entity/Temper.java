package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/3/8.
 */
public class Temper {
    private String officeId;
    private String frequencyId;
    private String itemId;
    private String frequency;
    private int order;
    private int priority;
    private int maxLine;
    private String textSize;
    private int showPatientName;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public int getShowPatientName() {
        return showPatientName;
    }

    public void setShowPatientName(int showPatientName) {
        this.showPatientName = showPatientName;
    }

}
