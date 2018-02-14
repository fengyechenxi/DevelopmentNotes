package org.gx.notes.loadmore.rv.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 *
 */
public class RecyclerViewGridDivider extends RecyclerView.ItemDecoration {
    /**
     * 绘制分割线的画笔
     */
    private Paint mPaint;
    private Builder mBuilder;

    public RecyclerViewGridDivider(Context context, Builder builder) {
        mBuilder = builder;
        initPaint(context,builder.mDividerColor);
    }

    private void initPaint(Context context, @ColorRes int dividerColor){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(context,dividerColor));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            boolean s = isLastColumn(parent,i,getSpanCount(parent),parent.getAdapter().getItemCount());
            if(!s){
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                int left = child.getRight() + params.rightMargin;
                int right = left + mBuilder.mDividerSize;
                int top = child.getTop() - params.topMargin;
                int bottom = child.getBottom() + params.bottomMargin + mBuilder.mDividerSize;
                c.drawRect(left,top,right,bottom,mPaint);
            }
        }

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < childCount; i++) {

            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mBuilder.mDividerSize;
            c.drawRect(left,top,right,bottom,mPaint);


        }
    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                 int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

//    private boolean isLastRow(RecyclerView parent, int pos, int spanCount,
//                                 int childCount) {
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
//            if (pos / spanCount == totalRow(childCount,spanCount) - 1)// 如果是最后一列，则不需要绘制右边
//            {
//                return true;
//            }
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            int orientation = ((StaggeredGridLayoutManager) layoutManager)
//                    .getOrientation();
//            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
//                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
//                {
//                    return true;
//                }
//            } else {
//                childCount = childCount - childCount % spanCount;
//                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
//                    return true;
//            }
//        }
//        return false;
//    }

    private int totalRow(int childCount,int spanCount){
        if(childCount % spanCount == 0){
            return childCount / spanCount;
        }
        else return childCount / spanCount + 1;
    }

    private int getColumn(int position,int spanCount){
        return position % spanCount;
    }

    private int getRow(int position,int spanCount){
        return position / spanCount;
    }



    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();


        if(isLastColumn(parent,position,spanCount,childCount)){//最后一列
            outRect.set(0,0,0,mBuilder.mDividerSize);
        }
        else {
            outRect.set(0,0,mBuilder.mDividerSize,mBuilder.mDividerSize);
        }

    }

    public static class Builder{
        /**
         * 分割线的尺寸，如果当前是垂直，该值就是分割线的宽度，
         * 如果分割线是水平，该值就是分割线的高度，默认值10px
         */
        private int mDividerSize = 10;
        /**
         * 分割线颜色，默认是系统的
         */
        private @ColorRes
        int mDividerColor = android.R.color.background_dark;

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

        public RecyclerViewGridDivider build(){
            return new RecyclerViewGridDivider(mContext,this);
        }

    }

}
