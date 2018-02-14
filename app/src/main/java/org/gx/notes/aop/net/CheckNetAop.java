package org.gx.notes.aop.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gx.notes.aop.Path;
import org.gx.notes.toast.TopToastView;
import org.gx.notes.utils.ContextUtils;

/**
 * Created by Administrator on 2017/9/11 0011.
 */
@Aspect
class CheckNetAop {

    /**
     * 切入点注解的路径
     */
    public static final String ANNOTATION_PATH = Path.AOP_PKG + ".net.CheckNet";
    /**
     * 切入点值
     */
    public static final String POINTCUT =
            Path.AOP_POINTCUT_PREFIX + ANNOTATION_PATH + Path.AOP_POINTCUT_SUFFIX;
    /**
     * 替换的方法
     */
    public static final String METHOD = "checkNet()";

    @Pointcut(POINTCUT)
    public void checkNet(){}

    @Around(METHOD)
    public Object checkNetEnvironment(ProceedingJoinPoint joinPoint) throws Throwable {

        //先获取是否使用了注解CheckNet
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
        if(checkNet != null){
            //使用了注解CheckNet，获取上下文，判断网络环境
            Context context = ContextUtils.getContext(joinPoint.getThis());
            if(context != null){
                if(!isNetworkAvailable(context)){
                    new TopToastView.Builder(context)
                            .message("网络进入二次元，请检查网络！")
                            .bgColor("#ff0000")
                            .textColor("#ffffff")
                            .globalToastView(true)
                            .build()
                            .showToast();
                    return null;
                }
            }
        }

        return joinPoint.proceed();
    }



    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    private static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
