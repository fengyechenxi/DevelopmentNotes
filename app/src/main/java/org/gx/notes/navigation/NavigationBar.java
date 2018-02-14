package org.gx.notes.navigation;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.gx.notes.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 能够切换Android，IOS风格的简单导航栏
 * Created by Administrator on 2017/11/23 0023.
 */

public class NavigationBar extends RelativeLayout implements Navigation{

    public static final int ANDROID = 0;
    public static final int IOS = 1;
    private int mNavigationType = ANDROID;
    private LinearLayout mLeftContainerView;
    private TextView mTitleView;
    private LinearLayout mRightContainerView;
    private int mItemMargin;

    public NavigationBar(Context context) {
        this(context,null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.navigation_bar,this,true);
        mLeftContainerView = (LinearLayout)findViewById(R.id.gx_navigationBar_left_container);
        mTitleView = (TextView) findViewById(R.id.gx_navigationBar_titleTextView);
        mRightContainerView = (LinearLayout)findViewById(R.id.gx_navigationBar_right_container);
        int contentInset = getResources().getDimensionPixelSize(R.dimen.gx_navigationBar_contentInset);
        setPadding(contentInset,getPaddingTop(),contentInset,getPaddingBottom());
        mItemMargin = getResources().getDimensionPixelSize(R.dimen.gx_navigationBar_itemMargin);
        changeToAndroidStyle();
    }

    @Override
    public NavigationBar contentInset(int contentInset){
        setPadding(contentInset,getPaddingTop(),contentInset,getPaddingBottom());
        return this;
    }

    @Override
    public NavigationBar titleText(String text){
        mTitleView.setText(text);
        return this;
    }

    @Override
    public NavigationBar titleText(@StringRes int text){
        mTitleView.setText(text);
        return this;
    }

    @Override
    public NavigationBar titleTextSize(int unit,int size){
        mTitleView.setTextSize(unit,size);
        return this;
    }

    @Override
    public NavigationBar titleTextSize(int size){
        mTitleView.setTextSize(size);
        return this;
    }

    @Override
    public NavigationBar titleTextColor(@ColorRes int color){
        mTitleView.setTextColor(ContextCompat.getColor(getContext(),color));
        return this;
    }

    @Override
    public NavigationBar addItemInLeft(@NonNull View item){
        LinearLayout.LayoutParams params = getItemLayoutParams();
        item.setLayoutParams(params);
        mLeftContainerView.addView(item);
        return this;
    }

    @Override
    public NavigationBar addItemInRight(@NonNull View item){
        LinearLayout.LayoutParams params = getItemLayoutParams();
        params.leftMargin = mItemMargin;
        item.setLayoutParams(params);
        mRightContainerView.addView(item);
        return this;
    }

    private LinearLayout.LayoutParams getItemLayoutParams(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void changeToAndroidStyle(){
        LayoutParams titleParams = (LayoutParams) mTitleView.getLayoutParams();
        titleParams.addRule(RelativeLayout.RIGHT_OF,mLeftContainerView.getId());
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,0);
        mTitleView.setLayoutParams(titleParams);
    }

    private void changeToIosStyle(){
        LayoutParams titleParams = (LayoutParams) mTitleView.getLayoutParams();
        titleParams.addRule(RelativeLayout.RIGHT_OF,0);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTitleView.setLayoutParams(titleParams);
    }

    private void setAllViewVisibility(int visibility){
        mLeftContainerView.setVisibility(visibility);
        mTitleView.setVisibility(visibility);
        mRightContainerView.setVisibility(visibility);
    }


    public void changeNavigationType(@NavigationType int type){
        if (mNavigationType == type)return;
        mNavigationType = type;
        switch (mNavigationType){
            case ANDROID:
                changeToAndroidStyle();
                break;
            case IOS:
                changeToIosStyle();
                break;
        }
    }

    @IntDef({ANDROID,IOS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigationType{

    }
}
