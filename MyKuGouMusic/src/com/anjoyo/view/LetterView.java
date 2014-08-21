package com.anjoyo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/****
 * 覆盖在ListView右侧的字母导航视图
 * 
 * @author Ligang
 *
 */
public class LetterView extends View{
	/**设置了背景的背景颜色**/
	public static final int COLOR_BG = 0xffB7B7B7;
	/**没有设置背景的背景颜色**/
	public static final int COLOR_NO_BG = 0x00000000;
	/**普通状态的字体颜色**/
	public static final int TEXT_COLOR_NORMAL = 0xaa000000;
	/**选中状态的字体颜色**/
	public static final int TEXT_COLOR_SELECTED = 0xffffffff;
	/**字体大小**/
	public static final int TEXT_SIZE_NORMAL = 14;
	/**字母表**/
	public static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
	/**是否显示背景**/
	private boolean isShowBg = false;
	/**该View的宽**/
	private int width;
	/**该View的高**/
	private int height;
	/**画笔**/
	private Paint paint;
	/**单个字母的高度**/
	private int singleHeight;
	/**当前选中的字母索引位置**/
	private int currentSelectedIndex = 0;
	/**上一次选中的字母索引位置**/
	private int lastSelectedIndex = 0;

	private static final int SPACE = 5;
	private Paint paintRect;
	private int textBaseLine;
	private static final int SELECTED_RECT_COLOR = 0xff3399ff;
	public LetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setAntiAlias(true);//抗锯齿
		paint.setTextSize(TEXT_SIZE_NORMAL);
		paint.setFakeBoldText(true);;
		paintRect = new Paint();
		paintRect.setAntiAlias(true);
		paintRect.setColor(SELECTED_RECT_COLOR);
	}
	public LetterView(Context context) {
		super(context);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isShowBg){
			paintRect.setColor(COLOR_BG);
			canvas.drawRect(7, 0, width-7, height, paintRect);
		}
		if(width == 0 || height == 0){
			height = getHeight();
			width = getWidth();
			singleHeight = (height-2*SPACE) / letters.length();
			FontMetricsInt fontMetrics = paint.getFontMetricsInt();  
			RectF mRectF = new RectF(0, SPACE, width, singleHeight+SPACE);
		
			textBaseLine = (int) (mRectF.top + (mRectF.bottom - mRectF.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
		}
		for (int i = 0; i < letters.length(); i++) {
			if (i == currentSelectedIndex) {
				paint.setColor(TEXT_COLOR_SELECTED);
				paintRect.setColor(SELECTED_RECT_COLOR);
				canvas.drawRect(7, i*singleHeight+SPACE, width-7, i*singleHeight+singleHeight+SPACE, paintRect);
			}else{
				if(isShowBg){
					paintRect.setColor(SELECTED_RECT_COLOR);
				}else{
					paint.setColor(TEXT_COLOR_NORMAL);
				}
			}
			float xPos = width / 2 - paint.measureText(letters.charAt(i)+"") / 2;
			float yPos = textBaseLine+i*singleHeight;
			canvas.drawText(letters.charAt(i)+"", xPos, yPos, paint);
		}
	}
	/**事件的y坐标**/
	private float y;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		y = event.getY();
		currentSelectedIndex = (int) (y/singleHeight);
		if(currentSelectedIndex < 0){
			currentSelectedIndex = 0;
			invalidate();
		}
		if(currentSelectedIndex >= letters.length()){
			currentSelectedIndex = letters.length()-1;
			invalidate();
		}
		if(currentSelectedIndex != lastSelectedIndex){
			invalidate();
			if(letterChangeListener != null){
				letterChangeListener.onLetterChange(currentSelectedIndex);
			}
		}
		lastSelectedIndex = currentSelectedIndex;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isShowBg = true;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			isShowBg = false;
			invalidate();
			break;
		}
		return true;
	}
	/**设置选择字母的索引位置**/
	public void setSelectedIndex(int index){
		currentSelectedIndex = index;
		invalidate();
	}
	/**设置选择的字母**/
	public void setSelectedLetter(String letter){
		setSelectedIndex(letters.indexOf(letter));
	}
	private OnLetterChangeListener letterChangeListener;

	public void setLetterChangeListener(OnLetterChangeListener letterChangeListener) {
		this.letterChangeListener = letterChangeListener;
	}
	/****
	 * 回调接口
	 * @author Ligang
	 *
	 */
	public interface OnLetterChangeListener{
		void onLetterChange(int selectedIndex);
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);

		switch (visibility) {
		case VISIBLE:
			startAnimation(getAnim());
			break;

		default:
			break;
		}
	}
	
	private TranslateAnimation anim;
	private TranslateAnimation getAnim(){
		if(anim == null){
			anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0, 
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			anim.setFillAfter(true);
			anim.setDuration(1000);
		}
		return anim;
	}
	
	
}
