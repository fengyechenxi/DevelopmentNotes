package org.gx.notes.load.loading;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 封装loading事件的目标对象的各种数据集合
 * Created by Administrator on 2017/9/26 0026.
 */

class TargetContext {

    private Context mContext;
    private ViewGroup mParentView;
    private View mOldContent;
    private int mChildIndex;
    private int mOldContentId;

    public TargetContext(Context context, ViewGroup parentView, View oldContent, int oldContentId, int childIndex) {
        this.mContext = context;
        this.mParentView = parentView;
        this.mOldContent = oldContent;
        this.mOldContentId = oldContentId;
        this.mChildIndex = childIndex;
    }

    public Context getContext() {
        return mContext;
    }

    public View getOldContent() {
        return mOldContent;
    }

    public int getChildIndex() {
        return mChildIndex;
    }

    public int getOldContentId() {
        return mOldContentId;
    }



    public ViewGroup getParentView() {
        return mParentView;
    }
}
