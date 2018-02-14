package org.gx.notes.toast;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.gx.notes.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;

/**
 * 单独显示文字提示的ToastView，如果需要自定义其他的ToastView，可以参考这个
 * Created by Administrator on 2018/2/13 0013.
 */

public class MessageSheetToastView extends ToastView{

    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    @BindView(R.id.tv_message)
    TextView tvMessage;

    /**
     * 默认TOP
     */
    private @SheetDirection int sheetDirection = TOP;
    private Builder builder;

    public void setBuilder(Builder builder) {
        this.builder = builder;
        if (builder != null) {
            sheetDirection = builder.sheetDirection;
        }
    }

    @Override
    protected int windowAnimationStyle() {
        if(TOP == sheetDirection){
            return R.style.TopSheetAnimStyle;
        }
        else if(BOTTOM == sheetDirection){
            return R.style.BottomSheetAnimStyle;
        }

        else throw new IllegalArgumentException("MessageSheetToastView's sheetDirection must be MessageSheetToastView.TOP or MessageSheetToastView.BOTTOM");
    }

    @Override
    public int windowGravity() {
        if(TOP == sheetDirection){
            return Gravity.TOP;
        }
        else if(BOTTOM == sheetDirection){
            return Gravity.BOTTOM;
        }

        else throw new IllegalArgumentException("MessageSheetToastView's sheetDirection must be MessageSheetToastView.TOP or MessageSheetToastView.BOTTOM");
    }

    @Override
    public int toastViewLayout() {
        return R.layout.toast_single_message;
    }

    @Override
    public void setupContentView() {
        if (builder != null) {
            tvMessage.setText(builder.msg);
            tvMessage.setTextColor(builder.textColor);
            tvMessage.setBackgroundColor(builder.bgColor);
            tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP,builder.textSize);
            setStatusBarMarginColor(builder.statusBarColor);
            setDismissTime(builder.autoDismissTime);
        }
    }

    public static class Builder{
        private Context context;
        private int sheetDirection;
        private String msg;
        private int textColor = Color.WHITE;
        private int bgColor = Color.RED;
        private int statusBarColor = Color.TRANSPARENT;
        private int textSize = 17;
        private long autoDismissTime = 1000;

        public Builder(Context context){
            this.context = context;
        }

        public Builder message(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder sheetDirection(@SheetDirection int sheetDirection) {
            this.sheetDirection = sheetDirection;
            return this;
        }

        public Builder textColor(@ColorRes int color) {
            this.textColor = ContextCompat.getColor(context, color);
            return this;
        }

        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder bgColor(@ColorRes int color) {
            this.bgColor = ContextCompat.getColor(context, color);
            return this;
        }

        public Builder statusBarColor(@ColorRes int color) {
            this.statusBarColor = ContextCompat.getColor(context, color);
            return this;
        }

        public Builder autoDismissTime(long time) {
            this.autoDismissTime = time;
            return this;
        }

        public MessageSheetToastView build(){
            MessageSheetToastView messageSheetToastView = new MessageSheetToastView();
            messageSheetToastView.setBuilder(this);
            return messageSheetToastView;
        }
    }


    /**
     * 方向，这里只有上下两种种
     */
    @IntDef({TOP,BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @interface SheetDirection{

    }



}
