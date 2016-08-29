package com.jerry.nsis.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/3/11.
 */
public class Prompt implements Serializable {

    private String itemId;
    private String color;
    private String title;
    private String frequencyId;
    private List<PromptBed> bedList = new ArrayList<>();

    private Nursing nursing;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PromptBed> getBedList() {
        return bedList;
    }

    public void setBedList(List<PromptBed> bedList) {
        this.bedList = bedList;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(String frequencyId) {
        this.frequencyId = frequencyId;
    }

    public Nursing getNursing() {
        return nursing;
    }

    public void setNursing(Nursing nursing) {
        this.nursing = nursing;
    }


    @Override
    public String toString() {
        return "Prompt{" +
                "itemId='" + itemId + '\'' +
                ", color='" + color + '\'' +
                ", title='" + title + '\'' +
                ", frequencyId='" + frequencyId + '\'' +
                ", bedList=" + bedList +
                ", nursing=" + nursing +
                '}';
    }
}
