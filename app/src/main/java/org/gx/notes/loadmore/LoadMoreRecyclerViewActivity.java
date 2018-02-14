package org.gx.notes.loadmore;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.gx.notes.R;
import org.gx.notes.base.BaseActivity;
import org.gx.notes.loadmore.rv.BaseHeaderWrapper;
import org.gx.notes.loadmore.rv.BaseLoadMoreWrapper;
import org.gx.notes.loadmore.rv.BaseRecyclerViewAdapter;
import org.gx.notes.loadmore.rv.DefaultLoadMoreWrapper;
import org.gx.notes.loadmore.rv.ViewHolder;
import org.gx.notes.loadmore.rv.decoration.RecyclerViewLinearDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadMoreRecyclerViewActivity extends BaseActivity {

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more_recycler_view);
        ButterKnife.bind(this);
        initSwipeRefreshLayout();
        initRecyclerViewAndAdapter();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });
    }

    private void initRecyclerViewAndAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecyclerViewLinearDivider.Builder(this)
                        .dividerSize(15)
                        .dividerColor(R.color.color_divider)
                        .build()
        );

        NormalAdapter normalAdapter = new NormalAdapter();
        //这里需要添加Header，因此要设置为true
        normalAdapter.setHasHeader(true);
        HeaderAdapter headerAdapter = new HeaderAdapter(this,normalAdapter);
        //上拉加载的Adapter可以自定义，这里使用已经封装的好的默认上拉加载
        final DefaultLoadMoreWrapper loadMoreWrapper = new DefaultLoadMoreWrapper(this,headerAdapter);
        loadMoreWrapper.setOnLoadMoreListener(new BaseLoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreWrapper.showComplete();
                    }
                },1000);

            }
        });
        recyclerView.setAdapter(loadMoreWrapper);
    }


    /**
     * 正常情况的Adapter，如果RecyclerView添加了Header，必须要调用方法 setHasHeader(true)，
     * 否则点击item时候的position会出错
     */
    private static class NormalAdapter extends BaseRecyclerViewAdapter{

        @Override
        public void onBindViewHolderWithHeader(final RecyclerView.ViewHolder holder, final int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(),"item:"+position,Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(40,40,20,20);
            textView.setTextColor(ContextCompat.getColor(parent.getContext(),android.R.color.black));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
            textView.setText("NormalAdapter");
            textView.setBackgroundColor(Color.WHITE);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            return new NormalViewHolder(textView);
        }

        @Override
        public int getItemCount() {
            return 15;
        }
    }

    static class NormalViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public NormalViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }


    /**
     * 自定义HeaderAdapter，需要继承自BaseHeaderWrapper
     */
    private static class HeaderAdapter extends BaseHeaderWrapper{

        private View headerView;

        public HeaderAdapter(Context context, @NonNull RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
            super(context, adapter);
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            //因为Header单独封装在一个Adapter中，因此不需要回收引用，只需要初始化一次就行
            if (headerView == null) {
                headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_header,parent,false);
            }
            return new HeaderViewHolder(headerView);
        }

        @Override
        public void onBindViewHolderOfHeader(final RecyclerView.ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(),"这是Header",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_banner)
        ImageView ivBanner;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
