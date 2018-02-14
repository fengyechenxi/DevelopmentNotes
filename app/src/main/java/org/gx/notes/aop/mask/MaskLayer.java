package org.gx.notes.aop.mask;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 弹出遮罩层，该方式不可以精确具体的View，只能施整个遮罩层显示在界面上，
 * 若要使用精确的View引导提示，建议使用DescribeHelper类
 * Created by Administrator on 2017/9/12 0012.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskLayer {
    /**
     * 要显示的遮罩层View的布局id
     * @return
     */
    @LayoutRes int layout();

    /**
     * 遮罩层布局中某个View的id值，配置了该值后，会在代码内部设置该id对应控件的点击事件，会触发点击关闭整个遮罩层
     * @return
     */
    @IdRes int closeId();

    /**
     *某个要显示遮罩提示层的界面，如果这个界面第一次进入新版本，引导提示显示过了，需要记录已经显示，下次就不需要再显示，
     * 该值就是在SharedPreferences文件中保存的标识
     * @return
     */
    String key();

    /**
     *延迟显示的时间
     * @return
     */
    long delayDuration();
}
