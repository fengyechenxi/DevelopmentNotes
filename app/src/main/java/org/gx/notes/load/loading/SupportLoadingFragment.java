package org.gx.notes.load.loading;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.ButterKnife;


/**
 * Fragment需要loading事件时候需要继承的基类
 * Created by Administrator on 2018/2/12 0012.
 */

public abstract class SupportLoadingFragment extends Fragment {

    protected Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //为了支持Loader，这里必须要添加一层底层容器，否则点击返回键第一次只会将原来的布局返回，不能起到原本的作用，需要点击两次返回键才会生效
        FrameLayout loaderContainer = new FrameLayout(activity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        loaderContainer.setLayoutParams(layoutParams);
        //添加Fragment的布局
        View rootView = inflater.inflate(layoutResId(),loaderContainer,false);
        loaderContainer.addView(rootView);
        ButterKnife.bind(this, rootView);
        setupViews(savedInstanceState);
        return loaderContainer;
    }

    public abstract @LayoutRes int layoutResId();
    public abstract void setupViews(@Nullable Bundle savedInstanceState);

}
