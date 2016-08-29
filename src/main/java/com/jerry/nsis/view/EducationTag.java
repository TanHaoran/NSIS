package com.jerry.nsis.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.adapter.EducationAdapter;
import com.jerry.nsis.entity.Education;
import com.jerry.nsis.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/2/17.
 */
public class EducationTag extends RelativeLayout {

    private String mTag;
    private int mNumber;

    private TextView mTagView;

    private View view;

    private EducationAdapter adapter;
    private List<Education> eduList;


    public EducationTag(Context context) {
        this(context, null);
    }

    public EducationTag(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EducationTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_edu_tag, this);
        mTagView = (TextView) findViewById(R.id.tv_edu_tag);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setDatas(filterData());
            }
        });
    }

    private List<Education> filterData() {
        L.i("指定标签是：" + mTag);
        List<Education> list = new ArrayList<>();
        for (Education e : eduList) {
            L.i("检测到的标签是：" + e.getType());
            if (e.getType().equals(mTag)) {
                list.add(e);
            }
        }
        L.i("一共有的个数：" + list.size());
        return list;
    }

    public void setEduList(List<Education> eduList) {
        this.eduList = eduList;
        L.i("设置的个数：" + eduList.size());
    }

    public void setAdapter(EducationAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 设置tag的文字
     *
     * @param tag
     */
    public void setTag(String tag) {
        mTag = tag;
        mTagView.setText(mTag + "（" + mNumber + "）");
    }

    public void setNumber(int number) {
        mNumber = number;
        mTagView.setText(mTag + "（" + mNumber + "）");
    }
}
