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

public abstract class BaseHeaderWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int ITEM_TYPE_HEADER = Integer.MAX_VALUE - 2;

    private Context mContext;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;
    private boolean isHaveStatesView = true;

    /**
     * 采用装饰者模式的构造方法
     * @param context 上下文对象
     * @param adapter 当前包装的普通Adapter对象
     */
    public BaseHeaderWrapper(Context context, @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter){
        mContext = context;
        mInnerAdapter = adapter;
    }

    public Context getContext() {
        return mContext;
    }



    public abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);
    public abstract void onBindViewHolderOfHeader(RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isHeaderType(viewType)){
            return onCreateHeaderViewHolder(parent);
        }

        return mInnerAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!isHeaderType(holder.getItemViewType())){
            mInnerAdapter.onBindViewHolder(holder,position);
        }
        else {
            onBindViewHolderOfHeader(holder,position);
        }
    }


    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (isHaveStatesView ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isHaveStatesView) {
            return ITEM_TYPE_HEADER;
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

    public boolean isHeaderType(int type) {
        return type == ITEM_TYPE_HEADER;
    }





}
