package org.gx.notes.navigation;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public interface Navigation {

    Navigation contentInset(int contentInset);
    Navigation titleText(String text);
    Navigation titleText(@StringRes int text);
    Navigation titleTextSize(int unit, int size);
    Navigation titleTextSize(int size);
    Navigation titleTextColor(@ColorRes int color);
    NavigationBar addItemInLeft(@NonNull View item);
    Navigation addItemInRight(@NonNull View item);

}
