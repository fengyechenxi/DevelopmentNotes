package org.gx.notes.enu;

import android.app.Activity;
import android.support.annotation.StringRes;

import org.gx.notes.R;
import org.gx.notes.bezier.BezierActivity;
import org.gx.notes.load.LoadingActivity;
import org.gx.notes.loadmore.LoadMoreRecyclerViewActivity;
import org.gx.notes.navigation.NavigationBarActivity;
import org.gx.notes.toast.ToastActivity;

/**
 * 知识点枚举类
 * Created by Administrator on 2018/2/11 0011.
 */

public enum  NotesKnowledgePoint {

    Bezier(BezierActivity.class, R.string.title_bezier,R.string.details_bezier),
    NavigationBar(NavigationBarActivity.class, R.string.title_navigation_bar,R.string.details_navigation_bar),
    LoadMoreRecyclerView(LoadMoreRecyclerViewActivity.class,R.string.title_load_more_recyclerview,R.string.details_load_more_recyclerview),
    Loading(LoadingActivity.class, R.string.title_loading,R.string.details_loading),
    Toast(ToastActivity.class, R.string.title_toast,R.string.details_toast)
    ;

    public Class<? extends Activity> cls;
    public int title;
    public int details;
    NotesKnowledgePoint(Class<? extends Activity> cls, @StringRes int title, @StringRes int details){
        this.cls = cls;
        this.title = title;
        this.details = details;
    }
}
