package org.gx.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.gx.notes.base.BaseActivity;
import org.gx.notes.enu.NotesKnowledgePoint;
import org.gx.notes.listener.OnRecyclerViewItemClickListener;
import org.gx.notes.loadmore.rv.decoration.RecyclerViewLinearDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    public static NotesKnowledgePoint[] notesKnowledgePoints = {
            NotesKnowledgePoint.Bezier,
            NotesKnowledgePoint.NavigationBar,
            NotesKnowledgePoint.LoadMoreRecyclerView,
            NotesKnowledgePoint.Loading,
            NotesKnowledgePoint.Toast
    };

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private NotesAdapter notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife推荐在每个使用的地方再调用，不建议在基类中配置，因为ButterKnife不同地方引用，调用的方法不一样
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new RecyclerViewLinearDivider.Builder(this)
                        .dividerColor(android.R.color.background_light)
                        .dividerSize(15)
                        .build()
        );
        notesAdapter = new NotesAdapter(notesKnowledgePoints);
        notesAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<NotesKnowledgePoint>() {
            @Override
            public void onRecyclerViewItemClick(View itemView, NotesKnowledgePoint data, int position) {
                goActivity(new Intent(MainActivity.this, data.cls));
            }
        });
        recyclerView.setAdapter(notesAdapter);

    }

     static class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder>{
        NotesKnowledgePoint[] notesKnowledgePoints;
        OnRecyclerViewItemClickListener<NotesKnowledgePoint> onRecyclerViewItemClickListener;
        public NotesAdapter(NotesKnowledgePoint[] notesKnowledgePoints){
            this.notesKnowledgePoints = notesKnowledgePoints;
        }

        @Override
        public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NotesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main_notes,parent,false));
        }

        @Override
        public void onBindViewHolder(final NotesViewHolder holder, final int position) {
            final NotesKnowledgePoint point = notesKnowledgePoints[position];
            holder.tvTitle.setText(point.title);
            holder.tvDetails.setText(point.details);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onRecyclerViewItemClick(holder.itemView,point,position);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return notesKnowledgePoints == null ? 0 : notesKnowledgePoints.length;
        }

        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<NotesKnowledgePoint> onRecyclerViewItemClickListener) {
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        }
    }


    static class NotesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_details)
        TextView tvDetails;
        public NotesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
