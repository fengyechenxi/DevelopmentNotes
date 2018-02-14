package org.gx.notes.loadmore.rv;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private boolean mHasHeader = false;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int pos = mHasHeader ? position -1 : position;
        onBindViewHolderWithHeader(holder,pos);
    }

    public void setHasHeader(boolean hasHeader) {
        mHasHeader = hasHeader;
    }

    public boolean isHasHeader() {
        return mHasHeader;
    }

    public abstract void onBindViewHolderWithHeader(RecyclerView.ViewHolder holder, int position);
}
