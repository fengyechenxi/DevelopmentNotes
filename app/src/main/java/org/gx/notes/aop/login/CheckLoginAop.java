package org.gx.notes.aop.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gx.notes.aop.Path;
import org.gx.notes.utils.ContextUtils;

/**
 * 无侵入，不打断原有业务逻辑，判断登录与否，并加上跳转逻辑
 * Created by Administrator on 2017/9/25 0025.
 */
@Aspect
class CheckLoginAop {

    /**
     * 切入点注解的路径
     */
    public static final String ANNOTATION_PATH = Path.AOP_PKG + ".login.CheckLogin";
    /**
     * 切入点值
     */
    public static final String POINTCUT =
            Path.AOP_POINTCUT_PREFIX + ANNOTATION_PATH + Path.AOP_POINTCUT_SUFFIX;
    /**
     * 替换的方法
     */
    public static final String METHOD = "checkLogin()";

    @Pointcut(POINTCUT)
    public void checkLogin(){}

    @Around(METHOD)
    public void checkLoginImpl(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckLogin checkLogin = signature.getMethod().getAnnotation(CheckLogin.class);
        String spKey = checkLogin.spKey();
        String preferenceName = checkLogin.preferenceName();
        String activityName = checkLogin.activityName();
        Context context = ContextUtils.getContext(joinPoint.getThis());

        if(isLogin(context,spKey,preferenceName)){//已经登录
            joinPoint.proceed();
        }
        else {//没有登录
            goToLogin(context,activityName);
        }

    }

    //判断是否登录
    private boolean isLogin(Context context, String spKey, String preferenceName){
        SharedPreferences preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return preferences.getBoolean(spKey,false);
    }

    //去登录
    private void goToLogin(Context context, String activityName){
        //TODO 跳转到登录界面
        Intent intent = new Intent();
        intent.setClassName(context,activityName);
        context.startActivity(intent);
    }

}
