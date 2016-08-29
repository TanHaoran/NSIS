package com.jerry.nsis.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jerry on 2016/3/31.
 */
public class DutyGroup implements Serializable{

    private String id;
    private String name;
    private String desc;
    private String order;
    private List<String> noList;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public List<String> getNoList() {
        return noList;
    }

    public void setNoList(List<String> noList) {
        this.noList = noList;
    }


    @Override
    public String toString() {
        return "DutyGroup{" +
                "noList=" + noList +
                ", order='" + order + '\'' +
                ", desc='" + desc + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
