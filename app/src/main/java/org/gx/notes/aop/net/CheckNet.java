package org.gx.notes.aop.net;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在某个需要网络的业务方法上添加该注解，如果当前没有网络，就不会执行该方法内部的代码，而是提示没有网络
 * Created by Administrator on 2017/9/11 0011.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckNet {
}
