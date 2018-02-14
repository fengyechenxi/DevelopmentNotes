package org.gx.notes.base;

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

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/12 0012.
 */

public abstract class BaseFragment extends Fragment{

    private Activity activity;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(layoutResId(),container,false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    public abstract @LayoutRes int layoutResId();
    public abstract void setupViews(@Nullable Bundle savedInstanceState);
}
