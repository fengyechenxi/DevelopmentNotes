package org.gx.notes.load.loading;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.SparseArray;
import android.view.View;

import org.gx.notes.load.loading.viewport.ShowingViewport;
import org.gx.notes.load.loading.viewport.SuccessViewport;

import java.util.Iterator;
import java.util.Set;

/**
 * 界面或者View，任何一处都可以使用的加载辅助类，
 * 该类使用方便，无侵入，可以全局设置(已经去掉相关代码，暂时不支持)，也可以在具体的界面或某个具体View上设置。
 * Created by Administrator on 2017/9/26 0026.
 */

/**
 * 基本用法举例如下：
 * 第一步：创建需要显示的ShowingViewport类。
 * LoadingViewport loadingViewport = new LoadingViewport();
 * ErrorViewport errorViewport = new ErrorViewport();
 * errorViewport.setOnReloadListener(
 *          new ShowingViewport.OnReloadListener() {
 *              @Override
 *              public void onReload(View v) {
 *
 *              }
 *          }
 * );
 * 注：LoadingViewport为继承自ShowingViewport的类，表示正在加载状态的视图，ErrorViewport为继承自ShowingViewport的类，表示加载错误的视图状态,
 * setOnReloadListener表示设置重新加载时候的回调，ErrorViewport中具体哪个控件能够响应该回调，需要在对象的initRetry中配置。
 * 第二步：构建一个Loader对象,用来添加loading事件的各种视图状态,还有loading的目标对象。
 * 注：Builder构造方法中传入的就是loading的目标对象,例子中传入的是Activity，也可以是某个Fragment或者View。addViewport是添加一种会用到的视图状态，setDefaultViewport是设置默认显示的视图状态。
 * Loader loader =new Loader.Builder(MainActivity.this).addViewport(loadingViewport).addViewport(errorViewport).setDefaultViewport(LoadingViewport.class).build();
 *第三步：注册loading事件，并且绑定Loader对象。
 *LoadingHelper.getHelper().register(MainActivity.this, loader);
 * 第四步：通过LoadingHelper对象，根据业务逻辑，可以随意切换视图状态，例如：
 * 显示成功状态： LoadingHelper.getHelper().showSuccess(MainActivity.this);
 * 显示当前自定义好的错误状态：LoadingHelper.getHelper().showViewport(MainActivity.this,ErrorViewport.class);
 * 如果有其他状态，需要先自定义，然后根据显示错误状态的类似代码进行显示，如果要显示某种状态，必须在Loader中先添加该状态的实例对象，否则无法找到。
 * 第五步：注销loading事件，并将目标绑定的Loader对象从LoadingHelper单例中移除。
 *  LoadingHelper.getHelper().unRegister(MainActivity.this);
 *  注释：（1）如果是在Activity中注册Activity和View的loading事件，前四步可以在onCreate中调用，
 *  但如果是在Fragment中注册Fragment和View的loading事件，需要在onCreateView完成之后才能调用，因为只有onCreateView方法完成后，
 *  Fragment才会有根视图，可在Fragment的onViewCreated中完成前四步。第五步需要在Activity和Fragment的onDestroy方法和onDestroyView方法中调用。
 * （2）使用loading事件的目标对象，即Activity，Fragment，View三种，不能够修改hashcode的默认方法，因为注册loading事件对应绑定的loader对象，就是通过hash值来确定唯一性的。
 */

public final class LoadingHelper {

    private static volatile LoadingHelper sHelper;

    private SparseArray<Loader> mLoaders;
//    private Settings mDefaultSettings;


    private LoadingHelper(){
        mLoaders = new SparseArray<Loader>();

    }
    public static LoadingHelper getHelper(){

        if(sHelper == null){
            synchronized (LoadingHelper.class){
                if(sHelper == null){
                    sHelper = new LoadingHelper();
                }
            }
        }
        return sHelper;
    }

//    public Settings defaultSettings(){
//        return new Settings();
//    }
//
//    void setDefaultSettings(Settings settings){
//        mDefaultSettings = settings;
//    }


    /**
     * 通过全局配置好的统一Loader，进行Loading事件，该方法使用的前提，必须全局配置过Settings类
     * @param target
     * @param onReloadListener
     * @return
     */
//    public LoadingHelper registerWithDefault(@NonNull Object target, ShowingViewport.OnReloadListener onReloadListener){
//        Loader loader =
//                getDefaultBuilder(target,mDefaultSettings)
////                        .setOnReloadListener(onReloadListener)
//                        .build();
//        registerLoader(target,loader);
//        return getHelper();
//    }

    /**
     * 注册Loading事件
     * @param target 只能是Activity，SupportLoadingFragment，View的子类
     * @param loader 封装loading数据的对象
     * @return LoadingHelper加载单例助手
     */
    private LoadingHelper registerLoader(@NonNull Object target, Loader loader){
        targetLegal(target);
        mLoaders.put(loader.getKey(),loader);
        return getHelper();
    }
    /**
     *注销Loading事件，这里传入的ta
     * @param target 只能是Activity，SupportLoadingFragment，View的子类,并且这里的target必须与调用register注册的target是同一个对象
     */
    private void unRegisterLoader(@NonNull Object target){
        if(target != null){
            targetLegal(target);
            int hashCode = target.hashCode();
            mLoaders.get(hashCode).clear();
            mLoaders.delete(hashCode);
        }
    }

    public LoadingHelper register(@NonNull View view, Loader loader){
        return registerLoader(view,loader);
    }

    public LoadingHelper register(@NonNull Activity activity,Loader loader){
        return registerLoader(activity,loader);
    }

    public LoadingHelper register(@NonNull SupportLoadingFragment fragment, Loader loader){
        return registerLoader(fragment,loader);
    }

    public void unRegister(@NonNull Activity activity){
        unRegisterLoader(activity);
    }

    public void unRegister(@NonNull SupportLoadingFragment fragment){
        unRegisterLoader(fragment);
    }

    public void unRegister(@NonNull View view){
        unRegisterLoader(view);
    }

    /**
     *获取loading时候的LoadingLayout
     * @param target  该target必须传入与注册时候的target是同一个对象
     * @return
     */
    public LoadingLayout getLoadLayout(@NonNull Object target){
        return getLoader(target).getLoadLayout();
    }

    /**
     * 获取当前拥有的Loader数量
     * @return
     */
    public int getLoaderSize(){
        return mLoaders.size();
    }


    /**
     * 显示某个ShowingViewport视图
     * @param target  注册时候对应的target
     * @param viewport ShowingViewport视图class类型
     */
    private void showViewportUI(@NonNull Object target, Class<? extends ShowingViewport> viewport){
        targetLegal(target);
        Loader loader = getLoader(target);
        if(loader != null){
            loader.showViewport(viewport);
        }
    }

    public void showViewport(@NonNull Activity activity, Class<? extends ShowingViewport> viewport){
        showViewportUI(activity,viewport);
    }

    public void showViewport(@NonNull View view, Class<? extends ShowingViewport> viewport){
        showViewportUI(view,viewport);
    }

    public void showViewport(@NonNull SupportLoadingFragment fragment, Class<? extends ShowingViewport> viewport){
        showViewportUI(fragment,viewport);
    }



    /**
     * 显示加载成功，或者完成时候的状态,也就是正常要显示的界面
     * @param target
     */
    private void showSuccessUI(@NonNull Object target){
        targetLegal(target);
        Loader loader = getLoader(target);
        if(loader != null){
            loader.showViewport(SuccessViewport.class);
        }
    }

    public void showSuccess(@NonNull Activity activity){
        showSuccessUI(activity);
    }

    public void showSuccess(@NonNull View view){
        showSuccessUI(view);
    }

    public void showSuccess(@NonNull SupportLoadingFragment fragment){
        showSuccessUI(fragment);
    }


    /**
     * 获取默认的Loader.Builder,该方法用于全局配置统一的加载视图，
     * 从配置好的Settings中获取设置的Loader.Builder对象
     * @param target
     * @param defaultSettings
     * @return
     */
//    private Loader.Builder getDefaultBuilder(Object target, Settings defaultSettings){
//        Loader.Builder builder = new Loader.Builder(target);
//        if(defaultSettings == null){
//            throw new IllegalStateException("before use registerWithDefault(Object target),you must first init a defaultSettings");
//        }
//        ArrayMap<Class<? extends ShowingViewport>,ShowingViewport> viewports = defaultSettings.getViewports();
//        if(viewports != null && viewports.size() > 0){
//            Set<Class<? extends ShowingViewport>> set = viewports.keySet();
//            for(Iterator<Class<? extends ShowingViewport>> it = set.iterator(); it.hasNext();){
//                builder.addViewport(viewports.get(it.next()));
//            }
//        }
//
//        Class<? extends ShowingViewport> defaultViewport = defaultSettings.getDefaultViewportClass();
//        if(defaultViewport != null){
//            builder.setDefaultViewport(defaultViewport);
//        }
//        return builder;
//    }

    /**
     * @param target
     * @return 根据传入的target获取对于target注册的loader事件，这里是通过target的hash值作为唯一定位值，因此target不能够重写hash，否则将会无效
     */
    private Loader getLoader(@NonNull Object target){
        return mLoaders.get(target.hashCode());
    }


    /**
     * 判断传入的target是否合法
     * @param target 只能是Activity，Fragment，View对象
     */
    private void targetLegal(@NonNull Object target){
        if(target == null){
            throw new IllegalStateException("target must not be null");
        }
        if(!(target instanceof Activity) && !(target instanceof View) && !(target instanceof SupportLoadingFragment)){
            throw new IllegalStateException("target must be extends Activity，Fragment or View");
        }

    }


    /**
     * 用来全局统一配置的配置选项类
     */
//    public static class Settings{
//
//        private ArrayMap<Class<? extends ShowingViewport>,ShowingViewport> mViewports;
//        private Class<? extends ShowingViewport> mDefaultViewportClass;
//
//        public Settings(){
//            mViewports = new ArrayMap<>();
//        }
//
//        public Class<? extends ShowingViewport> getDefaultViewportClass() {
//            return mDefaultViewportClass;
//        }
//
//        public ArrayMap<Class<? extends ShowingViewport>, ShowingViewport> getViewports() {
//            return mViewports;
//        }
//
//
//        /**
//         * 设置默认显示的ShowingViewport视图
//         * @param defaultViewportClass
//         * @return
//         */
//        public Settings setDefaultShowingViewportClass(Class<? extends ShowingViewport> defaultViewportClass) {
//            mDefaultViewportClass = defaultViewportClass;
//            return this;
//        }
//
//        /**
//         * 添加一项需要显示的ShowingViewport视图
//         * @param viewport
//         * @return
//         */
//        public Settings addShowingViewport(ShowingViewport viewport){
//            if(viewport != null){
//                Class<? extends ShowingViewport> cls = viewport.getClass();
//                if(!mViewports.containsKey(cls)){
//                    mViewports.put(cls,viewport);
//                }
//            }
//            return this;
//        }
//
//        public void save(){
//            getHelper().setDefaultSettings(this);
//        }
//
//
//    }

}
