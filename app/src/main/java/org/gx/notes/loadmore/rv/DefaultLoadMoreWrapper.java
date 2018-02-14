package org.gx.notes.loadmore.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.gx.notes.R;


/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class DefaultLoadMoreWrapper extends BaseLoadMoreWrapper {

    private View mViewLoadMore;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    public DefaultLoadMoreWrapper(Context context, @NonNull RecyclerView.Adapter adapter) {
        super(context, adapter);
    }

    @Override
    public void showComplete() {
        setCurState(STATE_READY);
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText("查看更多");
    }

    @Override
    public void showLoading() {
        setCurState(STATE_LOADING);
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setText("加载中……");
    }


    @Override
    public void showNoMore() {
        setCurState(STATE_NO_MORE);
        mProgressBar.setVisibility(View.GONE);
        mTextView.setText("没有更多");
    }

    @Override
    public RecyclerView.ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent) {
        if(mViewLoadMore == null){
            mViewLoadMore = LayoutInflater.from(getContext()).inflate(R.layout.gx_default_load_more,parent,false);
            mTextView = mViewLoadMore.findViewById(R.id.tv_state);
            mProgressBar = mViewLoadMore.findViewById(R.id.pb_loading);
        }
        return new ViewHolder(getContext(), mViewLoadMore);
    }


    @Override
    public void onBindViewHolderOfLoadMoreState(final RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NoDoubleClick.isDoubleClick()){//防止双击
                    if(getCurState() == STATE_READY){
                        if(getOnLoadMoreListener() != null){
                            showLoading();
                            getOnLoadMoreListener().onLoadMore();
                        }
                    }
                }
            }
        });

    }

    private static class NoDoubleClick{
        private static long mLastClickTime;
        private final static int SPACE_TIME = 500;

        public synchronized static boolean isDoubleClick() {
            long currentTime = System.currentTimeMillis();
            boolean isClick2 = currentTime - mLastClickTime <= SPACE_TIME;
            mLastClickTime = currentTime;
            return isClick2;
        }
    }
}
