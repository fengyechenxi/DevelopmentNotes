package org.gx.notes.load.loading;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.gx.notes.load.loading.viewport.ShowingViewport;
import org.gx.notes.load.loading.viewport.SuccessViewport;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;


/**
 * ShowingViewport的状态容器视图
 * Created by Administrator on 2017/9/26 0026.
 */

public class LoadingLayout extends FrameLayout {

//    private ShowingViewport.OnReloadListener mOnReloadListener;
    private Context mContext;
    ArrayMap<Class<? extends ShowingViewport>,ShowingViewport> mViewports;
    /**
     * 通过TargetContext封装好的目标对象数据集合
     */
    private TargetContext mTargetContext;
    /**
     * 传入的目标对象，代表当前loading的目标是谁，这里只允许Activity，Fragment，View三种
     */
    private WeakReference<Object> mObjectWeakReference;


    public LoadingLayout(@NonNull TargetContext targetContext) {
        super(targetContext.getContext());
        mTargetContext = targetContext;
        this.mContext = targetContext.getContext();
        mViewports = new ArrayMap<>();
    }


    public void setObjectWeakReference(WeakReference<Object> objectWeakReference) {
        mObjectWeakReference = objectWeakReference;
    }

//    public void setOnReloadListener(ShowingViewport.OnReloadListener onReloadListener) {
//        mOnReloadListener = onReloadListener;
//        if(mViewports != null && mViewports.size() > 0){
//            Set<Class<? extends ShowingViewport>> set = mViewports.keySet();
//            for(Iterator<Class<? extends ShowingViewport>> it = set.iterator(); it.hasNext();){
//                ShowingViewport showingViewport = mViewports.get(it.next());
//                showingViewport.setOnReloadListener(onReloadListener);
//            }
//        }
//    }

    /**
     * 添加一个状态ShowingViewport视图
     * @param viewport
     */
    public void addShowingViewport(@NonNull ShowingViewport viewport){
//        ShowingViewport cloneViewport = viewport.copy();
        ShowingViewport cloneViewport = viewport;
        cloneViewport.init( mContext,null, cloneViewport.getOnReloadListener());
        Class<? extends ShowingViewport> cls = cloneViewport.getClass();
        if(!mViewports.containsKey(cls)){
            mViewports.put(cls,cloneViewport);
        }
    }

    /**
     * 保存ShowingViewport在mViewports中
     * @param successViewport
     */
    public void setSuccessViewport(ShowingViewport successViewport){
        Class<? extends ShowingViewport> cls = successViewport.getClass();
        if(!mViewports.containsKey(cls)){
            mViewports.put(cls,successViewport);
        }
    }


    /**
     * 用viewport替换当前的视图
     * @param viewport 要替换的视图
     */
    public void replaceWithViewport(Class<? extends ShowingViewport> viewport){
        if(!mViewports.containsKey(viewport)){
            throw new IllegalArgumentException(
                    String.format("The ShowingViewport (%s) is non-existent.", viewport.getSimpleName())
            );
        }
        if (isMainThread()) {
            showViewport(viewport);
        } else {
            postToMainThread(viewport);
        }
    }

    private void postToMainThread(final Class<? extends ShowingViewport> viewport) {
        post(new Runnable() {
            @Override
            public void run() {
                showViewport(viewport);
            }
        });
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 显示cls对应的视图
     * @param cls
     */
    private void showViewport(Class<? extends ShowingViewport> cls) {
        if(cls == SuccessViewport.class){
            ViewGroup parentView = mTargetContext.getParentView();
            if(mObjectWeakReference.get() != null && mObjectWeakReference.get() instanceof Activity) {
                setVisibility(View.GONE);
                mTargetContext.getOldContent().setVisibility(View.VISIBLE);
            }
            else {
                ShowingViewport successViewport = mViewports.get(SuccessViewport.class);
                if(parentView != null){
                    //将要显示加载成功的视图，也就是当前加载完成后正常显示的视图，因此需要将LoadingLayout从跟视图移除
                    parentView.removeView(this);
                    //先remove，防止多次重复调用SuccessViewport，addView时候抛出异常
                    ViewGroup viewGroup = (ViewGroup) successViewport.getRootView().getParent();
                    if(viewGroup != null){
                        viewGroup.removeView(successViewport.getRootView());
                    }
                    //remove以后，添加就不会出问题，这里添加，显示加载完成后要显示的视图
                    parentView.addView(successViewport.getRootView(),mTargetContext.getChildIndex(),mTargetContext.getOldContent().getLayoutParams());
                }
                else {
                    addView(successViewport.getRootView());
                }
            }

        }
        else {

            if (getChildCount() > 0) {
                removeAllViews();
            }

            for (Class key : mViewports.keySet()) {
                if (key == cls) {
                    if(mObjectWeakReference.get() != null && mObjectWeakReference.get() instanceof Activity){
                        setVisibility(View.VISIBLE);
                        mTargetContext.getOldContent().setVisibility(View.GONE);
                        addView(mViewports.get(key).getRootView());
                    }
                    else {

                        //将oldContent先从父View移除，防止当前oldContent已经添加在某个视图上，再次调用addView会抛出异常
                        ViewGroup viewGroup = (ViewGroup) mTargetContext.getOldContent().getParent();
                        if(viewGroup != null){
                            viewGroup.removeView(mTargetContext.getOldContent());
                        }
                        //将当前对象从parent移除，与上面oldContent同理
                        ViewGroup thisParent = (ViewGroup) getParent();
                        if (thisParent != null) {
                            thisParent.removeView(this);
                        }
                        //添加当前要显示的showViewport视图到LoadingLayout
                        addView(mViewports.get(key).getRootView());
                        //在跟视图上替换成当前要设置的LoadingLayout
                        ViewGroup parentView = mTargetContext.getParentView();
                        parentView.addView(this,mTargetContext.getOldContent().getLayoutParams());

                    }

                }
            }
        }


//        if(mObjectWeakReference.get() != null && mObjectWeakReference.get() instanceof Activity){
//
//            ViewGroup parentView = mTargetContext.getParentView();
//            if(cls == SuccessViewport.class){
//                parentView.removeView(this);
//                mTargetContext.getOldContent().setVisibility(View.VISIBLE);
//            }
//            else {
//                if (getChildCount() > 0) {
//                    removeAllViews();
//                }
//
//                for (Class key : mViewports.keySet()) {
//                    if (key == cls) {
//                        addView(mViewports.get(key).getRootView());
//                    }
//                }
//            }
//        }
//        else {
//
//            if(cls == SuccessViewport.class){
//                ViewGroup parentView = mTargetContext.getParentView();
//                ShowingViewport successViewport = mViewports.get(SuccessViewport.class);
//                if(parentView != null){
//                    parentView.removeView(this);
//                    parentView.addView(successViewport.getRootView(),mTargetContext.getChildIndex(),mTargetContext.getOldContent().getLayoutParams());
//                }
//                else addView(successViewport.getRootView());
//
//            }
//            else {
//                if (getChildCount() > 0) {
//                    removeAllViews();
//                }
//
//                for (Class key : mViewports.keySet()) {
//                    if (key == cls) {
//                        addView(mViewports.get(key).getRootView());
//                    }
//                }
//            }
//
//        }




    }




}
