package com.jerry.nsis.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Jerry on 2016/2/26.
 */
public class Education implements Serializable {
    /**
     * 教育编号
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 图片
     */
    private String img;
    /**
     * 科室编号
     */
    private String officeId;
    /**
     * 类别
     */
    private String type;
    /**
     * 阅读次数
     */
    private int read;
    /**
     * 好评次数
     */
    private int positive;
    /**
     * 差评次数
     */
    private int negative;
    /**
     * 操作员编号
     */
    private String operatorId;
    /**
     * 操作时间
     */
    private String time;
    /**
     * 是否推荐
     */
    private boolean recommend;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件内容
     */
    private String content;
    /**
     * 文件描述
     */
    private String desc;
    /**
     * 教育类别
     */
    private String eduType;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 视频地址
     */
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEduType() {
        return eduType;
    }

    public void setEduType(String eduType) {
        this.eduType = eduType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Education{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img=" + img +
                ", officeId='" + officeId + '\'' +
                ", type='" + type + '\'' +
                ", read=" + read +
                ", positive=" + positive +
                ", negative=" + negative +
                ", operatorId='" + operatorId + '\'' +
                ", time='" + time + '\'' +
                ", recommend=" + recommend +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", desc='" + desc + '\'' +
                ", eduType='" + eduType + '\'' +
                ", fileType='" + fileType + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
