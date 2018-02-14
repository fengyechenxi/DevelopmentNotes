package org.gx.notes.load;

import android.content.Context;
import android.view.View;

import org.gx.notes.R;
import org.gx.notes.load.loading.viewport.ShowingViewport;

/**
 * Created by Administrator on 2018/2/12 0012.
 */

public class ErrorViewport extends ShowingViewport{

    @Override
    protected int onCreateView() {
        return R.layout.viewport_error;
    }

    @Override
    protected void initRetry(Context context, final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReload(view);
            }
        });

    }
}
