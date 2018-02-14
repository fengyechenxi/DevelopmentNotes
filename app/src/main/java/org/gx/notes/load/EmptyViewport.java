package org.gx.notes.load;

import android.content.Context;
import android.view.View;

import org.gx.notes.R;
import org.gx.notes.load.loading.viewport.ShowingViewport;

/**
 * Created by Administrator on 2018/2/12 0012.
 */

public class EmptyViewport extends ShowingViewport{

    @Override
    protected int onCreateView() {
        return R.layout.viewport_empty;
    }

    @Override
    protected void initRetry(Context context, final View view) {
    }
}
