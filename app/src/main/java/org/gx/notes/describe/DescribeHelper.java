package org.gx.notes.describe;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/23 0023.
 * 用于引导功能提示的遮罩层，
 * 该类只适用于功能界面View与上面悬浮的提示View相同大小，
 * 否则无法准确显示
 */

public class DescribeHelper {

    Map<View,View> mViews;
    Activity mContext;
    private String mLayerColor = "#99000000";
    private final FrameLayout mLayerView;
    private View.OnClickListener mListener;

    public DescribeHelper(Activity context){
        mViews = new HashMap<>();
        mContext = context;
        mLayerView = new FrameLayout(mContext);
    }

    /**
     *
     * @param matchView 界面视图上要匹配的那个View
     * @param resourceView 遮罩层上面对于的提示View
     * @return
     */
    public DescribeHelper addMatchView(View matchView, View resourceView){
        mViews.put(resourceView,matchView);
        return this;
    }

    /**
     *设置遮罩层的透明颜色
     * @param color 遮罩层的颜色，通常带有透明度
     * @return
     */
    public DescribeHelper layerBackground(String color){
        mLayerColor = color;
        return this;
    }

    /**
     * 整个遮罩层的点击事件
     * @param onClickListener
     * @return
     */
    public DescribeHelper setLayerOnClickListener(View.OnClickListener onClickListener){
        mListener = onClickListener;
        return this;
    }

    public void build(){
        final Window window = mContext.getWindow();
        window.getDecorView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(Build.VERSION.SDK_INT >= 16){
                            window.getDecorView()
                                    .getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        }
                        else window.getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        ViewGroup decorView = (ViewGroup)(window.getDecorView());

                        for(Map.Entry<View, View> entry: mViews.entrySet()) {
                            View resourceView = entry.getKey();
                            Rect rect = new Rect();
                            resourceView.getGlobalVisibleRect(rect);

                            View matchView = entry.getValue();
                            int height = FrameLayout.LayoutParams.WRAP_CONTENT;
                            int width = FrameLayout.LayoutParams.WRAP_CONTENT;
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,height);
                            mLayerView.addView(matchView,params);
                            matchView.setTranslationY(rect.top);
                            matchView.setTranslationX(rect.left);
                        }

                        mLayerView.setBackgroundColor(Color.parseColor(mLayerColor));
                        mLayerView.setOnClickListener(mListener);
                        decorView.addView(mLayerView);

                    }
                });

    }

    /**
     * 销毁整个遮罩层
     */
    public void destroyLayer(){
        ((ViewGroup)(mContext.getWindow().getDecorView())).removeView(mLayerView);
    }

    /**
     * 销毁遮罩层上面某个View，比如遮罩层上有很多View，
     * 但是点击某个View之后就需要消失，这时候可以调用该方法
     * @param view 要销毁的View
     */
    public void destroyView(View view){
        if(mLayerView != null && view != null){
            mLayerView.removeView(view);
        }
    }


}
