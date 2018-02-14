package org.gx.notes.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import butterknife.ButterKnife;

/**
 * 使用DialogFragment创建Dialog的快捷基础类
 * Created by Administrator on 2018/2/11 0011.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    /**
     * 没有设置dialog的弹出动画，这个是默认值
     */
    private static final int NO_SET_ANIMATION = -1;

    protected Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = LayoutInflater.from(activity).inflate(dialogContentViewLayoutId(), null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog = builder.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(contentView);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            if(windowAnimationStyle() != NO_SET_ANIMATION){
                window.setWindowAnimations(windowAnimationStyle());
            }
            windowOtherSettings(window);
        }
        ButterKnife.bind(this, contentView);
        setupContentView();
        return dialog;
    }

    /**
     * 关于Window的其他配置
     * @param window
     */
    public void windowOtherSettings(Window window){

    }

    /**
     * @return dialog的布局文件id
     */
    public abstract @LayoutRes int dialogContentViewLayoutId();

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


}
