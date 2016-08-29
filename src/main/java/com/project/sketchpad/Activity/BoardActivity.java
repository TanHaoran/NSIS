package com.project.sketchpad.Activity;


import com.jerry.nsis.R;
import com.jerry.nsis.activity.NsisApplication;
import com.jerry.nsis.utils.AnimationUtil;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.T;
import com.jerry.nsis.view.MyProgressDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.project.sketchpad.Contral.BitmapUtil;
import com.project.sketchpad.Contral.FileOpenHelper;
import com.project.sketchpad.Contral.PlygonCtl;
import com.project.sketchpad.view.BoardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

@ContentView(R.layout.activity_board)
public class BoardActivity extends Activity {

    @ViewInject(R.id.btn_eraser)
    private ImageView mEraser;

    @ViewInject(R.id.btn_pencil)
    private ImageView mPencil;

    @ViewInject(R.id.btn_pen)
    private ImageView mPen;

    @ViewInject(R.id.seekBar)
    private SeekBar seekBar;

    @ViewInject(R.id.boardview)
    private BoardView mBoardView;
    //	----------------------------------------------------
    private static boolean plygon_Click = false;
    private static boolean save_Click = false;
    // -----------------------------------------------------------
    private static final int REQUEST_TYPE_OPEN = 1;
    private static final int REQUEST_TYPE_SAVE = 2;

    private MyProgressDialog dialog;

    private static final float SCALE_SIZE = 1.3f;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);

        dialog = new MyProgressDialog(this);
        seekBar.setMax(100);

        // 设置调整画笔和橡皮大小的监听
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBoardView.setStrokeSize((seekBar.getProgress() / 10) * 5 + 5, BoardView.STROKE_PENCIL);
                mBoardView.setStrokeSize((seekBar.getProgress() / 10) * 5 + 5, BoardView.STROKE_ERASER);
                L.v("progress1：" + seekBar.getProgress());
                L.v("progress2：" + (seekBar.getProgress() / 20 + 3));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    /**
     * 接收打开对话框Activity返回的值，并打开和保存图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TYPE_OPEN:
                if (resultCode == RESULT_OK) {
                    try {
                        String path = data.getStringExtra("path");
//                        BitmapUtils bitmapUtils = new BitmapUtils(BoardActivity.this);
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
//                        bitmapUtils.display(mBoardView, path);
                        mBoardView.setBkBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    /**
     * 新建一个画板
     * @param v
     */
    @OnClick(R.id.btn_new)
    public void OnNewClick(View v) {
        //    this.onCreate(null);
        mBoardView.clearAllStrokes();
        PlygonCtl.setmPoint(PlygonCtl.getStartPoint().getX(), PlygonCtl.getStartPoint().getY());
    }

    /**
     * 使用铅笔画图
     * @param v
     */
    @OnClick(R.id.btn_pencil)
    public void OnPenClick(View v) {
        AnimationUtil.eduZoomAnim(v, SCALE_SIZE, mEraser, mPen);
        mBoardView.setStrokeType(mBoardView.STROKE_PENCIL);//设置画笔的类型
    }

    /**
     * 使用喷枪画图
     * @param v
     */
    @OnClick(R.id.btn_pen)
    public void OnSpraygunClick(View v) {
        AnimationUtil.eduZoomAnim(v, SCALE_SIZE, mEraser, mPencil);
        mBoardView.setStrokeType(mBoardView.STROKE_PEN);
    }

    /**
     * 保存画板
     * @param v
     */
    @OnClick(R.id.btn_save)
    public void OnSaveClick(View v) {
        dialog.show();
        dialog.setContent("正在保存中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filename = DateUtil.getYMDHMS().replace(" ", "_").replace(":", "_").replace("-", "_");
                String path = FileOpenHelper.getPicturePath() + filename;
                L.i("正在保存");
                Bitmap bmp = mBoardView.getCanvasBitmap();
                if (bmp != null) {
                    BitmapUtil.saveBitmapToSDCard(bmp, path);
                    mHandler.sendEmptyMessage(SAVE_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(SAVE_FAILURE);
                }
            }
        }).start();
    }

    private static final int SAVE_SUCCESS = 101;
    private static final int SAVE_FAILURE = 102;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SAVE_SUCCESS:
                    T.showLong(BoardActivity.this, "保存成功");
                    L.i("保存成功");
                    break;
                case SAVE_FAILURE:
                    T.showLong(BoardActivity.this, "保存失败");
                    L.i("保存失败");
                    break;
                default:
                    break;
            }
            mBoardView.clearAllStrokes();
            PlygonCtl.setmPoint(PlygonCtl.getStartPoint().getX(), PlygonCtl.getStartPoint().getY());
            dialog.dismiss();
        }
    };

    /**
     * 打开画板
     * @param v
     */
    @OnClick(R.id.btn_open)
    public void OnOpenClick(View v) {
        startActivityForResult(new Intent(this, OpenPictureActivity.class), REQUEST_TYPE_OPEN);
        PlygonCtl.setmPoint(PlygonCtl.getStartPoint().getX(), PlygonCtl.getStartPoint().getY());
    }

    /**
     * 画直线
     * @param v
     */
    @OnClick(R.id.btn_line)
    public void OnLineClick(View v) {
        mBoardView.setStrokeType(mBoardView.STROKE_LINE);
    }


    /**
     * 画圆
     * @param v
     */
    @OnClick(R.id.btn_circle)
    public void OnCycleClick(View v) {
        mBoardView.setStrokeType(mBoardView.STROKE_CIRCLE);
    }

    /**
     * 画矩形
     * @param v
     */
    @OnClick(R.id.btn_rect)
    public void OnRectClick(View v) {
        mBoardView.setStrokeType(mBoardView.STROKE_RECT);
    }

  
    /**
     * 使用橡皮
     * @param v
     */
    @OnClick(R.id.btn_eraser)
    public void OnEraserClick(View v) {
        AnimationUtil.eduZoomAnim(v, SCALE_SIZE, mPen, mPencil);
        //设置橡皮擦的类型
        mBoardView.setStrokeType(mBoardView.STROKE_ERASER);
        //mPen.setEnabled(true);
        //mEraser.setEnabled(false);
    }

    /**
     * 返回撤销点击事件
     *
     * @param v
     */
    @OnClick(R.id.btn_redo)
    public void onRedoClick(View v) {
        mBoardView.redo();
    }

    /**
     * 撤销点击事件
     *
     * @param v
     */
    @OnClick(R.id.btn_undo)
    public void OnUndoClick(View v) {
        mBoardView.undo();
    }


    /**
     * 切换颜色
     * @param v
     */
    @OnClick(R.id.btn_color)
    public void OnColorClick(View v) {
        //跳转到GridViewDemoActivity
        Intent intent = new Intent(BoardActivity.this, GridViewColorActivity.class);
        BoardActivity.this.startActivity(intent);
    }

    //	-----------------------------------------------------------
    //判断是否点击了绘制多边形按钮
    public static boolean isPlygon_Click() {
        return plygon_Click;
    }

    public static void setPlygon_Click(boolean plygon_Click) {
        BoardActivity.plygon_Click = plygon_Click;
        //设置多边形边数为0    2011-7-28---
        PlygonCtl.setCountLine(0);
    }

    @OnClick(R.id.iv_close)
    public void onClose(View v) {
        finish();
    }

}