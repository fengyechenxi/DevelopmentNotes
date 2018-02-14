package org.gx.notes.load;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import org.gx.notes.R;
import org.gx.notes.load.loading.Loader;
import org.gx.notes.load.loading.LoadingHelper;
import org.gx.notes.load.loading.SupportLoadingFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/12 0012.
 */

public class LoadingViewFragment extends SupportLoadingFragment {

    @BindView(R.id.iv_banner)
    ImageView ivBanner;


    @Override
    public int layoutResId() {
        return R.layout.fragment_loading_view;
    }

    @Override
    public void setupViews(@Nullable Bundle savedInstanceState) {


    }


    @OnClick(R.id.btn_loading)
    public void onClick(View view){
        LoadingHelper.getHelper().showViewport(ivBanner,LoadingViewportStyle1.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示加载成功
                LoadingHelper.getHelper().showSuccess(ivBanner);
            }
        },2000);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //必须在onCreateView后面调用，否则无法将跟视图替换，因为在必须在onCreateView完成之前，Fragment根本就没有根视图，
        // 而Activity中则可以在setContentView后面调用就行。
        //在Activity中的用法类似，只不过传入的target为Activity对象
        LoadingViewportStyle1 style1 = new LoadingViewportStyle1();
        ErrorViewport errorViewport = new ErrorViewport();
        Loader loader =  new Loader.Builder(ivBanner)
                .addViewport(style1)
                .addViewport(errorViewport)
                .setDefaultViewport(LoadingViewportStyle1.class)
                .build();

        //可以同时注册多个View的Loading事件，这里只写一个例子，不管注册多少个，最后都要一起注销。
        LoadingHelper.getHelper().register(ivBanner, loader);

    }

    @Override
    public void onResume() {
        super.onResume();
        //模拟加载数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示加载成功
                LoadingHelper.getHelper().showViewport(ivBanner,ErrorViewport.class);
            }
        },2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoadingHelper.getHelper().unRegister(ivBanner);
    }

}
