package com.tianqi.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.tianqi.baselib.R;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.ScreenUtils;


/**
 * @Description: 自定义普通进度条
 * @Param:
 * @Return:
 * @Author: zhangliangming
 * @Date: 2018-02-14
 * @Throws:
 */
public class CustomSeekBar extends View {
    /**
     * 背景画笔
     */
    private Paint mBackgroundPaint;

    /**
     * 进度画笔
     */
    private Paint mProgressPaint;

    /**
     * 第二进度画笔
     */
    private Paint mSecondProgressPaint;

    /**
     * 游标画笔
     */
    private Paint mThumbPaint;
    private Paint mThumbSmallPaint;

    /**
     * 文本画笔
     */
    private Paint textPaint;
    private Paint textAxisPaint;


    /**
     * 进度
     */
    private int mProgress = 0;
    /**
     * 第二进度
     */
    private int mSecondaryProgress = 0;
    /**
     * 最大值
     */
    private int mMax = 0;
    /**
     * 默认
     */
    private final int TRACKTOUCH_NONE = -1;
    /**
     * 开始拖动
     */
    private final int TRACKTOUCH_START = 0;
    /**
     * 进度改变
     */
    private final int TRACKTOUCH_PROGRESSCHANGED = 1;
    private int mTrackTouch = TRACKTOUCH_NONE;

    private OnChangeListener mOnChangeListener;
    /**
     *
     */
    private boolean isEnabled = true;

    private int mTouchSlop;
    /**
     * X轴和Y最后的位置
     */
    private float mLastX = 0, mLastY = 0;
    /**
     *
     */
    private Handler mHandler = new Handler();


    private int[] colors;
    private String progress_txt=" ";
    private String slow_tag,fast_tag;
    private int progress_start_color,progress_end_color,progress_text_color;


    public CustomSeekBar(Context context) {
        super(context);
        init(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSeekBar);
            progress_start_color = a.getColor(R.styleable.CustomSeekBar_progress_start_color, context.getResources().getColor(R.color.progress_miner_fee_fast));
            progress_end_color = a.getColor(R.styleable.CustomSeekBar_progress_end_color, context.getResources().getColor(R.color.progress_miner_fee_fast));
            progress_text_color = a.getColor(R.styleable.CustomSeekBar_progress_text_color, context.getResources().getColor(R.color.color_progress_text));
            a.recycle();
        }
        init(context);
    }
    /**
     * 初始化
     */
    private void init(Context context) {
        colors = new int[2];
        colors[0]=progress_start_color;
        colors[1]=progress_end_color;

        int rSize = getHeight()/2 / 4;
        if (mTrackTouch != TRACKTOUCH_NONE) {
            rSize = getHeight()/2 / 3;
        }
        int height = getHeight()/2 / 4 / 3;
        int leftPadding = rSize;

        if (getProgress() > 0) {
            leftPadding = 0;
        }

        //setBackgroundColor(Color.TRANSPARENT);
        //
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(context.getResources().getColor(R.color.progress_miner_fee_fast));


        //
        mProgressPaint = new Paint();
        mProgressPaint.setDither(true);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(context.getResources().getColor(R.color.progress_miner_fee_slow));


        //
        mSecondProgressPaint = new Paint();
        mSecondProgressPaint.setDither(true);
        mSecondProgressPaint.setAntiAlias(true);
        //mSecondProgressPaint.setColor(context.getResources().getColor(R.color.colorAccent));

        //
        mThumbPaint = new Paint();
        mThumbPaint.setDither(true);
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(progress_end_color);


        mThumbSmallPaint = new Paint();
        mThumbSmallPaint.setDither(true);
        mThumbSmallPaint.setAntiAlias(true);
        mThumbSmallPaint.setColor(context.getResources().getColor(R.color.white));


        textPaint = new Paint();          // 创建画笔
        textPaint.setColor(progress_text_color);        // 设置颜色
        textPaint.setStyle(Paint.Style.FILL);   // 设置样式
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(ScreenUtils.sp2px(context,16));              // 设置字体大小

        textAxisPaint = new Paint();          // 创建画笔
        textAxisPaint.setColor(context.getResources().getColor(R.color.color_progress_slow_text));        // 设置颜色
        textAxisPaint.setStyle(Paint.Style.FILL);   // 设置样式
        textAxisPaint.setAntiAlias(true);
        textAxisPaint.setDither(true);
        textAxisPaint.setTextSize(ScreenUtils.sp2px(context,10));              // 设置字体大小

        slow_tag=getResources().getString(R.string.take_currency_slow);
        fast_tag=getResources().getString(R.string.take_currency_fast);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int rSize = getHeight()/2 / 4;  //未拖动时滑块的圆半径
        if (mTrackTouch != TRACKTOUCH_NONE) {
            rSize = getHeight()/2 / 4;  //拖动时滑块的圆半径
        }
        int height = getHeight()/2 / 4 / 3;
        int leftPadding = rSize;

        if (getProgress() > 0) {
            leftPadding = 0;
        }

        RectF backgroundRect = new RectF(leftPadding, getHeight()/2 / 2 - height, getWidth(),
                getHeight()/2 / 2 + height);

        //设置渐变色背景
//        LinearGradient shader = new LinearGradient(leftPadding, getHeight()/2 / 2 - height, getWidth(), getHeight()/2 / 2 - height, colors,null, Shader.TileMode.MIRROR);
//        mBackgroundPaint.setShader(shader);
        canvas.drawRoundRect(backgroundRect, rSize, rSize, mBackgroundPaint);

        if (getMax() != 0 && isEnabled) {
            RectF secondProgressRect = new RectF(leftPadding, getHeight()/2 / 2 - height,
                    getSecondaryProgress() * getWidth() / getMax(), getHeight()/2
                    / 2 + height);
            canvas.drawRoundRect(secondProgressRect, rSize, rSize, mSecondProgressPaint);

            RectF progressRect = new RectF(leftPadding, getHeight()/2 / 2 - height,
                    getProgress() * getWidth() / getMax(), getHeight()/2 / 2
                    + height);

            //设置渐变色背景
//            shader = new LinearGradient(leftPadding, getHeight()/2 / 2 - height, getWidth(), getHeight()/2 / 2 - height, colors,null, Shader.TileMode.MIRROR);
//            mBackgroundPaint.setShader(shader);


            canvas.drawRoundRect(progressRect, rSize, rSize, mProgressPaint);  //已划过的进度条，设置颜色

            int cx = getProgress() * getWidth() / getMax();
            if ((cx + rSize) > getWidth()) {
                cx = getWidth() - rSize;
            } else {
                cx = Math.max(cx, rSize);
            }
            int cy = getHeight()/2 / 2;
//            canvas.drawCircle(cx, cy, rSize+10, mThumbPaint);
//            canvas.drawCircle(cx, cy, rSize+10-5, mThumbSmallPaint);

           //画圆角矩形。
            RectF r1 = new RectF();
            r1.left = cx-2;
            r1.right = cx+18;
            r1.top = getHeight()/2 / 2 - height-21 ;
            r1.bottom =  getHeight()/2 / 2 - height+30;
            canvas.drawRoundRect(r1, 10, 10, mProgressPaint);

            //写入文本
            float textWidth = textPaint.measureText(progress_txt);
            textPaint.setColor(progress_text_color);
            canvas.drawText(progress_txt,cx-textWidth/2, cy+60,textPaint);

            textAxisPaint.setColor(getResources().getColor(R.color.color_progress_slow_text));
            float textWidth0 = textAxisPaint.measureText(fast_tag);
            canvas.drawText(slow_tag,0, cy+50,textAxisPaint);
            textAxisPaint.setColor(getResources().getColor(R.color.color_progress_slow_text));
            canvas.drawText(fast_tag,getWidth()-textWidth0, cy+50,textAxisPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled || getMax() == 0)
            return super.onTouchEvent(event);


        float curX = event.getX();
        float curY = event.getY();

        int actionId = event.getAction();
        switch (actionId) {

            case MotionEvent.ACTION_DOWN:
                mLastX = curX;
                mLastY = curY;

                if (mTrackTouch == TRACKTOUCH_NONE) {
                    setTrackTouch(TRACKTOUCH_START);
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onProgressChanged(this);
                    }
                    invalidateProgress(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastX - curX);
                int deltaY = (int) (mLastY - curY);
                LogUtil.i("ttttttttt", mTrackTouch+"----onTouchEvent: 这个阈值是多少？"+mTouchSlop);

                if ((mTrackTouch == TRACKTOUCH_PROGRESSCHANGED) || (Math.abs(deltaX) > mTouchSlop
                        && Math.abs(deltaY) < mTouchSlop)) {
                    //左右移动
                    if (mTrackTouch == TRACKTOUCH_START || mTrackTouch == TRACKTOUCH_PROGRESSCHANGED) {
                        setTrackTouch(TRACKTOUCH_PROGRESSCHANGED);
                        if (mOnChangeListener != null) {
                            mOnChangeListener.onProgressChanged(this);
                        }
                        invalidateProgress(event);
                    }
                }
                break;

            default:
                if (mTrackTouch == TRACKTOUCH_START || mTrackTouch == TRACKTOUCH_PROGRESSCHANGED) {

                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 200);

                    if (mOnChangeListener != null) {
                        mOnChangeListener.onTrackingTouchFinish(this);
                    }

                } else {
                    setTrackTouch(TRACKTOUCH_NONE);
                }
                invalidateProgress(event);

                break;
        }

        return true;
    }

    /**
     *
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setTrackTouch(TRACKTOUCH_NONE);
            postInvalidate();
        }
    };

    /**
     * 更新进度
     *
     * @param event
     */
    private void invalidateProgress(MotionEvent event) {
        if (getMax() != 0) {
            int curX = (int) event.getX();
            if (curX < 0) {
                curX = 0;
            }
            if (curX > getWidth()) {
                curX = getWidth();
            }

            int progress = curX * getMax() / getWidth();
            mProgress = Math.min(getMax(), progress);
//            if (mProgress>3&&mProgress<97){
//                progress_txt=mProgress+" Satoshi";
//            }else {
//                progress_txt=" ";
//            }
            invalidate();
        }
    }

    public synchronized void setProgress(int progress) {
        if (mTrackTouch == TRACKTOUCH_NONE && getMax() != 0) {
            mProgress = Math.min(getMax(), progress);
            postInvalidate();
        }
    }

    public synchronized void setSecondaryProgress(int secondaryProgress) {
        this.mSecondaryProgress = secondaryProgress;
        postInvalidate();
    }

    public synchronized void setMax(int max) {
        this.mMax = max;
        postInvalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    public int getSecondaryProgress() {
        return mSecondaryProgress;
    }

    public int getMax() {
        return mMax;
    }

    private void setTrackTouch(int trackTouch) {
        this.mTrackTouch = trackTouch;
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundPaintColor(int backgroundColor) {
        mBackgroundPaint.setColor(backgroundColor);
        postInvalidate();
    }

    /**
     * 设置进度颜色
     *
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        mProgressPaint.setColor(progressColor);
        postInvalidate();
    }

    /**
     * 设置第二进度颜色
     *
     * @param secondProgressColor
     */
    public void setSecondProgressColor(int secondProgressColor) {
        mSecondProgressPaint.setColor(secondProgressColor);
        postInvalidate();
    }

    /**
     * 设置游标颜色
     *
     * @param thumbColor
     */
    public void setThumbColor(int thumbColor) {
        mThumbPaint.setColor(thumbColor);
        postInvalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        postInvalidate();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    public interface OnChangeListener {
        /**
         * 进度改变
         *
         * @param seekBar
         */
        void onProgressChanged(CustomSeekBar seekBar);

        /**
         * 拖动结束
         *
         * @param seekBar
         */
        void onTrackingTouchFinish(CustomSeekBar seekBar);

    }
}
