package org.gx.notes.load.loading.viewport;

import android.content.Context;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * 使用该类必须要继承，加载过程中不同的状态，比如错误提示页面，加载中，数据为空，都应该继承该类，
 * 并将具体显示的UI视图，通过构造方法传入，该类支持点击重试，可以是当前整个界面点击重试刷新，
 * 也可以是具体某个按钮点击刷新，具体的逻辑重写抽象方法initRetry。
 * 重写抽象方法onCreateView，里面返回是一个layout资源id，用来表示当前定义的状态ShowingViewport视图的界面
 * Created by Administrator on 2017/9/26 0026.
 */

public abstract class ShowingViewport implements Serializable {
    private static final long serialVersionUID = 4415260470501529556L;
    private View mRootView;
    /**
     * Context不能参与序列化，否则ShowingViewport整个对象无法序列化，
     * ShowingViewport对象需要序列化，他内部的对象也需要序列化，不能够让Context参加序列化。
     * 通过序列化深克隆已经弃用，目前不支持全局配置，因此Context不再需要加上transient修饰。
     */
    private Context mContext;
    private OnReloadListener mOnReloadListener;

    public ShowingViewport() {
    }

    ShowingViewport(Context context,View view, OnReloadListener onReloadListener) {
        this.mRootView = view;
        this.mContext = context;
        this.mOnReloadListener = onReloadListener;
    }

    /**
     * @param context
     * @param view
     * @param onReloadListener
     */
    public void init(Context context, View view, OnReloadListener onReloadListener) {
        this.mRootView = view;
        this.mContext = context;
        this.mOnReloadListener = onReloadListener;
    }


    /**
     * 深克隆（深拷贝），也可以实现Cloneable接口，然后重写clone方法，
     * 这里用的序列化方式实现。
     * 该序列化，只是拷贝对象本身
     * @return
     */
//    public ShowingViewport copy() {
//        ByteArrayOutputStream bao = new ByteArrayOutputStream();
//        ObjectOutputStream oos;
//        try {
//            oos = new ObjectOutputStream(bao);
//            oos.writeObject(this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ByteArrayInputStream bis = new ByteArrayInputStream(bao.toByteArray());
//        ObjectInputStream ois;
//        Object obj = null;
//        try {
//            ois = new ObjectInputStream(bis);
//            obj = ois.readObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return (ShowingViewport) obj;
//    }


    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.mOnReloadListener = onReloadListener;
    }

    public OnReloadListener getOnReloadListener() {
        return mOnReloadListener;
    }

    /**
     * 获取当前ShowingViewport的视图View，如果没有就通过onCreateView传入的layout资源id加载
     * @return
     */
    public View getRootView() {
        int resId = onCreateView();
        if (resId == 0 && mRootView != null) {
            return mRootView;
        }
        mRootView = View.inflate(mContext, onCreateView(), null);
        initRetry(mContext,mRootView);
        return mRootView;
    }

    /**
     * 监听重新加载的方法，直接调用即可
     * @param view
     */
    public void onReload(View view){
        if(mOnReloadListener != null){
            mOnReloadListener.onReload(view);
        }
    }


    public interface OnReloadListener {
        void onReload(View v);
    }

    /**
     *
     * @return 返回当前ShowingViewport的视图layout资源id
     */
    protected abstract int onCreateView();

    /**
     * ShowingViewport视图中一些具体的控件初始化可在该方法中设置，
     * 并且可以在该方法中设置重新加载得到点击事件
     * @param context
     * @param view
     */
    protected abstract void initRetry(Context context, View view);
}
