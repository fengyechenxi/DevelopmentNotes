package org.gx.notes.load.loading.viewport;

import android.content.Context;
import android.view.View;

/**
 * 该类不能被继承，加载成功或者完成时候显示的界面
 * Created by Administrator on 2017/9/26 0026.
 */
public final class SuccessViewport extends ShowingViewport {

    public SuccessViewport(Context context, View view, OnReloadListener onReloadListener) {
        super(context, view, onReloadListener);
    }

    /**
     * 这里传0，因为在初始化SuccessViewport时候，会将界面的View传递进来，作为当前的rootView
     * @return
     */
    @Override
    protected int onCreateView() {
        return 0;
    }

    @Override
    protected void initRetry(Context context, View view) {

    }
}
