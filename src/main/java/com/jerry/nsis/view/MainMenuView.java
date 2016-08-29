package com.jerry.nsis.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.jerry.nsis.R;
import com.jerry.nsis.activity.BedScanActivity;
import com.jerry.nsis.activity.EducationActivity;
import com.jerry.nsis.activity.ExchangeBedActivity;
import com.jerry.nsis.activity.NoteActivity;
import com.jerry.nsis.activity.WorkActivity;
import com.jerry.nsis.utils.AnimationUtil;
import com.jerry.nsis.utils.L;
import com.project.sketchpad.Activity.BoardActivity;

/**
 * 中心菜单控件
 * Created by Jerry on 2016/2/24.
 */
public class MainMenuView extends RelativeLayout {


    private final Context mContext;

    // 菜单开关的状态
    private Status mStatus = Status.CLOSE;

    public enum Status {
        CLOSE, OPEN;
    }

    private RelativeLayout mItemLayout;
    private View mInView;
    private View mOutView;
    private RelativeLayout mCenterButton;
    private MainMenuItem mEdct;
    private MainMenuItem mBoard;
    private MainMenuItem mWork;
    private MainMenuItem mBed;
    private MainMenuItem mExchange;
    private MainMenuNote mNote;


    // 是否拖动
    private boolean move = false;

    public long openTime = 0;

    private static final int START_ROTATION = 1;
    private static final int CLOSE_MENU = 2;


    public void setMove(boolean move) {
        this.move = move;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_ROTATION:
                    ObjectAnimator anim = ObjectAnimator.ofFloat(mCenterButton, "rotationX", 0.0F, 360.0F).setDuration(1000);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.start();
                    break;
                case CLOSE_MENU:
                    closeMenu();
                    break;
                default:
                    break;
            }
        }
    };


    public MainMenuView(Context context) {
        this(context, null);
    }

    public MainMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MainMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_main_menu, this);
        mItemLayout = (RelativeLayout) findViewById(R.id.rl_item);
        mCenterButton = (RelativeLayout) findViewById(R.id.btn_center);
        mInView = findViewById(R.id.v_in);
        mOutView = findViewById(R.id.v_out);
        mEdct = (MainMenuItem) findViewById(R.id.menu_edu);
        mWork = (MainMenuItem) findViewById(R.id.menu_work);
        mBoard = (MainMenuItem) findViewById(R.id.menu_board);
        mBed = (MainMenuItem) findViewById(R.id.menu_bed);
        mExchange = (MainMenuItem) findViewById(R.id.menu_exchange);
        mNote = (MainMenuNote) findViewById(R.id.menu_note);

        mCenterButton.setOnClickListener(mItemClickListener);
        mEdct.setOnClickListener(mItemClickListener);
        mWork.setOnClickListener(mItemClickListener);
        mBoard.setOnClickListener(mItemClickListener);
        mBed.setOnClickListener(mItemClickListener);
        mExchange.setOnClickListener(mItemClickListener);
        mNote.setOnClickListener(mItemClickListener);


        Thread rotate = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        if (mStatus == Status.CLOSE) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = START_ROTATION;
                            msg.sendToTarget();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        rotate.start();

        Thread openThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mStatus == Status.OPEN) {
                        long secondTime = System.currentTimeMillis();
                        // 如果菜单打开时间超过10秒，就自动关闭
                        if (secondTime - openTime > 10000) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = CLOSE_MENU;
                            msg.sendToTarget();
                        }
                    }
                }
            }
        });
        openThread.start();
    }


    private View.OnClickListener mItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!move) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.btn_center:
                        toggleMenu();
                        return;
                    case R.id.menu_edu:
                        intent.setClass(mContext, EducationActivity.class);
                        break;
                    case R.id.menu_work:
                        intent.setClass(mContext, WorkActivity.class);
                        break;
                    case R.id.menu_board:
                        intent.setClass(mContext, BoardActivity.class);
                        break;
                    case R.id.menu_bed:
                        intent.setClass(mContext, BedScanActivity.class);
                        break;
                    case R.id.menu_exchange:
                        intent.setClass(mContext, ExchangeBedActivity.class);
                        break;
                    case R.id.menu_note:
                        intent.setClass(mContext, NoteActivity.class);
                        break;
                    default:
                        break;
                }
                mContext.startActivity(intent);
            }
        }
    };

    private void toggleMenu() {
        if (mStatus == Status.CLOSE) {
            openMenu();
        } else {
            closeMenu();
        }
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {

        AnimationUtil.menuHide(mItemLayout);

        mStatus = Status.CLOSE;
    }

    /**
     * 打开菜单
     */
    private void openMenu() {
        openTime = System.currentTimeMillis();
        AnimationUtil.menuShow(mItemLayout);

        mStatus = Status.OPEN;
    }

    /**
     * 获取中心按钮
     *
     * @return
     */
    public RelativeLayout getCenterButton() {
        return mCenterButton;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        L.i("中心菜单重绘了");
        super.onDraw(canvas);
    }
}
