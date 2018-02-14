package org.gx.notes.load;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.gx.notes.R;
import org.gx.notes.load.loading.SupportLoadingFragment;
import org.gx.notes.load.loading.Loader;
import org.gx.notes.load.loading.LoadingHelper;
import org.gx.notes.load.loading.viewport.ShowingViewport;

/**
 * Created by Administrator on 2018/2/12 0012.
 */

public class LoadingUIStyle1Fragment extends SupportLoadingFragment {

    @Override
    public int layoutResId() {
        return R.layout.fragment_loading_ui_style;
    }

    @Override
    public void setupViews(@Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //必须在onCreateView后面调用，否则无法将跟视图替换，因为在必须在onCreateView完成之前，Fragment根本就没有根视图，
        // 而Activity中则可以在setContentView后面调用就行。
        //在Activity中的用法类似，只不过传入的target为Activity对象
        LoadingViewportStyle1 style1 = new LoadingViewportStyle1();
        ErrorViewport errorViewport = new ErrorViewport();
        errorViewport.setOnReloadListener(new ShowingViewport.OnReloadListener() {
            @Override
            public void onReload(View v) {
                LoadingHelper.getHelper().showViewport(LoadingUIStyle1Fragment.this,LoadingViewportStyle1.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //显示加载成功
                        LoadingHelper.getHelper().showSuccess(LoadingUIStyle1Fragment.this);
                    }
                },2000);
            }
        });
        Loader loader =  new Loader.Builder(this)
                .addViewport(style1)
                .addViewport(errorViewport)
                .setDefaultViewport(LoadingViewportStyle1.class)
                .build();

        LoadingHelper.getHelper().register(this, loader);

    }

    @Override
    public void onResume() {
        super.onResume();
        //模拟加载数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示加载错误
                LoadingHelper.getHelper().showViewport(LoadingUIStyle1Fragment.this,ErrorViewport.class);
            }
        },2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadingHelper.getHelper().unRegister(this);
    }

}
