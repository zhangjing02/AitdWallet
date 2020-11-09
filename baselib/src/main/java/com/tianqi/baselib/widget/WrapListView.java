package com.tianqi.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @创建者 zhangjing
 * @创时间 2019/3/79:09
 * @描述
 * @版本 RapidzpayForWrapListview
 * @更新时间 2019/3/7
 * @更新描述 TODO
 */
public class WrapListView extends ListView {
    public WrapListView(Context context)
    {
        super(context);
    }
    public WrapListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public WrapListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
