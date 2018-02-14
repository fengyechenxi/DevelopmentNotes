package org.gx.notes.aop.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检测登录注解定义，
 * 该功能可以在不打断原有业务逻辑之上，加入新的逻辑，只需要将该注解放在某个方法之上，配合SharedPreferences即可完成
 * Created by Administrator on 2017/9/25 0025.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {
    /**
     * 记录是否已经登录过的SharedPreferences文件中判断标识的key值
     * @return
     */
    String spKey();

    /**
     * 保存登录标识的SharedPreferences文件名字，内部是采用SharedPreferences的私有文件，因此需要传入文件名
     * @return
     */
    String preferenceName();

    /**
     * 如果没有登录，则将会跳转到该值配置的Activity界面，一般该值是登录界面
     * @return
     */
    String activityName();
}
