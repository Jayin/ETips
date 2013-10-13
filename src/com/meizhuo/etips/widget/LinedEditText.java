package com.meizhuo.etips.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 字体下有横线的EditText
 * 
 * @author Jayin Ton edited
 * 
 */
public class LinedEditText extends EditText {
	private Paint linePaint;
	public LinedEditText(Context context) {
		super(context);

	}

	public LinedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.linePaint = new Paint();
		this.linePaint.setStyle(Paint.Style.STROKE);
		this.linePaint.setColor(Color.BLACK);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setLines(20);
		// 得到总行数
		int lineCount = getLineCount();
		
		int height = getHeight();
		// 得到每行的高度
		int lineHeight = getLineHeight();
		lineCount =  height / lineHeight + 1 ;
		// 根据行数循环画线
		for (int i = 0; i < lineCount; i++) {
			int lineY = (i + 1) * lineHeight;
			canvas.drawLine(0, lineY, this.getWidth(), lineY, this.linePaint);
		}
	}

}
