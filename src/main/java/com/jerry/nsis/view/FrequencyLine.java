package com.jerry.nsis.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.activity.OrderPromptActivity;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.entity.Nursing;
import com.jerry.nsis.entity.Prompt;
import com.jerry.nsis.entity.PromptBed;
import com.jerry.nsis.utils.AnimationUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.NsisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/2/17.
 */
public class FrequencyLine extends LinearLayout {

    private Context mContext;

    private LinearLayout mLayout;

    private String mFrequency;

    private TextView mTextFrequency;
    private ImageView mPagingImg;
    private RelativeLayout mPagingLayout;
    private BigBed mBed1;
    private BigBed mBed2;
    private BigBed mBed3;
    private BigBed mBed4;
    private BigBed mBed5;
    private BigBed mBed6;
    private BigBed mBed7;
    private BigBed mBed8;
    private BigBed mBed9;
    private BigBed mBed10;
    private BigBed[] mBeds;
    private List<BedInfo> mBedList;

    private Nursing nursing;

    private TextView mPaging;


    // 当前正处在页码序号
    private int mIndex = 0;
    // 一共有多少页
    private int mPage = 0;

    public FrequencyLine(Context context) {
        this(context, null);
    }

    public Nursing getNursing() {
        return nursing;
    }

    public void setNursing(Nursing nursing) {
        this.nursing = nursing;
    }

    public FrequencyLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrequencyLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_frequencyline, this);

        mLayout = (LinearLayout) findViewById(R.id.ll_frequencyline_layout);
        mTextFrequency = (TextView) findViewById(R.id.tv_frequency);
        mPaging = (TextView) findViewById(R.id.tv_paging);
        mPagingLayout = (RelativeLayout) findViewById(R.id.rl_paging);
        mPagingImg = (ImageView) findViewById(R.id.iv_paging);
        mBed1 = (BigBed) findViewById(R.id.bed1);
        mBed2 = (BigBed) findViewById(R.id.bed2);
        mBed3 = (BigBed) findViewById(R.id.bed3);
        mBed4 = (BigBed) findViewById(R.id.bed4);
        mBed5 = (BigBed) findViewById(R.id.bed5);
        mBed6 = (BigBed) findViewById(R.id.bed6);
        mBed7 = (BigBed) findViewById(R.id.bed7);
        mBed8 = (BigBed) findViewById(R.id.bed8);
        mBed9 = (BigBed) findViewById(R.id.bed9);
        mBed10 = (BigBed) findViewById(R.id.bed10);
        mBeds = new BigBed[]{mBed1, mBed2, mBed3, mBed4, mBed5, mBed6, mBed7, mBed8, mBed9, mBed10};
        mPaging = (TextView) findViewById(R.id.tv_paging);

        mBedList = new ArrayList<>();

        mPagingLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPage > 1) {
                    nextPage();
                }
            }
        });

        mTextFrequency.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBedList.size() > 0) {
                    Intent intent = new Intent(mContext, OrderPromptActivity.class);
                    Prompt prompt = new Prompt();
                    for (BedInfo info : mBedList) {
                        PromptBed b = new PromptBed();
                        b.setBed(info.getNo());
                        b.setName(NsisUtil.getPatientNameByHosIdAndInTimes(info.getHosId(), info.getInTimes()));
                        b.setSex(NsisUtil.getPatientByHosIdAndInTimes(info.getHosId(), info.getInTimes()).getSex());
                        b.setHosId(info.getHosId());
//                        b.setDetailId(info.getNursingDetail().getDetailId());
                        prompt.getBedList().add(b);
                    }
                    if (nursing != null) {
                        prompt.setItemId(nursing.getItemId());
                        prompt.setTitle(nursing.getItemName() + "  ——  " + mFrequency);
                        prompt.setColor("#" + nursing.getTitleColorString());
                    } else {
                        L.i("nursing空了");
                    }
                    intent.putExtra("prompt", prompt);
                    mContext.startActivity(intent);

                }
            }
        });
    }

    /**
     * 自动刷新进程
     */
    private void startFefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TypeLine.PAGING_TIME);
                        Message msg = mHandler.obtainMessage();
                        msg.sendToTarget();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!JsonUtil.isEmpty(mBedList)) {
                nextPage();
            }
        }
    };


    private void startPagingAnim() {
        RotateAnimation animation = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(TypeLine.PAGING_TIME);//设置动画持续时间
        animation.setRepeatCount(-1); // 动画不停止
        mPagingImg.startAnimation(animation);
    }

    /**
     * 更新页面显示
     */
    private void nextPage() {
        if (mIndex == mPage) {
            mIndex = 1;
        } else {
            mIndex++;
        }
        int max;
        if (mIndex == mPage) {
            max = mBedList.size();
        } else {
            max = (mIndex - 1) * 10 + 10;
        }
        for (BigBed b : mBeds) {
            b.clearBedInfo();
        }
        for (int i = (mIndex - 1) * 10; i < max; i++) {
            mBeds[i % 10].setInfo(mBedList.get(i));
            mBeds[i % 10].setHasInfo(true);
        }
        mPaging.setText(mIndex + "/" + mPage);
    }

    public void setBedInfoList(List<BedInfo> list) {
        if (list.size() > 10) {
            startPagingAnim();
            mPagingImg.setRotation(AnimationUtil.getRandomRotate());
//            startFefresh();
        }
        if (list.size() > 0) {
            setNursing(list.get(0).getNursing());
        }
        mBedList = list;
        int max;
        if (list.size() <= 10) {
            max = list.size();
        } else {
            max = 10;
        }
        for (int i = 0; i < max; i++) {
            mBeds[i].setInfo(list.get(i));
            mBeds[i].setHasInfo(true);
        }

        mPage = (int) Math.ceil(mBedList.size() / 10d);
        mIndex = 1;

        if (mPage > 1) {
            mPaging.setText(mIndex + "/" + mPage);
            mPagingLayout.setVisibility(VISIBLE);
        } else {
            mPaging.setText("");
            mPagingLayout.setVisibility(INVISIBLE);
        }
    }


    /**
     * 清除所有的信息
     */
    public void clearAllBedInfo() {
        mBedList.clear();
        for (BigBed b : mBeds) {
            b.clearBedInfo();
        }
    }

    public void setFrequency(String frequency) {
        mFrequency = frequency;
        mTextFrequency.setText(frequency);
    }

    public void setVisibility(int visibility) {
        mLayout.setVisibility(visibility);
    }

}
