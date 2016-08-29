package com.project.sketchpad.Contral;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.project.sketchpad.data.vertexDefine;
import com.project.sketchpad.data.vertexStack;
import com.project.sketchpad.interfaces.IBoardDraw;
/*function:
 * @author:
 * Date:
 */
public class PlygonCtl implements IBoardDraw {
	private Path mPath=new Path();
	private Paint mPaint=new Paint();
	private boolean m_hasDrawn = false;
	//保存move点的坐标
	private static vertexDefine mPoint = new vertexDefine();
	//startPoint用于保存第一个起始点坐标
	private static vertexDefine startPoint = new vertexDefine();
	private static vertexStack pointStack = new vertexStack();
	//-------------史红艳   添加修正 2011-7-28---------------------
	//记录当前多边形绘制的线条数
	private static int countLine = 0;
	//-------------------------------------------------------
	public PlygonCtl(int penSize, int penColor)
	{
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(penColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(penSize);//设置画笔粗细
	}
	public void cleanAll() {
		// TODO Auto-generated method stub
		mPath.reset();
	}
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (null != canvas)
		{
			canvas.drawPath(mPath, mPaint);
			Log.i("sada022", "plygon02");
		}
	}
	public boolean hasDraw() {
		// TODO Auto-generated method stub
		return m_hasDrawn;
		//return false;
	}
	public void touchDown(float x, float y) {
		// TODO Auto-generated method stub
		mPath.reset();
		//判断堆栈是否是为空，若为空则为多边形起始点,保存用以比较多边形是否封闭；
		//若不为空则弹出元素
		if (pointStack.isEmpty()){
			mPoint.setPoint(x, y);
			startPoint.setPoint(mPoint.getX(), mPoint.getY());
		}
		else{
			//弹出上一次绘制的终点，作为此次绘制的始点
			mPoint = pointStack.pop();
			//测试当前多边形绘制是否结束，结束则即可绘制下一个多边形
			if(mPoint.getX()>=startPoint.getX()-1 && mPoint.getX()<=startPoint.getX()+1
					&& mPoint.getY()>=startPoint.getY()-1 && mPoint.getY()<=startPoint.getY()+1){
				touchDown(x,y);			//继续绘制下一个多边形
				Log.i("start","startPointX"+startPoint.getX()+","+"startPointY"+startPoint.getY());
				Log.i("end","mPointX"+mPoint.getX()+","+"mPointY"+mPoint.getY());
				return;
			}
		}
		mPath.moveTo(mPoint.getX(),mPoint.getY());
	}
	public void touchMove(float x, float y) {
		//TODO Auto-generated method stub
		//跟踪移动到的点的坐标
		mPoint.setPoint(x, y);
		m_hasDrawn = true; //表示已经操作了
	}
	public void touchUp(float x, float y) {
		// TODO Auto-generated method stub
		mPoint.setPoint(x, y);
		pointStack.push(mPoint);
		mPath.lineTo(mPoint.getX(), mPoint.getY());
		PlygonCtl.countLine++;
		// commit the path to our offscreen
		//mCanvas.drawPath(mPath, mPaint);
	}
	//自动封闭多边形  -----史红艳添加
	public void lineDraw(Canvas canvas){
		canvas.drawLine(mPoint.getX(),mPoint.getY(),startPoint.getX(),startPoint.getY(), mPaint);
		pointStack.pop();
	}
	//添加get方法    2011-7-27
	public static vertexDefine getmPoint() {
		return mPoint;
	}
	public static vertexDefine getStartPoint() {
		return startPoint;
	}
	public static void setmPoint(float pointX,float pointY) {
		PlygonCtl.mPoint.setX(pointX);
		PlygonCtl.mPoint.setY(pointY);
	}
	//添加get()、set()方法    2011-7-28
	public static int getCountLine() {
		return countLine;
	}
	public static void setCountLine(int countLine) {
		PlygonCtl.countLine = countLine;
	}

}
