package com.aoenang.mmgd;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class GestureDetectorView extends View {

	protected static final String TAG = null;
	// ************event*********************

	public static final int EVENT_MOVE_UP = 1;
	public static final int EVENT_MOVE_RIGHT_UP = 2;
	public static final int EVENT_MOVE_RIGHT = 3;
	public static final int EVENT_MOVE_RIGHT_DOWN = 4;
	public static final int EVENT_MOVE_DOWN = 5;
	public static final int EVENT_MOVE_LEFT_DOWN = 6;
	public static final int EVENT_MOVE_LEFT = 7;
	public static final int EVENT_MOVE_LEFT_UP = 8;
	public static final int EVENT_CLICK = 9;
	public static final int EVENT_CLICK_DOUBLE = 10;
	public static final int EVENT_CLICK_LONG = 11;

	private Context mContext;

	public GestureDetectorView(Context context) {
		super(context);
		this.mContext = context;
	}

	public GestureDetectorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public GestureDetectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 判断并执行action
		if (mDetector.onTouchEvent(event)) {
			return true;
		}
		return super.dispatchTouchEvent(event);
	}

	/**
	 * 手势处理
	 */
	private GestureDetector mDetector = new GestureDetector(mContext,
			new SimpleOnGestureListener() {
				@Override
				public void onLongPress(MotionEvent e) {
					Log.d(TAG, "onLongPress 长按！");
					ToastUtils.showToast(mContext, "Long Press!");
					super.onLongPress(e);
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					// 获取手指滑动的方向
					int eventDirection = getEventDirection(e1, e2);
					ToastUtils.showToast(mContext, "Event----" + eventDirection);
					return super.onFling(e1, e2, velocityX, velocityY);
				}

				@Override
				public boolean onDoubleTap(MotionEvent e) {
					Log.d(TAG, "onDoubleTap 双击！");
					ToastUtils.showToast(mContext, "Double Tap!");
					return super.onDoubleTap(e);
				}

				@Override
				public boolean onSingleTapConfirmed(MotionEvent e) {
					Log.d(TAG, "onSingleTapConfirmed 单击！");
					ToastUtils.showToast(mContext, "Single Tap Confirmed!");
					return super.onSingleTapConfirmed(e);
				}

			});

	// 180°分成8个刻度，每个刻度为22.5°
	private final float DEGREE_UNIT = 22.5f;

	/**
	 * 判断当前的移动方向
	 * 
	 * @param e1
	 * @param e2
	 */
	private int getEventDirection(MotionEvent e1, MotionEvent e2) {
		int event = -1;

		float x1 = e1.getX();
		float y1 = e1.getY();
		float x2 = e2.getX();
		float y2 = e2.getY();
		float a = x2 - x1;// a边为对边 x2-x1
		float b = y2 - y1;// b边为邻边 y2-y1
		// 反正切得到∠B弧度
		double B = Math.atan(a / b);
		// 弧度转换成角度
		B = Math.toDegrees(B);
		// 以x轴为分界线，以180°计算，算出的角度如果为负数，+180
		B = B < 0 ? B + 180 : B;
		if (Math.abs(a) >= 15 || Math.abs(b) >= 15) {
			// 当手指往x轴的正方向移动的时候，y轴正方向为0°，反方向为180°
			if (x2 > x1) {
				if (B < DEGREE_UNIT) {
					Log.d(TAG, "下 ：" + B);
					event = EVENT_MOVE_DOWN;
				} else if (B >= DEGREE_UNIT && B < DEGREE_UNIT * 3) {
					Log.d(TAG, "右下 ：" + B);
					event = EVENT_MOVE_RIGHT_DOWN;
				} else if (B >= DEGREE_UNIT * 3 && B < DEGREE_UNIT * 5) {
					Log.d(TAG, "右 ：" + B);
					event = EVENT_MOVE_RIGHT;
				} else if (B >= DEGREE_UNIT * 5 && B < DEGREE_UNIT * 7) {
					Log.d(TAG, "右上 ：" + B);
					event = EVENT_MOVE_RIGHT_UP;
				} else if (B >= DEGREE_UNIT * 7) {
					Log.d(TAG, "上 ：" + B);
					event = EVENT_MOVE_UP;
				}
			} else if (x2 < x1) {
				// 当手指往x轴的反方向移动的时候y轴反方向为0°，正方向为180°
				if (B < DEGREE_UNIT) {
					Log.d(TAG, "上 ：" + B);
					event = EVENT_MOVE_UP;
				} else if (B >= DEGREE_UNIT && B < DEGREE_UNIT * 3) {
					Log.d(TAG, "左上 ：" + B);
					event = EVENT_MOVE_LEFT_UP;
				} else if (B >= DEGREE_UNIT * 3 && B < DEGREE_UNIT * 5) {
					Log.d(TAG, "左 ：" + B);
					event = EVENT_MOVE_LEFT;
				} else if (B >= DEGREE_UNIT * 5 && B < DEGREE_UNIT * 7) {
					Log.d(TAG, "左下 ：" + B);
					event = EVENT_MOVE_LEFT_DOWN;
				} else if (B >= DEGREE_UNIT * 7) {
					Log.d(TAG, "下 ：" + B);
					event = EVENT_MOVE_DOWN;
				}
			} else {
				// 如果正好x轴不动，y轴移动
				if (y2 > y1) {
					Log.d(TAG, "下 ：" + B);
					event = EVENT_MOVE_DOWN;
				} else if (y2 < y1) {
					Log.d(TAG, "上 ：" + B);
					event = EVENT_MOVE_UP;
				}
			}
		}
		return event;
	}

}
