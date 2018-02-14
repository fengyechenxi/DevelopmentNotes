package org.gx.notes.toast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import org.gx.notes.R;
import org.gx.notes.dialog.BaseDialogFragment;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;


/**
 * 自定义toast的基类
 * Created by Administrator on 2018/2/13 0013.
 */

public abstract class ToastView extends DialogFragment {

    /**
     * 没有设置dialog的弹出动画，这个是默认值
     */
    private static final int NO_SET_ANIMATION = -1;
    private Activity activity;
    private FrameLayout containerView;
    /**
     * 显示后，是否自动关闭，默认关闭
     */
    private boolean autoDismiss = true;
    /**
     * 延迟关闭的时间，默认1秒
     */
    private long dismissTime = 1000;
    private Handler dismissHandler;
    private Runnable delayDismissRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    public void setDismissTime(long dismissTime) {
        this.dismissTime = dismissTime;
    }

    public void setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;

    }


    @Override
    public void onStart() {
        super.onStart();
        /**
         *  如果采用onCreateView方式初始化Dialog，并未有使用自己创建的Dialog实例，
         *  因此这种方式产生的Dialog是DialogFragment内部自己创建的，创建时机是在DialogFragment源码中的onStart()中，
         *  而要设置Dialog的宽高，必须要Dialog初始化之后才能够生效，因此需要在super.onStart();调用之后设置。
         */
        Window window = getDialog().getWindow();
        if(window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //注意：这里传入的ViewGroup container为null
        windowConfigs();
        dismissHandler = new Handler();
        //Gravity.TOP这种情况在视图上方添加一个statusBar高度的margin，让状态栏露出来
        //这里就不再进行区分Gravity.BOTTOM情况下，手机上是否有虚拟导航键显示，如果要做处理，与Gravity.TOP类似
        if(Gravity.TOP == windowGravity()){
            //为了确保露出来的状态栏高度能够配置颜色，因此在最外加一层FrameLayout，通过设置FrameLayout的背景颜色就可以模拟状态栏改变颜色
            containerView = new FrameLayout(activity);
            containerView.setPadding(0,getStatusHeight(activity),0,0);
            inflater.inflate(toastViewLayout(), containerView,true);
            //
            ButterKnife.bind(this, containerView);
            //
            setupContentView();
            return containerView;
        }
        else {
            View toastView = inflater.inflate(toastViewLayout(),null);
            //
            ButterKnife.bind(this,toastView);
            //
            setupContentView();
            return toastView;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        delayDismissDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissHandler.removeCallbacks(delayDismissRunnable);
        dismissHandler = null;
    }

    /**
     * 设置状态栏的颜色
     * @param color
     */
    void setStatusBarMarginColor(int color){
        if(windowGravity() == Gravity.TOP){
            if (containerView != null) {
                containerView.setBackgroundColor(color);
            }
        }
    }


    /**
     * window的属性配置，这里在onCreateView中设置，不通过类似BaseDialogFragment方式在onCreateDialog配置，
     * 是想让整个ToastView的视图界面将状态栏填充，防止从屏幕顶部显示动画效果在移动到状态栏处就突然闪动消失
     */
    private void windowConfigs() {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        if(window != null){
            if(Build.VERSION.SDK_INT >= 19){
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //清理蒙板
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if(windowAnimationStyle() != NO_SET_ANIMATION){
                window.setWindowAnimations(windowAnimationStyle());
            }
            window.setGravity(windowGravity());
            windowOtherSettings(window);
        }
    }

    /**
     * window显示内容的Gravity
     * @return
     */
    public int windowGravity(){
        return Gravity.CENTER;
    }

    /**
     * 关于Window的其他配置，可以重写该方法
     * @param window
     */
    public void windowOtherSettings(Window window){

    }

    /**
     * 延迟相应时间关闭ToastView
     */
    private void delayDismissDialog(){
        if(autoDismiss){
            dismissHandler.postDelayed(delayDismissRunnable,dismissTime);
        }
    }

    /**
     * @return dialog的布局文件id
     */
    public abstract @LayoutRes int toastViewLayout();

    /**
     * 初始化dialog的控件，这里绑定了ButterKnife,直接使用@BindView
     */
    public abstract void setupContentView();

    /**
     * 重写该方法，可以修改dialog的弹出动画
     * 比如 ：
     * <style name="BottomSheetDialogStyle">
     *     <item name="android:windowEnterAnimation">@anim/bottom_sheet_enter</item>
     *     <item name="android:windowExitAnimation">@anim/bottom_sheet_exit</item>
     * </style>
     * @return 返回dialog弹出动画的style
     */
    protected int windowAnimationStyle(){
        return NO_SET_ANIMATION;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    private int getStatusHeight(Context context) {

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



}
