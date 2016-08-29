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
public class TopBlock extends LinearLayout {

    private FrequencyLine mFrequency1;
    private FrequencyLine mFrequency2;
    private FrequencyLine mFrequency3;
    private FrequencyLine mFrequency4;
    private FrequencyLine mFrequency5;
    private FrequencyLine mFrequency6;
    private FrequencyLine mFrequency7;

    private FrequencyLine[] mFrequencys;


    public TopBlock(Context context) {
        this(context, null);
    }

    public TopBlock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_topmain_block, this);

        mFrequency1 = (FrequencyLine) findViewById(R.id.frequencyline1);
        mFrequency2 = (FrequencyLine) findViewById(R.id.frequencyline2);
        mFrequency3 = (FrequencyLine) findViewById(R.id.frequencyline3);
        mFrequency4 = (FrequencyLine) findViewById(R.id.frequencyline4);
        mFrequency5 = (FrequencyLine) findViewById(R.id.frequencyline5);
        mFrequency6 = (FrequencyLine) findViewById(R.id.frequencyline6);
        mFrequency7 = (FrequencyLine) findViewById(R.id.frequencyline7);

        mFrequencys = new FrequencyLine[]{mFrequency1, mFrequency2, mFrequency3, mFrequency4, mFrequency5, mFrequency6, mFrequency7};
    }

    /**
     * 给指定行设置显示类型
     *
     * @param index 行号
     * @param frequency
     */
    public void setFrequency(int index, String frequency) {
        mFrequencys[index].setFrequency(frequency);
    }

    public void setNursing(int index, Nursing nursing) {
        mFrequencys[index].setNursing(nursing);
    }



    /**
     * 清除大模块的所有信息
     */
    public void clearAllBedInfo() {
        for(FrequencyLine line:mFrequencys) {
            line.clearAllBedInfo();
        }
    }

    /**
     * 给指定行设置是否显示
     *
     * @param index      行号
     * @param visibility
     */
    public void setLineVisibility(int index, int visibility) {
        mFrequencys[index].setVisibility(visibility);
    }

    public void setBedInfoList(int index, List<BedInfo> list) {
        mFrequencys[index].setBedInfoList(list);
    }
}
