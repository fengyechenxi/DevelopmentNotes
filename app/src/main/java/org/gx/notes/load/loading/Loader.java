package org.gx.notes.load.loading;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.gx.notes.load.loading.viewport.ShowingViewport;
import org.gx.notes.load.loading.viewport.SuccessViewport;
import java.lang.ref.WeakReference;

/**
 * loading事件构造类
 * Created by Administrator on 2017/9/26 0026.
 */

public final class Loader {

    private Builder mBuilder;

    private Loader(Builder builder){
        mBuilder = builder;
    }

    public int getKey(){
        return mBuilder.getTarget().hashCode();
    }

    public void showViewport(Class<? extends ShowingViewport> cls){
        mBuilder.showViewport(cls);
    }

    public LoadingLayout getLoadLayout(){
        return mBuilder.getLoadingLayout();
    }

    public void clear(){
        mBuilder.clearReference();
    }

    /**
     * 数据构建类
     */
    public static class Builder{

        private WeakReference<Object> mObjectWeakReference;
        private LoadingLayout mLoadingLayout;
        private TargetContext mTargetContext;
        private Class<? extends ShowingViewport> mDefaultViewportClass;

        public Builder(View view){
            mObjectWeakReference = new WeakReference<>((Object)view);
            mTargetContext = getTargetContext(view);
            mLoadingLayout = new LoadingLayout(mTargetContext);
            mLoadingLayout.setObjectWeakReference(mObjectWeakReference);
        }

        public Builder(Activity activity){
            mObjectWeakReference = new WeakReference<>((Object) activity);
            mTargetContext = getTargetContext(activity);
            mLoadingLayout = new LoadingLayout(mTargetContext);
            mLoadingLayout.setObjectWeakReference(mObjectWeakReference);
        }

        public Builder(SupportLoadingFragment fragment){
            mObjectWeakReference = new WeakReference<>((Object)fragment);
            mTargetContext = getTargetContext(fragment);
            mLoadingLayout = new LoadingLayout(mTargetContext);
            mLoadingLayout.setObjectWeakReference(mObjectWeakReference);
        }

        public void clearReference(){
            mLoadingLayout = null;
            mObjectWeakReference.clear();
        }

        public Builder addViewport(ShowingViewport viewport){
            if(viewport != null){
                mLoadingLayout.addShowingViewport(viewport);
            }
            return this;
        }


        public Builder setDefaultViewport(Class<? extends ShowingViewport> defaultViewportClass){
            mDefaultViewportClass = defaultViewportClass;
            return this;
        }

//        public Builder setOnReloadListener(ShowingViewport.OnReloadListener onReloadListener){
//            mLoadingLayout.setOnReloadListener(onReloadListener);
//            return this;
//        }

        public Object getTarget() {
            return mObjectWeakReference.get();
        }

        public Loader build(){
            mLoadingLayout.setSuccessViewport(
                    new SuccessViewport(
                            mTargetContext.getContext(),
                            mTargetContext.getOldContent(),
                            null
                    )
            );
            if (mTargetContext.getParentView() != null) {//不为空时，替换的target为Activity和xml中的View

                if(mObjectWeakReference.get() != null && mObjectWeakReference.get() instanceof Activity){
                    mTargetContext.getParentView()
                            .addView(
                                    mLoadingLayout,
                                    mTargetContext.getChildIndex(),
                                    mTargetContext.getOldContent().getLayoutParams()
                            );
                    mTargetContext.getParentView()
                            .addView(
                                    mTargetContext.getOldContent(),
                                    mTargetContext.getChildIndex() + 1,
                                    mTargetContext.getOldContent().getLayoutParams()
                            );
                    mTargetContext.getOldContent().setVisibility(View.GONE);
                }
                else {
                    //为xml中的View时，考虑到RelativeLayout和ConstraintLayout这种，
                    // xml中的View需要依靠某些View的Id来确定相对位置，因此必须将替换的View的Id设置为当前替换目标的Id，
                    // 不然依赖的View，找不到该Id就会使得界面上的布局显示错乱
                    mLoadingLayout.setId(mTargetContext.getOldContentId());
                    mTargetContext.getParentView()
                            .addView(
                                    mLoadingLayout,
                                    mTargetContext.getChildIndex(),
                                    mTargetContext.getOldContent().getLayoutParams()
                            );
                }

            }


            if(mDefaultViewportClass != null){
                mLoadingLayout.replaceWithViewport(mDefaultViewportClass);
            }
            else mLoadingLayout.replaceWithViewport(SuccessViewport.class);

            return new Loader(this);
        }


        public void showViewport(Class<? extends ShowingViewport> cls){
            mLoadingLayout.replaceWithViewport(cls);
        }

        public LoadingLayout getLoadingLayout() {
            return mLoadingLayout;
        }
    }


    private static TargetContext getTargetContext(Object target) {
        ViewGroup contentParent;
        Context context;
        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (target instanceof View) {
            View view = (View) target;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        }
        else if(target instanceof SupportLoadingFragment){
            SupportLoadingFragment fragment = (SupportLoadingFragment) target;
            View view = fragment.getView();
            contentParent = (ViewGroup) view;
            context = fragment.getContext();
        }
        else {
            throw new IllegalArgumentException("The target must be within Activity, SupportLoadingFragment, View.");
        }
        int childIndex = 0;
        int childCount = contentParent == null ? 0 : contentParent.getChildCount();
        View oldContent;
        if (target instanceof View) {
            oldContent = (View) target;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    childIndex = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent != null ? contentParent.getChildAt(0) : null;
        }
        if (oldContent == null) {
            throw new IllegalArgumentException(String.format("unexpected error when register LoadHelper in %s", target
                    .getClass().getSimpleName()));
        }

        int oldContentId = oldContent.getId();

        if (contentParent != null) {
            contentParent.removeView(oldContent);
        }

        return new TargetContext(context, contentParent, oldContent, oldContentId, childIndex);
    }



}
