package org.gx.notes.loadmore.rv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class ViewHolder extends RecyclerView.ViewHolder{

    private SparseArray<View> views;
    protected View itemView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        views = new SparseArray<>();
        this.itemView = itemView;
    }

    public View getView(int resId){
        View view = views.get(resId);
        if(view == null){
            view = itemView.findViewById(resId);
            views.put(resId,view);
        }
        return view;
    }
}
