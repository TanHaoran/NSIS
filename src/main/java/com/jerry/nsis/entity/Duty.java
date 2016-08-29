package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/3/7.
 */
public class Duty {
    /**
     * 医生值班编号
     */
    private String id;
    /**
     * 班次名称
     */
    private String name;
    /**
     * 显示顺序
     */
    private int order;
    /**
     * 显示颜色
     */
    private String color;
    /**
     * 科室编号
     */
    private String officeId;
    /**
     * 横竖信息
     */
    private int orention;
    /**
     * 医生内容
     */
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public int getOrention() {
        return orention;
    }

    public void setOrention(int orention) {
        this.orention = orention;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
