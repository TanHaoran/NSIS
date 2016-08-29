package com.jerry.nsis.entity;

/**
 * 护理项目
 * Created by Jerry on 2016/2/25.
 */
public class Print {

    /**
     * 科室名称
     */
    private String office;
    /**
     * 护理项目数量
     */
    private int num;
    /**
     * 是否选中
     */
    private boolean check;

    public Print() {

    }

    public Print(String office, int num, boolean check) {
        this.office = office;
        this.num = num;
        this.check = check;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
