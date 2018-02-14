package org.gx.notes.loadmore.rv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Desc:用于RecyclerView加载更多的监听，实现滑动到底部自动加载更多
 */
abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {

    private int mPreviousTotal;
    private boolean mIsLoading = true;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private int[] mLastPositions;
    private int mTotalItemCount;
    private int mLastVisibleItemPosition;
    /**
     * 是否向下滚动
     *
     */
    private boolean mIsSlidingToLast;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager)
            mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            mLastPositions = mStaggeredGridLayoutManager.findLastVisibleItemPositions(null);
        }

        int visibleItemCount = recyclerView.getChildCount();
        if (mLinearLayoutManager != null) {
            mTotalItemCount = mLinearLayoutManager.getItemCount();
            mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        } else if (mStaggeredGridLayoutManager != null) {
            mTotalItemCount = mStaggeredGridLayoutManager.getItemCount();
            mLastVisibleItemPosition = mLastPositions[0];
        }

        if (mIsLoading) {
            if (mTotalItemCount > mPreviousTotal) {//加载更多结束
                mIsLoading = false;
                mPreviousTotal = mTotalItemCount;
            } else if (mTotalItemCount < mPreviousTotal) {//用户刷新结束
                mPreviousTotal = mTotalItemCount;
                mIsLoading = false;
            } else {//有可能是在第一页刷新也可能是加载完毕

            }
        }

        if (!mIsLoading
                && mIsSlidingToLast
                && visibleItemCount > 0
                && mTotalItemCount - 1 == mLastVisibleItemPosition
                && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            mIsSlidingToLast = false;
            loadMore();
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
//        if(dy > 0){//向下滑动
//            mIsSlidingToLast = true;
//        }
//        else {//向上滑动
//            mIsSlidingToLast = false;
//        }
        mIsSlidingToLast = dy > 0;
    }


    public abstract void loadMore();
}
