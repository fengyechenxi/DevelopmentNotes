package org.gx.notes.loadmore.rv.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class RecyclerViewLinearDivider extends RecyclerView.ItemDecoration {

    /**
     * 绘制分割线的画笔
     */
    private Paint mPaint;
    private Builder mBuilder;

    public RecyclerViewLinearDivider(Context context, Builder builder) {
        mBuilder = builder;
        initPaint(context,builder.mDividerColor);
    }


    private void initPaint(Context context, @ColorRes int dividerColor){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context,dividerColor));
    }



    /**
     *
     * @param outRect 该参数里面的是个方向值，表示绘制时，每个ItemView的各个方向的padding值
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int lastItemPosition = parent.getAdapter().getItemCount() - 1;
        if(LinearLayoutManager.VERTICAL == mBuilder.mOrientation){
            if(position == 0){
                //第一个Item上面需要绘制分割线，让top方向留下可绘制的宽度
                if(mBuilder.mHasStartDivider){
                    outRect.top = mBuilder.mDividerSize;
                }
                outRect.bottom = mBuilder.mDividerSize;
            }
            else if(position == lastItemPosition){//最后一个item，如果这里不判断，最后会留有一段padding
                if(mBuilder.mHasEndDivider){
                    outRect.bottom = mBuilder.mDividerSize;
                }
            }
            else outRect.bottom = mBuilder.mDividerSize;
        }
        else if(LinearLayoutManager.HORIZONTAL == mBuilder.mOrientation){
            if(position == 0){
                //第一个Item上面需要绘制分割线，让top方向留下可绘制的宽度
                if(mBuilder.mHasStartDivider){
                    outRect.left = mBuilder.mDividerSize;
                }
                outRect.right = mBuilder.mDividerSize;
            }
            else if(position == lastItemPosition){//最后一个item，如果这里不判断，最后会留有一段padding
                if(mBuilder.mHasEndDivider){
                    outRect.right = mBuilder.mDividerSize;
                }
            }
            else outRect.right = mBuilder.mDividerSize;

        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        if(LinearLayoutManager.VERTICAL == mBuilder.mOrientation){
            drawVertical(c,parent,childCount);
        }
        else if(LinearLayoutManager.HORIZONTAL == mBuilder.mOrientation){
            drawHorizontal(c,parent,childCount);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent, int childCount){
        int left = parent.getPaddingLeft() + mBuilder.mMarginLeft;
        int right = parent.getWidth() - parent.getPaddingRight() - mBuilder.mMarginRight;

        //绘制第一个Item上面的分割线
        if(mBuilder.mHasStartDivider && childCount > 0){
            View child = parent.getChildAt(0);
            c.drawRect(left, child.getTop() - mBuilder.mDividerSize, right, child.getTop(), mPaint);
        }

        int forCount = !mBuilder.mHasEndDivider ? childCount - 1 : childCount;
        for (int i = 0; i < forCount; i++) {
            View child = parent.getChildAt(i);
            c.drawRect(left, child.getBottom(), right, child.getBottom() + mBuilder.mDividerSize, mPaint);
        }

    }

    private void drawHorizontal(Canvas c, RecyclerView parent, int childCount){
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();


        //绘制第一个Item左边的分割线
        if(mBuilder.mHasStartDivider && childCount > 0){
            View child = parent.getChildAt(0);
            c.drawRect(child.getLeft() - mBuilder.mDividerSize, top, child.getLeft(), bottom, mPaint);
        }

        int forCount = !mBuilder.mHasEndDivider ? childCount - 1 : childCount;
        for (int i = 0; i < forCount; i++) {
            View child = parent.getChildAt(i);
            c.drawRect(child.getRight(), top, child.getRight()+  mBuilder.mDividerSize, bottom, mPaint);
        }
    }



    @IntDef({LinearLayoutManager.HORIZONTAL, LinearLayoutManager.VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface LayoutManagerOrientation{

    }

    public static class Builder{
        /**
         * 分割线的尺寸，如果当前RecyclerView是垂直滚动，该值就是分割线的高度，
         * 如果RecycleView是水平滚动，该值就是分割线的宽度，默认值10px
         */
        private int mDividerSize = 10;
        /**
         * 分割线距离RecyclerView左边的距离，默认为0，也就是在最左边开始绘制，
         * 该值只有RecycleView垂直滚动才有效
         */
        private int mMarginLeft = 0;
        /**
         * 分割线距离RecyclerView右边边的距离，默认为0，也就是在最右边开始绘制，
         * 该值只有RecycleView垂直滚动才有效
         */
        private int mMarginRight = 0;
        /**
         * RecyclerView最后一个Item下面是否绘制分割线，默认不绘制
         */
        private boolean mHasEndDivider = false;
        /**
         * RecyclerView第一个Item上面是否绘制分割线，默认不绘制
         */
        private boolean mHasStartDivider = false;

        /**
         * 分割线颜色，默认是系统的
         */
        private @ColorRes
        int mDividerColor = android.R.color.background_dark;

        /**
         * 当前RecycleView的滚动方向
         */
        private @LayoutManagerOrientation int mOrientation = LinearLayoutManager.VERTICAL;

        private Context mContext;

        public Builder(Context context){
            mContext = context;
        }

        public Builder dividerColor(int dividerColor) {
            mDividerColor = dividerColor;
            return this;
        }

        public Builder dividerSize(int dividerSize) {
            mDividerSize = dividerSize;
            return this;
        }

        public Builder marginRight(int marginRight) {
            mMarginRight = marginRight;
            return this;
        }

        public Builder marginLeft(int marginLeft) {
            mMarginLeft = marginLeft;
            return this;
        }

        public Builder hasEndDivider(boolean hasEndDivider) {
            mHasEndDivider = hasEndDivider;
            return this;
        }

        public Builder hasStartDivider(boolean hasStartDivider) {
            mHasStartDivider = hasStartDivider;
            return this;
        }

        public Builder orientation(int orientation) {
            mOrientation = orientation;
            return this;
        }

        public RecyclerViewLinearDivider build(){
            return new RecyclerViewLinearDivider(mContext,this);
        }
    }
}
