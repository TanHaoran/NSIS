package com.jerry.nsis.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.jerry.nsis.activity.CustomPromptActivity;
import com.jerry.nsis.activity.OrderPromptActivity;
import com.jerry.nsis.common.CommonValues;
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
import java.util.Random;

/**
 * Created by Jerry on 2016/2/17.
 */
public class TypeLine extends LinearLayout {

    private Context mContext;

    private TriangleArrow mArrow;
    private TextView mTextType;
    private TextView mPaging;
    private TextView mTextNum;
    private RelativeLayout mPagingLayout;
    private ImageView mPagingImg;
    private SmallBed mBed1;
    private SmallBed mBed2;
    private SmallBed mBed3;
    private SmallBed mBed4;
    private SmallBed mBed5;
    private SmallBed[] mBeds;
    private List<BedInfo> mBedInfoList;

    private Nursing mNursing;

    public static long PAGING_TIME = 15 * 1000;

    // 当前正处在页码序号
    private int mIndex = 0;
    // 一共有多少页
    private int mPage = 0;

    private static final int CUSTOM_PROMPT = 1;


    public TypeLine(Context context) {
        this(context, null);
    }

    public TypeLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypeLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_type_line, this);
        mArrow = (TriangleArrow) findViewById(R.id.tarrow);
        mTextType = (TextView) findViewById(R.id.tv_bottom_type);
        mTextNum = (TextView) findViewById(R.id.tv_number);
        mPaging = (TextView) findViewById(R.id.tv_paging);
        mPagingLayout = (RelativeLayout) findViewById(R.id.rl_paging);
        mPagingImg = (ImageView) findViewById(R.id.iv_paging);
        mBed1 = (SmallBed) findViewById(R.id.bed1);
        mBed2 = (SmallBed) findViewById(R.id.bed2);
        mBed3 = (SmallBed) findViewById(R.id.bed3);
        mBed4 = (SmallBed) findViewById(R.id.bed4);
        mBed5 = (SmallBed) findViewById(R.id.bed5);
        mBeds = new SmallBed[]{mBed1, mBed2, mBed3, mBed4, mBed5};

        setVisibility(View.INVISIBLE);

        mPagingLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPage > 1) {
                    nextPage();
                }
            }
        });

        mTextType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 医嘱提醒
                if (mNursing.getItemType().equals(CommonValues.TYPE_ORDER)) {
                    // 重要显示要超过10个才可以弹出页面，普通显示超过5个就可以弹出
                    if ((mNursing.isImportantShow() && mBedInfoList.size() > 0) || (!mNursing.isImportantShow() && mBedInfoList.size() > 0)) {
                        Intent intent = new Intent(mContext, OrderPromptActivity.class);
                        Prompt prompt = createPrompt();
                        intent.putExtra("prompt", prompt);
                        mContext.startActivity(intent);
                    }
                }
                // 自定义提醒
                else if (mNursing.getItemType().equals(CommonValues.TYPE_CUSTOM)) {
                    Intent intent = new Intent(mContext, CustomPromptActivity.class);
                    Prompt prompt = createPrompt();
                    intent.putExtra("prompt", prompt);
                    ((Activity) mContext).startActivityForResult(intent, CUSTOM_PROMPT);
                }
            }
        });
        mBedInfoList = new ArrayList<>();
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
            max = mBedInfoList.size();
        } else {
            max = (mIndex - 1) * 5 + 5;
        }
        for (SmallBed b : mBeds) {
            b.clearBedInfo();
        }
        for (int i = (mIndex - 1) * 5; i < max; i++) {
            mBeds[i % 5].setInfo(mBedInfoList.get(i));
            mBeds[i % 5].setHasInfo(true);
        }
        mPaging.setText(mIndex + "/" + mPage);
        startPagingAnim();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!JsonUtil.isEmpty(mBedInfoList)) {
                nextPage();
            }
        }
    };


    /**
     * 自动刷新进程
     */
    private void startFefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(PAGING_TIME);
                        Message msg = mHandler.obtainMessage();
                        msg.sendToTarget();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @NonNull
    private Prompt createPrompt() {
        Prompt prompt = new Prompt();
        for (BedInfo info : mBedInfoList) {
            PromptBed b = new PromptBed();
            b.setBed(info.getNo());
            b.setName(NsisUtil.getPatientNameByHosIdAndInTimes(info.getHosId(), info.getInTimes()));
            if (NsisUtil.getPatientByHosIdAndInTimes(info.getHosId(), info.getInTimes()) != null) {
                b.setSex(NsisUtil.getPatientByHosIdAndInTimes(info.getHosId(), info.getInTimes()).getSex());
            }
            b.setHosId(info.getHosId());
            b.setDetailId(info.getNursingDetail().getDetailId());
            b.setInTimes(info.getInTimes());
            prompt.getBedList().add(b);
        }
        prompt.setNursing(mNursing);
        prompt.setItemId(mNursing.getItemId());
        prompt.setTitle(mNursing.getItemName());
        if (TextUtils.isEmpty(mNursing.getTitleColorString())) {
            prompt.setColor("#0000FF");
        } else {
            prompt.setColor("#" + mNursing.getTitleColorString());
        }
        prompt.setFrequencyId(mNursing.getFrequencyList().get(0).getFrequencyId());
        return prompt;
    }

    public Nursing getNursing() {
        return mNursing;
    }

    public void setNursing(Nursing nursing) {
        this.mNursing = nursing;
    }

    public void setArrowColor(String colorString) {
        if (TextUtils.isEmpty(colorString)) {
            colorString = "#0000FF";
        }
        mArrow.setColor(colorString);
        mTextType.setTextColor(Color.parseColor(colorString));
        mArrow.invalidate();
    }

    public void setType(String type) {
        mTextType.setText(type);
        setVisibility(View.VISIBLE);
    }


    /**
     * 清除所有信息
     */
    public void clearAllBedInfo() {
        mBedInfoList.clear();
        for (SmallBed b : mBeds) {
            b.clearBedInfo();
        }
    }

    public void setBedInfoList(List<BedInfo> list) {
        if (list.size() > 5) {
            mTextNum.setText(String.valueOf(list.size()));
            startPagingAnim();
            mPagingImg.setRotation(AnimationUtil.getRandomRotate());
//            startFefresh();
        } else {
            mTextNum.setText("");
        }
        mBedInfoList = list;
        int max = 0;
        if (list.size() <= 5) {
            max = list.size();
        } else {
            max = 5;
        }
        for (int i = 0; i < max; i++) {
            mBeds[i].setInfo(list.get(i));
            mBeds[i].setHasInfo(true);
        }
        mPage = (int) Math.ceil(mBedInfoList.size() / 5d);
        mIndex = 1;

        if (mPage > 1) {
            mPaging.setText(mIndex + "/" + mPage);
            mPagingLayout.setVisibility(VISIBLE);
        } else {
            mPaging.setText("");
            mPagingLayout.setVisibility(INVISIBLE);
        }
    }

    private void startPagingAnim() {
        RotateAnimation animation = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(PAGING_TIME);//设置动画持续时间
        animation.setRepeatCount(-1); // 动画不停止
        mPagingImg.startAnimation(animation);
    }

}
