package org.gx.notes.loadmore.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public abstract class BaseLoadMoreWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 1;

    protected static final int STATE_READY = 0;
    protected static final int STATE_LOADING = 1;
    protected static final int STATE_NO_MORE = 2;

    private int mCurState = STATE_READY;
    private Context mContext;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;
    private boolean isHaveStatesView = true;

    private LoadMoreScrollListener mLoadMoreScrollListener;
    private OnLoadMoreListener mOnLoadMoreListener;

    public BaseLoadMoreWrapper(Context context, @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
        mContext = context;
        mInnerAdapter = adapter;
        mLoadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void loadMore() {
                if(mCurState != STATE_NO_MORE) {
                    if (mOnLoadMoreListener != null && isHaveStatesView) {
                        showLoading();
                        mOnLoadMoreListener.onLoadMore();
                    }
                }

            }
        };
    }

    public Context getContext() {
        return mContext;
    }

    public abstract void showComplete();
    public abstract void showLoading();
    public abstract void showNoMore();

    public void setCurState(int curState) {
        mCurState = curState;
    }

    public int getCurState() {
        return mCurState;
    }


    public abstract RecyclerView.ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent);
    public abstract void onBindViewHolderOfLoadMoreState(RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isLoadMoreType(viewType)){
            return onCreateLoadMoreViewHolder(parent);
        }

        return mInnerAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!isLoadMoreType(holder.getItemViewType())){
            mInnerAdapter.onBindViewHolder(holder,position);
        }
        else {
            onBindViewHolderOfLoadMoreState(holder,position);
        }
    }


    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (isHaveStatesView ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isHaveStatesView) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (position == getItemCount() - 1 && isHaveStatesView) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null && isHaveStatesView) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
        recyclerView.addOnScrollListener(mLoadMoreScrollListener);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (holder.getLayoutPosition() == getItemCount() - 1 && isHaveStatesView) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }

    public boolean isLoadMoreType(int type) {
        return type == ITEM_TYPE_LOAD_MORE;
    }




    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }



}
