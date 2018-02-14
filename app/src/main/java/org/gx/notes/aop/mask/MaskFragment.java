package org.gx.notes.aop.mask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import org.gx.notes.utils.ScreenUtils;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class MaskFragment extends DialogFragment {

    private @LayoutRes
    int mLayout;
    private @IdRes
    int mCloseId;
    private String mKey;
    private View mRootView;
    private Activity mActivity;


    public static MaskFragment newInstance(@LayoutRes int layout, @IdRes int closeId, String key){
        MaskFragment maskFragment = new MaskFragment();
        maskFragment.mLayout = layout;
        maskFragment.mCloseId = closeId;
        maskFragment.mKey = key;
        return maskFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if(window != null){
            window.setLayout(ScreenUtils.getScreenWidth(mActivity),ScreenUtils.getScreenHeight(mActivity));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if(window != null){
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(mRootView != null && mRootView.getParent() != null){
            ViewGroup parentView = (ViewGroup) mRootView.getParent();
            parentView.removeView(mRootView);
        }
        else {
            mRootView = inflater.inflate(mLayout,container,false);
            mRootView.findViewById(mCloseId).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    dismiss();
                    return false;
                }
            });
        }



        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MaskHelper.saveAlreadyShowMaskLayer(mActivity,mKey);
    }

}
