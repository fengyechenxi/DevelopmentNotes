package org.gx.notes.toast;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import org.gx.notes.R;

import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2017/9/28 0028.
 */
@Deprecated
public class TopToastView extends DialogFragment {

    public static final int DISMISS_DURATION = 4000;
    //显示toast的间隔时间，如果有按钮点击，不能让toast一直不停显示
    //下次可显示toast的间隔时间
    public static final long INTERVAL_TIME = 10000;
    public static final String INTERVAL_KEY = "top_toast_time_interval";

    protected Builder mBuilder;
    private int mActionBarHeight;
    private int mScreenWidth;
    private int mStatusBarHeight;
    private Handler mHandler;

    private Runnable mDelayDismiss = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };


    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if(window != null){
            window.setLayout(mScreenWidth,mActionBarHeight+mStatusBarHeight);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getDialog().getWindow();
        if(window != null){
            if(Build.VERSION.SDK_INT >= 19){
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //清理背景变暗
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.ToastView;
            window.getAttributes().gravity = Gravity.TOP;
        }


        mHandler = new Handler();

        mActionBarHeight = getActionBarHeight(mBuilder.mContext.get());
        mScreenWidth = getScreenWidth(mBuilder.mContext.get());
        mStatusBarHeight = getStatusHeight(mBuilder.mContext.get());

        TextView toastTextView = new AppCompatTextView(mBuilder.mContext.get());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,getActionBarHeight(mBuilder.mContext.get()));
        layoutParams.topMargin = mStatusBarHeight;
        toastTextView.setLayoutParams(layoutParams);
        toastTextView.setGravity(Gravity.CENTER_VERTICAL);
        int paddingHorizontal = dip2px(mBuilder.mContext.get(),20);
        toastTextView.setPadding(paddingHorizontal,0,paddingHorizontal,0);
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        toastTextView.setLines(1);
        toastTextView.setEllipsize(TextUtils.TruncateAt.END);
        toastTextView.setText(mBuilder.mMessage);
        toastTextView.setBackgroundColor(mBuilder.mBgColor);
        toastTextView.setTextColor(mBuilder.mTextColor);

        FrameLayout rootView = new FrameLayout(mBuilder.mContext.get());
        rootView.setBackgroundColor(mBuilder.mStatusBarColor);
        rootView.addView(toastTextView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        delayDismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mDelayDismiss);
        mHandler = null;
    }

    private void delayDismiss(){
        if(mBuilder.mAutoDismiss){
            mHandler.postDelayed(mDelayDismiss,DISMISS_DURATION);
        }
    }


    public void showToast(){
        if(mBuilder.mIsGlobalToastView){
            if(!canShowToast(mBuilder.getContext().get()))return;
        }
        show(((FragmentActivity)mBuilder.mContext.get()).getSupportFragmentManager(),TopToastView.class.getSimpleName());
    }

    /**
     * 防止同时显示多个ToastView
     * @param context
     * @return
     */
    private boolean canShowToast(Context context){
        synchronized (this) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            long lastTime = sharedPreferences.getLong(INTERVAL_KEY,0);
            long currentTime = System.currentTimeMillis();
            if(currentTime - lastTime >= INTERVAL_TIME){
                sharedPreferences.edit().putLong(INTERVAL_KEY,currentTime).apply();
                return true;
            }
            return false;
        }
    }

    private int getActionBarHeight(Context context){
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        //actionbar高度
        int height = (int) actionbarSizeTypedArray.getDimension(0,0);
        actionbarSizeTypedArray.recycle();
        return height;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static class Builder{

        private WeakReference<Context> mContext;
        private int mBgColor = Color.RED;
        private int mStatusBarColor = Color.LTGRAY;
        private int mTextColor = Color.WHITE;
        private String mMessage;
        private boolean mAutoDismiss = true;
        private boolean mIsGlobalToastView = false;

        public Builder(Context context){
            mContext = new WeakReference<>(context);
        }

        public WeakReference<Context> getContext() {
            return mContext;
        }

        public Builder bgColor(@ColorRes int color){
            mBgColor = getResources().getColor(color);
            return this;
        }

        public Builder bgColor(String color){
            mBgColor = Color.parseColor(color);
            return this;
        }

        public Builder statusBarColor(@ColorRes int color){
            mStatusBarColor = getResources().getColor(color);
            return this;
        }

        public Builder statusBarColor(String color){
            mStatusBarColor = Color.parseColor(color);
            return this;
        }

        public Builder textColor(@ColorRes int color){
            mTextColor = getResources().getColor(color);
            return this;
        }

        public Builder textColor(String color){
            mTextColor = Color.parseColor(color);
            return this;
        }


        public Builder message(@StringRes int msg){
            mMessage = getResources().getString(msg);
            return this;
        }

        public Builder message(String msg){
            mMessage = msg;
            return this;
        }

        public Builder autoDismiss(boolean autoDismiss){
            mAutoDismiss = autoDismiss;
            return this;
        }

        public Builder globalToastView(boolean global) {
            mIsGlobalToastView = global;
            return this;
        }

        public TopToastView build(){
            TopToastView topToastView = new TopToastView();
            topToastView.mBuilder = this;
            return topToastView;
        }

        private Resources getResources(){
            return mContext.get().getResources();
        }

    }


}
