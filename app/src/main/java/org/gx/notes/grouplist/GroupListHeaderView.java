package org.gx.notes.grouplist;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Created by Administrator on 2017/10/9 0009.
 */
class GroupListHeaderView extends AppCompatTextView {
    public GroupListHeaderView(Context context) {
        this(context,null);
    }

    public GroupListHeaderView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public GroupListHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.END);
    }






}
