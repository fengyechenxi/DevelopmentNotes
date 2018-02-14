package org.gx.notes.navigation;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.gx.notes.utils.DrawablesHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tab导航
 * Created by Administrator on 2017/11/24 0024.
 */

public class TabBar extends LinearLayout implements View.OnClickListener{

    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private SparseArray<Tab> mTabSparseArray = new SparseArray<>();
    private @DrawableDirection int mDirection = TOP;
    private View mCheckedButton;

    private OnTabSelectListener mOnTabSelectListener;
    private OnTabRepeatClickListener mOnTabRepeatClickListener;

    public TabBar(Context context) {
        super(context);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public static Tab newTab(Context context){
        return new Tab(context);
    }

    public void clearAllTabs(){
        mTabSparseArray.clear();
        removeAllViews();
    }


    public TabBar addTab(Tab tab,int index){
        mTabSparseArray.put(index,tab);
        return this;
    }
    public TabBar direction(@DrawableDirection int direction){
        mDirection = direction;
        return this;
    }

    public TabBar setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        if(onTabSelectListener != null){
            mOnTabSelectListener = onTabSelectListener;
        }
        return this;
    }

    public TabBar setOnTabRepeatClickListener(OnTabRepeatClickListener onTabRepeatClickListener) {
        mOnTabRepeatClickListener = onTabRepeatClickListener;
        return this;
    }

    /**
     * 添加tab之后必须调用此方法，否则check方法不会起作用
     */
    public void commit(){
        removeAllViews();
        setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<mTabSparseArray.size();i++){
            TextView button = createTabButton(mTabSparseArray.get(i));
            button.setId(i);
            button.setOnClickListener(this);
            addView(button,i,getButtonLayoutParams());
        }

    }


    private TextView createTabButton(Tab tab){
        TextView button = new TextView(getContext());
        button.setGravity(Gravity.CENTER);
        button.setPadding(0,tab.mTabPadding,0,tab.mTabPadding);
        button.setCompoundDrawablePadding(0);
        if(tab.mShowTitle){
            button.setText(tab.mTitle);
        }
        button.setTextSize(tab.mUnit,tab.mTitleTextSize);
        button.setTextColor(ContextCompat.getColorStateList(button.getContext(),tab.mSColor));
        if(tab.mBgDrawable == -1){
            ViewCompat.setBackground(button,null);
        }
        else ViewCompat.setBackground(button, ContextCompat.getDrawable(getContext(),tab.mBgDrawable));
        switch (mDirection){
            case TOP:
                new DrawablesHelper(button)
                        .direction(DrawablesHelper.TOP)
                        .drawable(tab.mIconDrawable)
                        .commit();
                break;
            case BOTTOM:
                new DrawablesHelper(button)
                        .direction(DrawablesHelper.BOTTOM)
                        .drawable(tab.mIconDrawable)
                        .commit();
                break;
            case LEFT:
                new DrawablesHelper(button)
                        .direction(DrawablesHelper.LEFT)
                        .drawable(tab.mIconDrawable)
                        .commit();
                break;
            case RIGHT:
                new DrawablesHelper(button)
                        .direction(DrawablesHelper.RIGHT)
                        .drawable(tab.mIconDrawable)
                        .commit();
                break;
            default:
                new DrawablesHelper(button)
                        .direction(DrawablesHelper.TOP)
                        .drawable(tab.mIconDrawable)
                        .commit();
        }

        return button;
    }

    private LayoutParams getButtonLayoutParams(){
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER_VERTICAL;
        return params;
    }

    @Override
    public void onClick(View view) {

        if(view == mCheckedButton){
            //重复点击已经选择的Tab，回调重复点击
            if(mOnTabRepeatClickListener != null){
                mOnTabRepeatClickListener.onTabRepeatClick((TextView) view,view.getId());
            }
            return;
        }
        if(mCheckedButton != null){
            mCheckedButton.setSelected(false);
        }
        view.setSelected(true);
        mCheckedButton = view;
        if(mOnTabSelectListener != null){
            mOnTabSelectListener.onTabSelected((TextView) view,view.getId());
        }
    }

    public void checked(int index){
        View child = getChildAt(index);
        if(child != null){
            child.performClick();
        }
    }


    public static class Tab{

        private Context mContext;
        private boolean mShowTitle = true;
        private String mTitle;
        private @ColorRes
        int mSColor = android.R.color.black;
        private @DrawableRes
        int mIconDrawable;
        private @DrawableRes
        int mBgDrawable = -1;
        private int mTabPadding;
        private int mTitleTextSize;
        private int mUnit = TypedValue.COMPLEX_UNIT_SP;

        public Tab(Context context){
            mContext = context;
        }

        public Tab showTitle(boolean showTitle){
            mShowTitle = showTitle;
            return this;
        }

        public Tab title(String text){
            mTitle = text;
            return this;
        }

        public Tab title(@StringRes int text){
            mTitle = mContext.getResources().getString(text);
            return this;
        }

        public Tab titleTextSize(int size){
            mTitleTextSize = size;
            return this;
        }

        public Tab titleTextSize(int unit,int size){
            mUnit = unit;
            mTitleTextSize = size;
            return this;
        }

        public Tab tabPadding(int padding){
            mTabPadding = padding;
            return this;
        }

        public Tab titleColor(@ColorRes int sColor){
            mSColor = sColor;
            return this;
        }

        public Tab icon(@DrawableRes int sDrawable){
            mIconDrawable = sDrawable;
            return this;
        }

        public Tab background(@DrawableRes int sDrawable){
            mBgDrawable = sDrawable;
            return this;
        }

    }


    @IntDef({TOP,BOTTOM,LEFT, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DrawableDirection{

    }

    public interface OnTabSelectListener{
        void onTabSelected(TextView tab, int index);
    }

    public interface OnTabRepeatClickListener{
        void onTabRepeatClick(TextView tab, int index);
    }
}
