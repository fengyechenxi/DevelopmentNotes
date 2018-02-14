package org.gx.notes.aop.permission;

import android.support.annotation.StringRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 执行某个方法时候，方法内部的代码需要某项权限，在该方法上加上该注解，就可以弹出请求权限的弹窗
 * Created by Administrator on 2017/9/12 0012.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPermission {
    /**
     * 需要权限的数组
     * @return
     */
    String[] permissions();

    /**
     * 如果拒绝了该权限，就会弹出该提示，让用户去开启
     * @return
     */
    @StringRes int message();

    /**
     * 该值用来设置用户拒绝了权限之后，是否把当前界面关闭，通常可以用在启动页面，请求一些基本的权限，如果用户拒绝，就可以直接将启动页关闭
     * 如果这里是启动页，拒绝权限后，这里没有添加杀死当前进程的字段，如有需求，可以重写添加
     * @return
     */
    boolean finish();
}
