package org.gx.notes.load;

import android.content.Context;
import android.view.View;

import org.gx.notes.R;
import org.gx.notes.load.loading.viewport.ShowingViewport;

/**
 * Created by Administrator on 2018/2/12 0012.
 */

public class LoadingViewportStyle2 extends ShowingViewport{

    @Override
    protected int onCreateView() {
        return R.layout.viewport_loading_style2;
    }

    @Override
    protected void initRetry(Context context, View view) {
        //因为是loading，不需要做什么
    }
}
