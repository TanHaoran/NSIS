package com.jerry.nsis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jerry.nsis.R;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.entity.Nursing;

import java.util.List;

/**
 * Created by Jerry on 2016/2/17.
 */
public class BottomBlock extends LinearLayout {

    private TypeLine line1;
    private TypeLine line2;
    private TypeLine line3;
    private TypeLine line4;
    private TypeLine line5;
    private TypeLine line6;
    private TypeLine line7;
    private TypeLine line8;
    private TypeLine line9;

    private TypeLine[] lines;


    public BottomBlock(Context context) {
        this(context, null);
    }

    public BottomBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_bottommain_block, this);

        line1 = (TypeLine) findViewById(R.id.typeline1);
        line2 = (TypeLine) findViewById(R.id.typeline2);
        line3 = (TypeLine) findViewById(R.id.typeline3);
        line4 = (TypeLine) findViewById(R.id.typeline4);
        line5 = (TypeLine) findViewById(R.id.typeline5);
        line6 = (TypeLine) findViewById(R.id.typeline6);
        line7 = (TypeLine) findViewById(R.id.typeline7);
        line8 = (TypeLine) findViewById(R.id.typeline8);
        line9 = (TypeLine) findViewById(R.id.typeline9);

        lines = new TypeLine[]{line1, line2, line3, line4, line5, line6, line7, line8, line9};
    }

    /**
     * 给指定行设置显示类型
     *
     * @param index 行号
     * @param type
     */
    public void setType(int index, String type) {
        lines[index].setType(type);
    }



    public void setNursing(int index, Nursing nursing) {
        lines[index].setNursing(nursing);
    }

    /**
     * 给指定行设置箭头的颜色并重绘
     *
     * @param index       行号
     * @param colorString
     */
    public void setColor(int index, String colorString) {
        lines[index].setArrowColor(colorString);
    }

    /**
     * 给指定行设置是否显示
     *
     * @param index      行号
     * @param visibility
     */
    public void setLineVisibility(int index, int visibility) {
        lines[index].setVisibility(visibility);
    }

    public void clearAllBedInfo() {
        for(TypeLine line:lines) {
            line.clearAllBedInfo();
        }
    }

    public void setBedInfoList(int index, List<BedInfo> infoList) {
        lines[index].setBedInfoList(infoList);
    }
}
