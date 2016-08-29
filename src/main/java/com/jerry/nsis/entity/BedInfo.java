package com.jerry.nsis.entity;

import java.io.Serializable;

/**
 * Created by Jerry on 2016/3/9.
 */
public class BedInfo implements Serializable {

    private String no;
    private String content;

    private String hosId;
    private int inTimes;
    private Nursing nursing;
    private NursingDetail nursingDetail;

    private String event;

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Nursing getNursing() {
        return nursing;
    }

    public void setNursing(Nursing nursing) {
        this.nursing = nursing;
    }

    public NursingDetail getNursingDetail() {
        return nursingDetail;
    }

    public void setNursingDetail(NursingDetail nursingDetail) {
        this.nursingDetail = nursingDetail;
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "BedInfo{" +
                "no='" + no + '\'' +
                ", content='" + content + '\'' +
                ", hosId='" + hosId + '\'' +
                ", inTimes=" + inTimes +
                ", nursing=" + nursing +
                ", nursingDetail=" + nursingDetail +
                ", event='" + event + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
