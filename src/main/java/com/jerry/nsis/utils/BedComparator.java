package com.jerry.nsis.utils;

import android.text.TextUtils;

import com.jerry.nsis.entity.BedInfo;

import java.util.Comparator;

/**
 * Created by Jerry on 2016/4/29.
 */
public class BedComparator implements Comparator<BedInfo> {
    @Override
    public int compare(BedInfo lhs, BedInfo rhs) {
        String left = lhs.getNo();
        String right = rhs.getNo();
        if (TextUtils.isEmpty(left)) {
            return 1;
        }
        if (TextUtils.isEmpty(right)) {
            return -1;
        }
        int length = Math.min(left.length(), right.length());
        for (int i = 0; i < length; i++) {
            if (left.charAt(i) < right.charAt(i)) {
                return -1;
            } else if (left.charAt(i) > right.charAt(i)) {
                return 1;
            } else {
                continue;
            }
        }
        return 0;
    }
}
