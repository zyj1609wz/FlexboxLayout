package com.zhaoyanjun.flexboxlayout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjun.zhao
 * @time 2020/10/29 9:06 PM
 * @desc 升级版本-考虑 TagLayout2 padding 情况
 */
public class TagLayout2 extends ViewGroup {

    int lineMaxHeight = 0;  //一行最大的高度
    int lineUseWidth = 0;
    int lineUseHeight = 0;
    int viewWidth = 0;  //view的宽度
    List<Rect> childBoundList = new ArrayList<>(); //保存子view坐标

    public TagLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        lineUseWidth = paddingLeft;
        lineUseHeight = paddingTop;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            int rWidth = lineUseWidth + child.getMeasuredWidth();

            if (rWidth > size - getPaddingLeft() - getPaddingRight()) {
                //换行
                lineUseWidth = paddingLeft;
                lineUseHeight += lineMaxHeight;
                lineMaxHeight = 0;
                Log.d("rrr--", "换行------------------2:" + lineUseHeight);
            }

            Rect bound = new Rect(lineUseWidth, lineUseHeight, lineUseWidth + child.getMeasuredWidth(), lineUseHeight + child.getMeasuredHeight());
            Log.d("rrr--", "保存数据------------------2:" + bound.left + " " + bound.top + " " + bound.right + " " + bound.bottom);
            childBoundList.add(bound);
            lineUseWidth += child.getMeasuredWidth();
            lineMaxHeight = Math.max(lineMaxHeight, child.getMeasuredHeight());
            viewWidth = Math.max(viewWidth, lineUseWidth);
        }

        //view的高度=已经用了高度+最后一行最大高度 + paddingBottom
        int viewHeight = lineUseHeight + lineMaxHeight + paddingBottom;

        Log.d("rrr--", "保存数据 view宽高------------------2:" + viewWidth + " " + viewHeight);

        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Rect bond = childBoundList.get(i);
            child.layout(bond.left, bond.top, bond.right, bond.bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
