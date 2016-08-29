package com.jerry.nsis.utils;

import com.jerry.nsis.entity.SortModel;

import java.util.Comparator;

/**
 * Created by Jerry on 2016/4/5.
 */
public class PinyinComparator implements Comparator<SortModel> {

    public int compare(SortModel o1, SortModel o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.getLetter().equals("#")) {
            return -1;
        } else if (o1.getLetter().equals("#")) {
            return 1;
        } else {
            return o1.getLetter().compareTo(o2.getLetter());
        }
    }

}