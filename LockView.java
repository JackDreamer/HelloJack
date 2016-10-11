package com.guochao.lockview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Administrator on 2016/10/10.
 */

public class LockView extends View {

    private static final String TAG = "LockView";
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mLockWidth;
    private float mDownX;

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //加载盖子
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_button);

        mPaint = new Paint();

        mLockWidth = mBitmap.getWidth();
    }

    /**
     * onMeasure方法进行测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, mBitmap.getHeight());

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * ondraw方法画
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:


                //获取按下的位置
                mDownX = event.getX();

                float dX = mDownX - mLockWidth / 2;

                int scrollX = (int) (0 - dX);

//                //如果点击盖子以外的位置，则不用滚动
//                if (dX > mLockWidth / 2) {
//                    return false;
//                }
                //右边距
                int maxRight = getWidth() - mLockWidth / 2;
                if (mDownX > maxRight) {
                    dX = Math.abs(maxRight - mLockWidth / 2);
                    scrollX = (int) -dX;
                }

                //左边距

                int minLeft = mLockWidth / 2;

                if (mDownX < minLeft) {
                    dX = minLeft - mLockWidth / 2;
                    scrollX = (int) -dX;
                }

                //滚动
                scrollTo(scrollX, 0);
                break;
            /**
             * 获取移动的距离
             */
            case MotionEvent.ACTION_MOVE:

                //获取移动的距离
                int moveX = (int) event.getX();

                //获取子控件移动的距离
                int dX2 = (int) (moveX - mDownX);

                //子控件移动的距离
                int scrollX2 = -dX2;

                //左右边距的判断,总宽度-子控件的宽度
                int fX = getWidth() - mLockWidth;

                //右边距
                if (scrollX2 < -fX) {

                    scrollX2 = -fX;
                } else if (scrollX2 > 0) {

                    //左边距
                    scrollX2 = 0;
                }

                //在日志中打印出scrollX2的值
                Log.d(TAG, "onTouchEvent: 滑动的距离" + scrollX2);
                scrollTo(scrollX2, 0);

                break;
            /**
             * 拖动时，如果没有达到最大距离，手指抬起则返回起点
             */
            case MotionEvent.ACTION_UP:

                //获取手指抬起的长度
//                int upX = (int) event.getX() - mLockWidth / 2;
                int upX = (int) event.getX() - mLockWidth;


                int fX3 = getWidth() - mLockWidth;
                Log.d(TAG, "onTouchEvent: " + upX + "fx3:" + fX3);
                if (upX < fX3) {
                    //则子控件滚回去
                    scrollTo(0, 0);

                } else {

//                    //关闭页面,滑块滑到底不一定是关闭页面，所以定义一个接口，让外部来实现
//                    if (mActivity != null) {
//                        mActivity.finish();
//                    }

                    if (mOnLockListener != null) {
                        mOnLockListener.onUnlock();
                    }
                }

                break;


        }

        return true;//自己消费事件
    }

    private MainActivity mActivity;

    public void setmActivity(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * 定义一个接口，编写一个滑块滑到底的方法
     */
    public interface onUnLockListener {
        void onUnlock();
    }

    private onUnLockListener mOnLockListener;

    public void setmOnLockListener(onUnLockListener mOnLockListener) {
        this.mOnLockListener = mOnLockListener;
    }
}
