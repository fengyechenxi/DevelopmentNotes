package org.gx.notes.grouplist;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import org.gx.notes.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/10/9 0009.
 */

public class GroupListView extends LinearLayout {

    public static final String TAG = "GroupListView";


    public static final int SEPARATOR_STYLE_NORMAL = 0;
    public static final int SEPARATOR_STYLE_NONE = 1;
    private int mSeparatorStyle = SEPARATOR_STYLE_NORMAL;
    private SparseArray<Section> mSections;

    public GroupListView(Context context) {
        this(context,null);
    }

    public GroupListView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public GroupListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }


    protected void init(Context context, AttributeSet attrs, int defStyleAttr){
        mSections = new SparseArray<Section>();
        setOrientation(VERTICAL);
    }

    public void setSeparatorStyle(@SeparatorStyle int separatorStyle) {
        mSeparatorStyle = separatorStyle;
    }

    private void addSection(Section section){
        mSections.append(mSections.size(),section);
    }

    private void removeSection(Section section) {
        for (int i = 0; i < mSections.size(); i++) {
            Section each = mSections.valueAt(i);
            if (each == section) {
                mSections.remove(i);
            }
        }
    }
    public Section getSection(int index) {
        return mSections.get(index);
    }

    public @SeparatorStyle int getSeparatorStyle() {
        return mSeparatorStyle;
    }

    public static class Section{

        private SparseArray<View> mItemViews;
        private Context mContext;
        private String mTitle = "";
        private int mHeaderHeight;
        private int mHeaderTextSize;
        private int mUnit = TypedValue.COMPLEX_UNIT_PX;
        private int mHeaderTextColor;
        private int mPadding;

        private int mSeparatorDrawableForSingle = 0;
        private int mSeparatorDrawableForTop = 0;
        private int mSeparatorDrawableForBottom = 0;
        private int mSeparatorDrawableForMiddle = 0;

        public Section(Context context){
            mContext = context;
            mItemViews = new SparseArray<View>();
            mPadding = context.getResources().getDimensionPixelOffset(R.dimen.gx_groupList_item_horizontalPadding);
            mHeaderHeight = context.getResources().getDimensionPixelOffset(R.dimen.gx_groupList_item_headerHeight);
            mHeaderTextSize = (int) context.getResources().getDimension(R.dimen.gx_groupList_item_header_textSize);
            mHeaderTextColor = ContextCompat.getColor(mContext,R.color.gx_groupList_item_header_textColor);
        }

        public Section headerTitle(String title){
            mTitle = title;
            return this;
        }

        public Section headerTextSize(int size){
            mHeaderTextSize = size;
            return this;
        }

        public Section headerTextSize(int unit,int size){
            mUnit = unit;
            mHeaderTextSize = size;
            return this;
        }

        public Section headerTextColor(@ColorRes int color){
            mHeaderTextColor = ContextCompat.getColor(mContext, color);
            return this;
        }

        public Section headerHeight(int height){
            mHeaderHeight = height;
            return this;
        }

        public Section itemPadding(int padding){
            mPadding = padding;
            return this;
        }

        public Section addItemView(GroupItem itemView){
            mItemViews.append(mItemViews.size(), (View) itemView);
            return this;
        }

        public void attachToGroupListView(GroupListView groupListView){

            attachHeaderView(groupListView);

            if (groupListView.getSeparatorStyle() == SEPARATOR_STYLE_NORMAL) {
                if (mSeparatorDrawableForSingle == 0) {
                    mSeparatorDrawableForSingle = R.drawable.gx_s_item_bg_double_border;
                }
                if (mSeparatorDrawableForTop == 0) {
                    mSeparatorDrawableForTop = R.drawable.gx_s_item_bg_double_border;
                }

                if (mSeparatorDrawableForBottom == 0) {
                    mSeparatorDrawableForBottom = R.drawable.gx_s_list_item_bg_bottom_border;
                }

                if (mSeparatorDrawableForMiddle == 0) {
                    mSeparatorDrawableForMiddle = R.drawable.gx_s_list_item_bg_bottom_border;
                }
            }

            int itemCount = mItemViews.size();
            for(int i=0;i<itemCount;i++){
                View itemView = mItemViews.get(i);
                int resDrawableId;
                if (groupListView.getSeparatorStyle() == SEPARATOR_STYLE_NORMAL) {
                    if (itemCount == 1) {
                        resDrawableId = mSeparatorDrawableForSingle;
                    } else if (i == 0) {
                        resDrawableId = mSeparatorDrawableForTop;
                    } else if (i == itemCount - 1) {
                        resDrawableId = mSeparatorDrawableForBottom;
                    } else {
                        resDrawableId = mSeparatorDrawableForMiddle;
                    }
                } else {
                    resDrawableId = R.drawable.gx_s_list_item_bg_none_border;
                }

                ViewCompat.setBackground(itemView, ContextCompat.getDrawable(mContext,resDrawableId));
                int[] padding = new int[]{mPadding, itemView.getPaddingTop(), mPadding, itemView.getPaddingBottom()};
                itemView.setPadding(padding[0], padding[1], padding[2], padding[3]);
                groupListView.addView(itemView);
            }

            groupListView.addSection(this);

        }

        private void attachHeaderView(GroupListView groupListView){
            GroupListHeaderView headerView = new GroupListHeaderView(mContext);
            headerView.setTextSize(mUnit,mHeaderTextSize);
            headerView.setTextColor(mHeaderTextColor);
            headerView.setPadding(mPadding, headerView.getPaddingTop(), mPadding, headerView.getPaddingBottom());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,mHeaderHeight);
            headerView.setLayoutParams(params);
            if(!TextUtils.isEmpty(mTitle)){
                headerView.setText(mTitle);
            }
            groupListView.addView(headerView);
        }


    }



    @IntDef({SEPARATOR_STYLE_NORMAL, SEPARATOR_STYLE_NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SeparatorStyle {
    }
}
