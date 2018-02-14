package org.gx.notes.aop.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gx.notes.aop.Path;
import org.gx.notes.aop.permission.core.PermissionHelper;
import org.gx.notes.aop.permission.core.RxPermissions;
import org.gx.notes.utils.ContextUtils;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/9/12 0012.
 */
@Aspect
class CheckPermissionAop {

    /**
     * 切入点注解的路径
     */
    public static final String ANNOTATION_PATH = Path.AOP_PKG + ".permission.CheckPermission";
    /**
     * 切入点值
     */
    public static final String POINTCUT =
            Path.AOP_POINTCUT_PREFIX + ANNOTATION_PATH + Path.AOP_POINTCUT_SUFFIX;
    /**
     * 替换的方法
     */
    public static final String METHOD = "check()";


    @Pointcut(POINTCUT)
    public void check(){}


    @Around(METHOD)
    public void checkPermission(final ProceedingJoinPoint joinPoint) throws Throwable {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            CheckPermission checkPermission = signature.getMethod().getAnnotation(CheckPermission.class);
            String[] permissions = checkPermission.permissions();
            final int msg = checkPermission.message();
            final boolean finish = checkPermission.finish();
            final Activity activity = (Activity) ContextUtils.getContext(joinPoint.getThis());
            if(activity != null){
                new RxPermissions(activity)
                        .request(permissions)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (!granted) {
                                    // All requested permissions are granted
                                    showDialog(activity,msg,finish);
                                } else {
                                    // At least one permission is denied
                                    try {
                                        joinPoint.proceed();
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            }
                        });
            }
            else joinPoint.proceed();
        }
        else joinPoint.proceed();

    }

    private void showDialog(final Activity activity, int message, final boolean finish){

        Resources resources = activity.getResources();
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(resources.getString(message))
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(finish){
                            activity.finish();
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PermissionHelper.getAppDetailSettingIntent(activity);
                        if(finish){
                            activity.finish();
                        }
                    }
        }).create().show();




    }





}
