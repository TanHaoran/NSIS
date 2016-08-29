package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/3/14.
 */
public class Schedule {


    public static final String DAY_WORK = "白班";
    public static final String NIGHT_WORK = "晚班";
    public static final String VOCATION = "休息";

    /**
     * 班次编号
     */
    private String scheduleId;
    /**
     * 班次名称
     */
    private String ScheduleName;
    /**
     * 班次类型
     */
    private String typeName;

    /**
     * 颜色
     * @return
     */
    private String color;

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return ScheduleName;
    }

    public void setScheduleName(String scheduleName) {
        ScheduleName = scheduleName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
