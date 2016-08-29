package com.project.sketchpad.interfaces;

import android.graphics.Canvas;
public interface IBoardDraw {

	public void draw(Canvas canvas);
	public boolean hasDraw();
	public void cleanAll();
	public void touchDown(float x, float y);
	public void touchMove(float x, float y);
	public void touchUp(float x, float y);
}
