package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/4/5.
 * 排序实体
 */
public class SortModel {

    /**
     * 显示首字母
     */
    private String letter;
    /**
     * 显示名称
     */
    private String name;
    /**
     * 显示电话
     */
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
