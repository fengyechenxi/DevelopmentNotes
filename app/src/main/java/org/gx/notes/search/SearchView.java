package org.gx.notes.search;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.gx.notes.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class SearchView extends LinearLayout {

    /**
     * 编辑模式
     */
    public static final int MODE_EDIT = 0;
    /**
     * 不可编辑，展示模式
     */
    public static final int MODE_DISPLAY = 1;

    private @SearchView.Mode int mCurMode = MODE_EDIT;

    private View mLlInput;
    private ImageView mIvSearch;
    private EditText mEtInput;

    public SearchView(Context context) {
        this(context,null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.search_view,this,true);
        mLlInput = findViewById(R.id.ll_input);
        mIvSearch = (ImageView) findViewById(R.id.iv_search);
        mEtInput = (EditText) findViewById(R.id.et_input);
        setGravity(Gravity.CENTER_VERTICAL);
        ViewCompat.setBackground(this, ContextCompat.getDrawable(context,R.drawable.bg_search_view));
    }

    /*********************背景配置************************/

    public SearchView background(@DrawableRes int drawable){
        ViewCompat.setBackground(this, ContextCompat.getDrawable(getContext(),drawable));
        return this;
    }


    /*******************搜索Icon********************/
    public SearchView searchIcon(@DrawableRes int drawable){
        mIvSearch.setImageResource(drawable);
        return this;
    }


    /*************************文本配置***************************/

    public SearchView textSize(float size){
        mEtInput.setTextSize(size);
        return this;
    }

    public SearchView textSize(int unit,float size){
        mEtInput.setTextSize(unit,size);
        return this;
    }

    public SearchView textColor(int color){
        mEtInput.setTextColor(color);
        return this;
    }

    public SearchView textColor(ColorStateList colors){
        mEtInput.setTextColor(colors);
        return this;
    }


    /**********************hint配置*********************/

    public SearchView defaultHintText(String hintText){
        mEtInput.setHint(hintText);
        return this;
    }

    public SearchView defaultHintText(@StringRes int hintText){
        mEtInput.setHint(hintText);
        return this;
    }


    public SearchView hintTextColor(int color){
        mEtInput.setHintTextColor(color);
        return this;
    }

    public SearchView hintTextColor(ColorStateList colors){
        mEtInput.setHintTextColor(colors);
        return this;
    }

    public void setHint(CharSequence hint){
        mEtInput.setHint(hint);
    }

    public void setHint(@StringRes int hint){
        mEtInput.setHint(hint);
    }

    /**********************切换模式**********************/
    public SearchView changeMode(@Mode int mode){

        if(mCurMode != mode){
            if(MODE_EDIT == mode){
                LayoutParams params = (LayoutParams) mLlInput.getLayoutParams();
                params.width = LayoutParams.MATCH_PARENT;
                params.height = LayoutParams.WRAP_CONTENT;
                mLlInput.setLayoutParams(params);
                setGravity(Gravity.CENTER_VERTICAL);
                mEtInput.setEnabled(true);
            }
            else if(MODE_DISPLAY == mode){
                LayoutParams params = (LayoutParams) mLlInput.getLayoutParams();
                params.width = LayoutParams.WRAP_CONTENT;
                params.height = LayoutParams.WRAP_CONTENT;
                mLlInput.setLayoutParams(params);
                setGravity(Gravity.CENTER);
                mEtInput.setEnabled(false);
            }
            else throw new IllegalStateException("mode must be type of SearchView.Mode");
        }

        mCurMode = mode;
        return this;
    }


    @IntDef({MODE_EDIT,MODE_DISPLAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode{

    }


}
