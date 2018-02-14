package org.gx.notes.grouplist;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by Administrator on 2017/11/23 0023.
 */
interface GroupItem {

    GroupItem itemBackground(@DrawableRes int drawable);
    GroupItem imageView(Drawable drawable);
    GroupItem imageView(@DrawableRes int drawable);

    GroupItem titleText(String text);
    GroupItem subTitleText(String text);
    GroupItem detailText(String text);

    GroupItem titleText(@StringRes int text);
    GroupItem subTitleText(@StringRes int text);
    GroupItem detailText(@StringRes int text);

    GroupItem titleTextSize(int unit, float size);
    GroupItem subTitleTextSize(int unit, float size);
    GroupItem detailTextSize(int unit, float size);

    GroupItem titleTextSize(float size);
    GroupItem subTitleTextSize(float size);
    GroupItem detailTextSize(float size);

    GroupItem titleTextColor(@ColorRes int color);
    GroupItem subTitleTextColor(@ColorRes int color);
    GroupItem detailTextColor(@ColorRes int color);

}
