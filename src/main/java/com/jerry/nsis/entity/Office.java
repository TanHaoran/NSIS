package com.jerry.nsis.entity;

/**
 * Created by Jerry on 2016/4/12.
 */
public class Office {

    private String id;
    private String name;
    private String spell;
    private String hisId;

    private boolean check;

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

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public boolean isCheck() {

        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
