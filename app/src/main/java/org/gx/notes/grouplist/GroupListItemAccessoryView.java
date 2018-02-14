package org.gx.notes.grouplist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.gx.notes.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class GroupListItemAccessoryView extends LinearLayout implements GroupItem{

    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mDetailView;
    private ViewGroup mAccessoryView;
    protected CheckBox mSwitchView;
    protected ImageView mArrowView;


    public GroupListItemAccessoryView(Context context) {
        this(context,null);
    }

    public GroupListItemAccessoryView(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public GroupListItemAccessoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.gx_grouplist_item_view, this, true);
        initWidget();

        setMinimumHeight(getResources().getDimensionPixelSize(R.dimen.gx_groupList_item_minHeight));
        setGravity(Gravity.CENTER_VERTICAL);

        Drawable drawable  = ContextCompat.getDrawable(context,R.drawable.gx_s_item_bg_double_border);
        if(Build.VERSION.SDK_INT >= 16){
            setBackground(drawable);
        }
        else setBackgroundDrawable(drawable);
    }

    private void initWidget(){
        mImageView = (ImageView) findViewById(R.id.gx_groupList_item_imageView);
        mTitleView = (TextView) findViewById(R.id.gx_groupList_item_titleTextView);
        mSubTitleView = (TextView) findViewById(R.id.gx_groupList_item_subTitleTextView);
        mDetailView = (TextView) findViewById(R.id.gx_groupList_item_detailTextView);
        mAccessoryView = (ViewGroup) findViewById(R.id.gx_groupList_item_accessoryView);
    }


    public GroupListItemAccessoryView accessoryArrow(){
        setAccessoryType(ACCESSORY_TYPE_ARROW);
        return this;
    }

    public GroupListItemAccessoryView accessoryArrow(Drawable drawable){
        setAccessoryType(ACCESSORY_TYPE_ARROW);
        arrowDrawable(drawable);
        return this;
    }

    public GroupListItemAccessoryView accessoryArrow(@DrawableRes int drawable){
        setAccessoryType(ACCESSORY_TYPE_ARROW);
        arrowDrawable(drawable);
        return this;
    }

    public GroupListItemAccessoryView accessorySwitch(){
        setAccessoryType(ACCESSORY_TYPE_SWITCH);
        return this;
    }

    public GroupListItemAccessoryView accessorySwitch(@DrawableRes int drawable){
        setAccessoryType(ACCESSORY_TYPE_SWITCH);
        switchDrawable(drawable);
        return this;
    }

    private void arrowDrawable(Drawable drawable) {
        mArrowView.setImageDrawable(drawable);
    }

    private void arrowDrawable(@DrawableRes int drawable) {
        mArrowView.setImageResource(drawable);
    }

    private void switchDrawable(@DrawableRes int drawable) {
        mSwitchView.setButtonDrawable(drawable);
    }


    @Override
    public GroupListItemAccessoryView itemBackground(@DrawableRes int drawable) {
        ViewCompat.setBackground(this, ContextCompat.getDrawable(getContext(), drawable));
        return this;
    }

    @Override
    public GroupListItemAccessoryView imageView(@NonNull Drawable drawable) {
        mImageView.setImageDrawable(drawable);
        return this;
    }

    @Override
    public GroupListItemAccessoryView imageView(@DrawableRes int drawable) {
        mImageView.setImageResource(drawable);
        return this;
    }


    @Override
    public GroupListItemAccessoryView titleText(String text) {
        mTitleView.setText(text);
        return this;
    }

    @Override
    public GroupListItemAccessoryView subTitleText(String text) {
        if(!TextUtils.isEmpty(text)){
            mSubTitleView.setText(text);
            mSubTitleView.setVisibility(VISIBLE);
        }
        else mSubTitleView.setVisibility(GONE);

        return this;
    }

    @Override
    public GroupListItemAccessoryView detailText(String text) {
        mDetailView.setText(text);
        return this;
    }

    @Override
    public GroupListItemAccessoryView titleText(@StringRes int text) {
        mTitleView.setText(text);
        return this;
    }

    @Override
    public GroupListItemAccessoryView subTitleText(@StringRes int text) {
        String str = getResources().getString(text);
       return subTitleText(str);
    }

    @Override
    public GroupListItemAccessoryView detailText(@StringRes int text) {
        mDetailView.setText(text);
        return this;
    }

    @Override
    public GroupListItemAccessoryView titleTextSize(int unit, float size) {
        mTitleView.setTextSize(unit,size);
        return this;
    }

    @Override
    public GroupListItemAccessoryView subTitleTextSize(int unit, float size) {
        mSubTitleView.setTextSize(unit,size);
        return this;
    }

    @Override
    public GroupListItemAccessoryView detailTextSize(int unit, float size) {
        mDetailView.setTextSize(unit,size);
        return this;
    }

    @Override
    public GroupListItemAccessoryView titleTextSize(float size) {
        mTitleView.setTextSize(size);
        return this;
    }

    @Override
    public GroupListItemAccessoryView subTitleTextSize(float size) {
        mSubTitleView.setTextSize(size);
        return this;
    }

    @Override
    public GroupListItemAccessoryView detailTextSize(float size) {
        mDetailView.setTextSize(size);
        return this;
    }

    @Override
    public GroupListItemAccessoryView titleTextColor(@ColorRes int color) {
        mTitleView.setTextColor(ContextCompat.getColor(getContext(), color));
        return this;
    }

    @Override
    public GroupListItemAccessoryView subTitleTextColor(@ColorRes int color) {
        mSubTitleView.setTextColor(ContextCompat.getColor(getContext(), color));
        return this;
    }

    @Override
    public GroupListItemAccessoryView detailTextColor(@ColorRes int color) {
        mDetailView.setTextColor(ContextCompat.getColor(getContext(), color));
        return this;
    }


    private ViewGroup.LayoutParams getAccessoryLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    private void setAccessoryType(@AccessoryViewType int type){
        mAccessoryView.removeAllViews();
        switch (type){
            case ACCESSORY_TYPE_ARROW:
                if(mArrowView == null){
                    mArrowView = new ImageView(getContext());
                    mArrowView.setLayoutParams(getAccessoryLayoutParams());
                }
                mAccessoryView.addView(mArrowView);
                break;
            case ACCESSORY_TYPE_SWITCH:
                if(mSwitchView == null){
                    mSwitchView = new CheckBox(getContext());
                    mSwitchView.setLayoutParams(getAccessoryLayoutParams());
                    // disable掉且不可点击，然后通过整个item的点击事件来toggle开关的状态
                    mSwitchView.setClickable(false);
                }
                mAccessoryView.addView(mSwitchView);
                break;
        }

    }

    public void toggleSwitch(){
        if(mSwitchView != null){
            mSwitchView.toggle();
        }
    }


//    /**
//     * 右侧不显示任何东西
//     */
//    public final static int ACCESSORY_TYPE_NONE = 0;
    /**
     * 右侧显示一个箭头
     */
    public final static int ACCESSORY_TYPE_ARROW = 1;
    /**
     * 右侧显示一个开关
     */
    public final static int ACCESSORY_TYPE_SWITCH = 2;
//    /**
//     * 自定义右侧显示的 View
//     */
//    public final static int ACCESSORY_TYPE_CUSTOM = 3;

//    @IntDef({ACCESSORY_TYPE_NONE, ACCESSORY_TYPE_ARROW, ACCESSORY_TYPE_SWITCH, ACCESSORY_TYPE_CUSTOM})
    @IntDef({ACCESSORY_TYPE_ARROW, ACCESSORY_TYPE_SWITCH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AccessoryViewType{}


}
