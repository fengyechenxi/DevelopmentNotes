package org.gx.notes.listener;

import android.view.View;

/**
 * RecyclerView的通用Item点击监听器
 * Created by Administrator on 2018/2/11 0011.
 */

public interface OnRecyclerViewItemClickListener<T> {
    /**
     *RecyclerView的item单击事件，需要设置在Adapter上
     * @param itemView 就是ViewHolder中的itemView
     * @param data  每个item对应的javaBean对象
     * @param position 点击的item的序号
     */
    void onRecyclerViewItemClick(View itemView, T data, int position);
}
