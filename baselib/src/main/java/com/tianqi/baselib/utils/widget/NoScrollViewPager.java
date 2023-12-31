package com.tianqi.baselib.utils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {
   /**
     * true表示可以滑动，false不能滑动
     */
    private boolean isPagingEnabled = false;
    public NoScrollViewPager(Context context) {
        super(context);
    }
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }
    public void setPagerEnabled(boolean enabled) {
        this.isPagingEnabled = enabled;
    }
}

