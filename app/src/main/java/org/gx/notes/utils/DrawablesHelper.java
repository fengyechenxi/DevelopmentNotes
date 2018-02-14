package org.gx.notes.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

/**
 * TextView及其子类设置CompoundDrawables助手
 */
public class DrawablesHelper {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;


    private int mDirection = LEFT;
    private WeakReference<TextView> mViewWeakReference;
    private int mWidth;
    private int mHeight;
    private @DrawableRes
    int mDrawable;

    public DrawablesHelper(TextView view){
        mViewWeakReference = new WeakReference<>(view);
    }

    public DrawablesHelper direction(int direction){
        mDirection = direction;
        return this;
    }

    public DrawablesHelper drawableWidthPx(int width){
        mWidth = width;
        return this;
    }

    public DrawablesHelper drawableHeightPx(int height){
        mHeight = height;
        return this;
    }

    public DrawablesHelper drawable(@DrawableRes int drawable){
        mDrawable = drawable;
        return this;
    }

    public void commit(){
        TextView textView = mViewWeakReference.get();
        if(textView != null){
            Drawable drawable;
            if(mWidth != 0 && mHeight != 0){
                drawable = getDrawable(textView.getContext(), mDrawable, mWidth, mHeight);
            }
            else drawable = getDrawable(textView.getContext(), mDrawable);

            Drawable[] compoundDrawables = textView.getCompoundDrawables();
            switch (mDirection){
                case LEFT:
                    leftDrawable(textView,drawable,compoundDrawables);
                    break;
                case RIGHT:
                    rightDrawable(textView,drawable,compoundDrawables);
                    break;
                case TOP:
                    topDrawable(textView,drawable,compoundDrawables);
                    break;
                case BOTTOM:
                    bottomDrawable(textView,drawable,compoundDrawables);
                    break;
            }
        }
    }

    private DrawablesHelper leftDrawable(TextView textView, Drawable drawable, Drawable[] compoundDrawables){
        textView.setCompoundDrawables(drawable,compoundDrawables[1],compoundDrawables[2],compoundDrawables[3]);
        return this;
    }
    private DrawablesHelper rightDrawable(TextView textView, Drawable drawable, Drawable[] compoundDrawables){
        textView.setCompoundDrawables(compoundDrawables[0],compoundDrawables[1],drawable,compoundDrawables[3]);
        return this;
    }

    private DrawablesHelper topDrawable(TextView textView, Drawable drawable, Drawable[] compoundDrawables){
        textView.setCompoundDrawables(compoundDrawables[0],drawable,compoundDrawables[2],compoundDrawables[3]);
        return this;
    }

    private DrawablesHelper bottomDrawable(TextView textView, Drawable drawable, Drawable[] compoundDrawables){
        textView.setCompoundDrawables(compoundDrawables[0],compoundDrawables[1],compoundDrawables[2],drawable);
        return this;
    }

    /**
     * 获取drawable
     *
     * @param context
     * @param drawableResId
     * @param drawablePxWidth
     * @param drawablePxHeight
     * @return
     */
    private static Drawable getDrawable(Context context, int drawableResId, int drawablePxWidth, int drawablePxHeight) {
        Drawable drawable = ContextCompat.getDrawable(context,drawableResId);
        drawable.setBounds(0, 0, drawablePxWidth, drawablePxHeight);
        return drawable;
    }

    private static Drawable getDrawable(Context context, int drawableResId) {
        Drawable drawable = ContextCompat.getDrawable(context,drawableResId);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        return drawable;
    }


}
