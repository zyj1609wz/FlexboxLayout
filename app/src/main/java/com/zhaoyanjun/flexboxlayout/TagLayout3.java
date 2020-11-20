package com.zhaoyanjun.flexboxlayout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjun.zhao
 * @time 2020/10/29 9:06 PM
 * @desc 高级升级版-
 * 1、考虑 TagLayout3 padding 情况
 * 2、考虑 child view margin,padding 情况
 */
public class TagLayout3 extends ViewGroup {

    int lineMaxHeight = 0;  //一行最大的高度
    int lineUseWidth = 0;
    int lineUseHeight = 0;
    int viewWidth = 0;  //view的宽度
    List<Rect> childBoundList = new ArrayList<>(); //保存子view坐标

    public TagLayout3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingRight = getPaddingRight();

        lineUseWidth = paddingLeft;
        lineUseHeight = paddingTop;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);

            //获取childView margin
            MarginLayoutParams childParams = (MarginLayoutParams) child.getLayoutParams();
            int topMargin = childParams.topMargin;
            int bottomMargin = childParams.bottomMargin;
            int leftMargin = childParams.leftMargin;
            int rightMargin = childParams.rightMargin;

            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            //子view需要的宽度
            int childNeedWidth = lineUseWidth + child.getMeasuredWidth() + leftMargin + rightMargin;

            if (childNeedWidth > size - getPaddingLeft() - getPaddingRight()) {
                //换行
                lineUseWidth = paddingLeft;
                lineUseHeight += lineMaxHeight;
                lineMaxHeight = 0;
            }

            //计算 child view的左边位置
            int childLayoutLeft = lineUseWidth + leftMargin;
            //计算 child view的右边位置 = 左边位置 + child 的宽度
            int childLayoutRight = childLayoutLeft + child.getMeasuredWidth();

            //计算 child view的上边位置
            int childLayoutTop = lineUseHeight + topMargin;
            //计算 child view的下边位置 = 上边位置 + child 高度
            int childLayoutBottom = childLayoutTop + child.getMeasuredHeight();

            Rect bound = new Rect(childLayoutLeft, childLayoutTop , childLayoutRight, childLayoutBottom);
            childBoundList.add(bound);
            lineUseWidth += child.getMeasuredWidth() + leftMargin + rightMargin;
            lineMaxHeight = Math.max(lineMaxHeight, child.getMeasuredHeight() + topMargin + bottomMargin);
            viewWidth = Math.max(viewWidth, lineUseWidth);
        }

        //view的高度=已经用了高度+最后一行最大高度 + paddingBottom
        int viewHeight = lineUseHeight + lineMaxHeight + paddingBottom;

        setMeasuredDimension(viewWidth + paddingRight, viewHeight);
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
