package com.project.sketchpad.view;

import java.util.ArrayList;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.L;
import com.project.sketchpad.Activity.BoardActivity;
import com.project.sketchpad.Contral.BitmapCtl;
import com.project.sketchpad.Contral.BitmapUtil;
import com.project.sketchpad.Contral.Circlectl;
import com.project.sketchpad.Contral.EraserCtl;
import com.project.sketchpad.Contral.LineCtl;
import com.project.sketchpad.Contral.OvaluCtl;
import com.project.sketchpad.Contral.PenuCtl;
import com.project.sketchpad.Contral.PlygonCtl;
import com.project.sketchpad.Contral.RectuCtl;
import com.project.sketchpad.Contral.Spraygun;
import com.project.sketchpad.data.BoardCommonValue;
import com.project.sketchpad.interfaces.IBoardDraw;
import com.project.sketchpad.interfaces.IUndoRedoCommand;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * 白板实现类
 */
public class BoardView extends View implements IUndoRedoCommand {

    public static final int STROKE_PENCIL = 12;       //铅笔画笔
    public static final int STROKE_ERASER = 2;    //橡皮擦2
    public static final int STROKE_RECT = 9;      //矩形 4
    public static final int STROKE_CIRCLE = 8;    //圆 5
    public static final int STROKE_LINE = 6;      //直线7
    public static final int STROKE_PEN = 5;      //喷枪8

    public static final int UNDO_SIZE = 20;       //撤销栈的大小

    private int mStrokeType = STROKE_PENCIL;   //画笔风格初始化为铅笔
    private static int mStrokeColor = Color.BLACK;   //画笔颜色初始化为黑色
    private static int mPenSize = BoardCommonValue.SMALL_PEN_WIDTH;         //画笔大小
    private static int mEraserSize = BoardCommonValue.LARGE_ERASER_WIDTH;   //橡皮擦大小

    //实例新画布
    private boolean mIsDirty = false;     //标记是否有东西画了
    private boolean mIsTouchUp = false;    //标记是否鼠标弹起
    private boolean mIsSetForeBmp = false;   //标记是否设置了前bitmap
    private int mBgColor = Color.WHITE;    //背景色

    private int mCanvasWidth = 100;    //画布宽
    private int mCanvasHeight = 100;    //画布高

    private Bitmap mForeBitmap = null;     //用于显示的bitmap
    private Bitmap mTempForeBitmap = null; //用于缓冲的bitmap
    private Bitmap mBgBitmap = null;       //用于背后画的bitmap

    private Canvas mCanvas;     //画布
    private Paint mBitmapPaint = null;   //画笔
    private UndoStack mUndoStack = null;//栈存放执行的操作
    private IBoardDraw mCurTool = null;   //记录操作的对象画笔类

    private int mActionTemp = 0;//获取鼠标点击画布的event
    private boolean mMyloop = false;// 喷枪结束标识符

    public BoardView(Context context) {
        this(context, null);
    }

    public BoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    /**
     * 是否有画图了
     *
     * @return
     */
    public boolean isDirty() {
        return mIsDirty;
    }


    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setBkColor(int color) {
        if (mBgColor != color) {
            mBgColor = color;
            invalidate();
        }
    }


    /**
     * 获取画笔的颜色
     *
     * @return
     */
    public int getStrokeColor() {   //得到画笔的大小
        return mStrokeColor;
    }

    /**
     * 设置画笔的颜色
     *
     * @param color
     */
    public static void setStrokeColor(int color) {   //设置画笔颜色
        mStrokeColor = color;
    }


    /**
     * 获取画笔大小
     *
     * @return
     */
    public int getStrokeSize() {   //得到画笔的大小
        return mPenSize;
    }

    /**
     * 获取橡皮大小
     *
     * @return
     */
    public static int  getEraser() {   //得到橡皮擦的大小
        return mEraserSize;
    }


    /**
     * 设置画笔的大小和橡皮擦大小
     *
     * @param size 大小
     * @param type 种类：STROKE_PEN、STROKE_ERASER
     */
    public void setStrokeSize(int size, int type) {
        switch (type) {
            case STROKE_PENCIL:
                mPenSize = size;
                break;
            case STROKE_ERASER:
                mEraserSize = size;
                break;
        }
    }

    /**
     * 清空设置
     */
    public void clearAllStrokes() {
        mUndoStack.clearAll();
        // 设置当前的bitmap对象为空
        if (null != mTempForeBitmap) {
            mTempForeBitmap.recycle();
            mTempForeBitmap = null;
        }
        // Create a new fore bitmap and set to canvas.
        createStrokeBitmap(mCanvasWidth, mCanvasHeight);

        invalidate();
        mIsDirty = true;
    }

    /**
     * 取到当前绘图板的图片
     *
     * @return
     */
    public Bitmap getCanvasBitmap() {
        // 保存图像之前记得调用该方法，否则无法保存
        setDrawingCacheEnabled(true);
        buildDrawingCache(true);
        Bitmap bmp = getDrawingCache(true);
        if (bmp == null) {
            L.i("保存的图像为空");
        }
        Bitmap result = BitmapUtil.copyBitmap(bmp);
        // 保存完图像及的设置DrawingCacheEnabled为false，否则下次保存的还是之前的图像
        setDrawingCacheEnabled(false);
        return result;
    }

    /**
     * 打开图像文件时，设置当前视图为foreBitmap
     *
     * @param foreBitmap
     */
    public void setForeBitmap(Bitmap foreBitmap) {
        if (foreBitmap != mForeBitmap && null != foreBitmap) {
            // Recycle the bitmap.
            if (null != mForeBitmap) {
                mForeBitmap.recycle();
            }
            // Here create a new fore bitmap to avoid crashing when set bitmap to canvas.
            mForeBitmap = BitmapUtil.copyBitmap(foreBitmap);
            if (null != mForeBitmap && null != mCanvas) {
                mCanvas.setBitmap(mForeBitmap);
            }
            invalidate();
        }
    }

    public Bitmap getForeBitmap() {
        return mBgBitmap;
    }

    /**
     * 设置背景bitmap
     *
     * @param bmp
     */
    public void setBkBitmap(Bitmap bmp) {
//        if (mBgBitmap != bmp) {
//            //mBgBitmap = bmp;
//            mBgBitmap = BitmapUtil.copyBitmap(bmp);
//            invalidate();
//        }
        setForeBitmap(bmp);
    }

    public Bitmap getBkBitmap() {
        return mBgBitmap;
    }

    protected void createStrokeBitmap(int w, int h) {
        mCanvasWidth = w;
        mCanvasHeight = h;
        Bitmap bitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
        if (null != bitmap) {
            mForeBitmap = bitmap;
            // Set the fore bitmap to mCanvas to be as canvas of strokes.
            mCanvas.setBitmap(mForeBitmap);
        }
    }

    protected void setTempForeBitmap(Bitmap tempForeBitmap) {
        if (null != tempForeBitmap) {
            if (null != mForeBitmap) {
                mForeBitmap.recycle();
            }
            mForeBitmap = BitmapCtl.duplicateBitmap(tempForeBitmap);
            if (null != mForeBitmap && null != mCanvas) {
                mCanvas.setBitmap(mForeBitmap);
                invalidate();
            }
        }
    }

    protected void setCanvasSize(int width, int height) {//设置画布大小
        if (width > 0 && height > 0) {
            if (mCanvasWidth != width || mCanvasHeight != height) {
                mCanvasWidth = width;
                mCanvasHeight = height;
                createStrokeBitmap(mCanvasWidth, mCanvasHeight);
            }
        }
    }

    //初始化数据   调用
    protected void initialize() {
        mCanvas = new Canvas();//实例画布用于整个绘图操作
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  //实例化画笔用于bitmap设置画布canvas
        mUndoStack = new UndoStack(this, UNDO_SIZE);//实例化队列
        // Set stroke type and create a stroke tool.
        //setStrokeType(STROKE_PENCIL);  //开启画笔默认
    }

    //启动设置画笔的颜色和大小    调用修改
    public void setStrokeType(int type) {
        mStrokeColor = getStrokeColor();
        mPenSize = getStrokeSize();
        switch (type) {
            case STROKE_PENCIL:
                mCurTool = new PenuCtl(mPenSize, mStrokeColor);
                Log.i("sada022", "pen实例化");
                break;
            case STROKE_ERASER:
                mCurTool = new EraserCtl(mEraserSize);
                break;
            case STROKE_RECT:
                mCurTool = new RectuCtl(mPenSize, mStrokeColor);
                Log.i("sada022", "Rect实例化！");
                break;
            case STROKE_CIRCLE:
                mCurTool = new Circlectl(mPenSize, mStrokeColor);
                Log.i("sada022", "Circle实例化！");
                break;
            case STROKE_LINE:
                mCurTool = new LineCtl(mPenSize, mStrokeColor);
                break;
            case STROKE_PEN:
                mCurTool = new Spraygun(mPenSize, mStrokeColor);
                break;
        }
        //用于记录操作动作名称
        mStrokeType = type;
    }


    public boolean canRedo() {
        if (null != mUndoStack) {
            return mUndoStack.canUndo();
        }
        return false;
    }

    public boolean canUndo() {
        if (null != mUndoStack) {
            return mUndoStack.canRedo();
        }
        return false;
    }

    public void onDeleteFromRedoStack() {
    }

    public void onDeleteFromUndoStack() {
    }

    public void redo() {
        if (null != mUndoStack) {
            mUndoStack.redo();
        }
    }

    public void undo() {
        if (null != mUndoStack) {
            mUndoStack.undo();
            Log.i("sada022", "undo00");
        }
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mBgBitmap) {
            RectF dst = new RectF(getLeft(), getTop(), getRight(), getBottom());
            Rect rst = new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight());
            canvas.drawBitmap(mBgBitmap, rst, dst, mBitmapPaint);
        }
        if (null != mForeBitmap) {
            canvas.drawBitmap(mForeBitmap, 0, 0, mBitmapPaint);
        }
        if (null != mCurTool) {
            if (STROKE_ERASER != mStrokeType) {
                if (!mIsTouchUp) {   //调用绘图功能
                    mCurTool.draw(canvas);
                }
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!mIsSetForeBmp) {
            setCanvasSize(w, h);
        }
        Log.i("sada022", "Canvas");
        mCanvasWidth = w;
        mCanvasHeight = h;
        mIsSetForeBmp = false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        float yx = event.getX();
        float yy = event.getY();
        mIsTouchUp = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //根据m_strokeType进行重新生成对象且记录下操作对象
                setStrokeType(mStrokeType);
                mCurTool.touchDown(event.getX(), event.getY());
                if (STROKE_PEN == mStrokeType)  //若当前操作为喷枪则使用线程
                {
                    mMyloop = true;
                    spraygunRun();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurTool.touchMove(event.getX(), event.getY());
                //若果当前操作为橡皮擦或喷枪则调用绘图操作
                if (STROKE_ERASER == mStrokeType) {
                    mCurTool.draw(mCanvas);
                }
                if (STROKE_PEN == mStrokeType) {
                    mCurTool.draw(mCanvas);
                }
                invalidate();
                mIsDirty = true;
                break;
            case MotionEvent.ACTION_UP:
                mIsTouchUp = true;
                if (mCurTool.hasDraw()) {
                    // Add to undo stack.
                    mUndoStack.push(mCurTool);
                }
                mCurTool.touchUp(event.getX(), event.getY());
                // Draw strokes on bitmap which is hold by mCanvas.
                mCurTool.draw(mCanvas);
                invalidate();
                mIsDirty = true;
                mMyloop = false;
                break;

        }
        return true;
    }

    public class UndoStack {
        private int m_stackSize = 0;   //栈大小
        private BoardView m_sketchPad = null;  //视图对象
        private ArrayList<IBoardDraw> m_undoStack = new ArrayList<IBoardDraw>();
        private ArrayList<IBoardDraw> m_redoStack = new ArrayList<IBoardDraw>();
        private ArrayList<IBoardDraw> m_removedStack = new ArrayList<IBoardDraw>();

        public UndoStack(BoardView sketchPad, int stackSize) {
            m_sketchPad = sketchPad;
            m_stackSize = stackSize;
        }

        public void push(IBoardDraw sketchPadTool) {
            if (null != sketchPadTool) {
                if (m_undoStack.size() == m_stackSize && m_stackSize > 0) {
                    IBoardDraw removedTool = m_undoStack.get(0);
                    m_removedStack.add(removedTool);
                    m_undoStack.remove(0);
                }
                m_undoStack.add(sketchPadTool);
            }
        }

        //清空栈
        public void clearAll() {
            m_redoStack.clear();
            m_undoStack.clear();
            m_removedStack.clear();
        }

        public void undo() {
            if (canUndo() && null != m_sketchPad) {
                Log.i("sada022", "undo点击");
                IBoardDraw removedTool = m_undoStack.get(m_undoStack.size() - 1);
                m_redoStack.add(removedTool);
                m_undoStack.remove(m_undoStack.size() - 1);

                if (null != mTempForeBitmap) {
                    // Set the temporary fore bitmap to canvas.
                    m_sketchPad.setTempForeBitmap(m_sketchPad.mTempForeBitmap);
                } else {
                    // Create a new bitmap and set to canvas.
                    m_sketchPad.createStrokeBitmap(m_sketchPad.mCanvasWidth, m_sketchPad.mCanvasHeight);
                }
                Canvas canvas = m_sketchPad.mCanvas;
                // First draw the removed tools from undo stack.
                for (IBoardDraw sketchPadTool : m_removedStack) {
                    sketchPadTool.draw(canvas);
                }
                for (IBoardDraw sketchPadTool : m_undoStack) {
                    sketchPadTool.draw(canvas);
                }
                m_sketchPad.invalidate();
            }
        }

        public void redo() {
            if (canRedo() && null != m_sketchPad) {
                IBoardDraw removedTool = m_redoStack.get(m_redoStack.size() - 1);
                m_undoStack.add(removedTool);
                m_redoStack.remove(m_redoStack.size() - 1);

                if (null != mTempForeBitmap) {
                    // Set the temporary fore bitmap to canvas.
                    m_sketchPad.setTempForeBitmap(m_sketchPad.mTempForeBitmap);
                } else {
                    // Create a new bitmap and set to canvas.
                    m_sketchPad.createStrokeBitmap(m_sketchPad.mCanvasWidth, m_sketchPad.mCanvasHeight);
                }
                Canvas canvas = m_sketchPad.mCanvas;

                // First draw the removed tools from undo stack.
                for (IBoardDraw sketchPadTool : m_removedStack) {
                    sketchPadTool.draw(canvas);
                }
                for (IBoardDraw sketchPadTool : m_undoStack) {
                    sketchPadTool.draw(canvas);
                }
                m_sketchPad.invalidate();
            }
        }

        public boolean canUndo() {//
            return (m_undoStack.size() > 0);
        }

        public boolean canRedo() {//判断栈的大小
            return (m_redoStack.size() > 0);
        }
    }

    //喷枪的线程操作
    public void spraygunRun() {// 匿名内部内，鼠标按下不放时的操作，启动一个线程监控
        new Thread(new Runnable() {
            public void run() {
                while (mMyloop) {
                    mCurTool.draw(mCanvas);
                    try {
                        Thread.sleep(50);
                        if (mActionTemp == MotionEvent.ACTION_UP) {
                            mMyloop = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate(); //在线程中更新界面
                }
            }
        }).start();
    }
}
