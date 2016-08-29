package com.jerry.nsis.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.entity.BedInfo;
import com.jerry.nsis.entity.Notice;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/2/17.
 */
public class NoticeView extends LinearLayout {

    private TextView mTextTitle;
    private TextView mTextType;
    private TextView mTextTime;
    private TextView mTextOffice;
    private TextView mTextContent;

    private ImageView mAttach;

    private Context mContext;

    private List<Notice> mNoticeList = new ArrayList<>();
    private Notice mNotice;
    private int mIndex;

    private static final int INVISIBLE_NOTICE = 100;
    private static final int VISIBLE_NOTICE = 101;

    private boolean mShowFinish = false;

    private long mLastShowTime = 0;

    private boolean isStart = false;

    private static final long SHOW_INTERVAL = 5 * 60 * 1000;

    public NoticeView(Context context) {
        this(context, null);
    }

    public NoticeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoticeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.view_notice, this);
        mTextTitle = (TextView) findViewById(R.id.tv_title);
        mTextType = (TextView) findViewById(R.id.tv_type);
        mTextTime = (TextView) findViewById(R.id.tv_time);
        mTextOffice = (TextView) findViewById(R.id.tv_office);
        mTextContent = (TextView) findViewById(R.id.tv_content);
        mAttach = (ImageView) findViewById(R.id.attachment);

        mAttach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当有附件的时候
                if (!TextUtils.isEmpty(mNotice.getFileName())) {
                    L.i("文件路径是：" + LoginInfo.mFileUrl.getUrl() + "/" + mNotice.getUrl());
                    HttpGetUtil.doDonwload(mContext, LoginInfo.mFileUrl.getUrl() + "/" + mNotice.getUrl(),
                            mNotice.getFileName());
                }

            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VISIBLE_NOTICE:
                    mLastShowTime = System.currentTimeMillis();
                    L.i("显示通知消息");
                    setNotice(mNotice);
                    break;
                case INVISIBLE_NOTICE:
                    L.i("隐藏通知消息");
                    L.i("通知消息大小是：" + mNoticeList.size());
                    setVisibility(INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    public void setNotice(Notice notice) {
        mNotice = notice;
        L.i("通知内容：" + notice.toString());
        setVisibility(VISIBLE);
        mTextTitle.setText(notice.getTitle());
        mTextType.setText(notice.getType());
        mTextTime.setText(notice.getStartTime());
        mTextOffice.setText(notice.getOffice());
        mTextContent.setText(notice.getContent());
        if (!TextUtils.isEmpty(notice.getFileName())) {
            mAttach.setVisibility(VISIBLE);
            if (notice.getFileName().endsWith("doc") || notice.getFileName().endsWith("docx")) {
                mAttach.setImageResource(R.drawable.document_word);
            } else if (notice.getFileName().endsWith("ppt") || notice.getFileName().endsWith("pptx")) {
                mAttach.setImageResource(R.drawable.document_ppt);
            }
        } else {
            mAttach.setVisibility(INVISIBLE);
        }
    }

    public void addNotice(Notice notice) {
        mNoticeList.add(notice);
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                L.i("通知消息大小是：" + mNoticeList.size());
                while (true) {
                    // 首先计算间隔的时间超过5分钟了没有
                    long interval = System.currentTimeMillis() - mLastShowTime;
                    L.i("间隔时间是：" + interval);
                    if (interval > SHOW_INTERVAL) {
                        if (mNoticeList.size() > 0) {
                            boolean has = false;
                            for (int i = 0; i < mNoticeList.size(); i++) {
                                mIndex = i;
                                mNotice = mNoticeList.get(i);
                                if (checkNoticeShow(mNotice)) {
                                    has = true;
                                    mHandler.sendEmptyMessage(VISIBLE_NOTICE);
                                    try {
                                        Thread.sleep(mNotice.getLast() * 60 * 1000);
                                        mHandler.sendEmptyMessage(INVISIBLE_NOTICE);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    // 如果没有超过5分钟，就睡眠至超过5分钟
                    try {
                        Thread.sleep(SHOW_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        isStart = true;

    }

    /**
     * 检测通知是否需要显示
     */
    private boolean checkNoticeShow(Notice notice) {
        boolean show = false;
        L.i("开始时间" + notice.getStartTime());
        L.i("结束时间" + notice.getEndTime());
        L.i("当前时间" + DateUtil.getYMDHMS());

        if (DateUtil.compareDate(notice.getStartTime(), DateUtil.getYMDHMS()) <= 0 &&
                DateUtil.compareDate(notice.getEndTime(), DateUtil.getYMDHMS()) >= 0) {
            show = true;
        }
        L.i("检测消息是否显示的返回值：" + show);
        return show;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }
}
