package org.gx.notes.aop.mask;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gx.notes.aop.Path;
import org.gx.notes.utils.ContextUtils;


/**
 * 弹出遮罩层，该方式不可以精确具体的View，只能施整个遮罩层显示在界面上，
 * 若要使用精确的View引导提示，建议使用DescribeHelper类
 * Created by Administrator on 2017/9/12 0012.
 */
@Aspect
class MaskAop {

    /**
     * 切入点注解的路径
     */
    public static final String ANNOTATION_PATH = Path.AOP_PKG + ".mask.MaskLayer";
    /**
     * 切入点值
     */
    public static final String POINTCUT =
            Path.AOP_POINTCUT_PREFIX + ANNOTATION_PATH + Path.AOP_POINTCUT_SUFFIX;
    /**
     * 替换的方法
     */
    public static final String METHOD = "maskLayer()";



    @Pointcut(POINTCUT)
    public void maskLayer(){}


    @After(METHOD)
    public void showMaskLayer(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MaskLayer maskLayer = signature.getMethod().getAnnotation(MaskLayer.class);
        final int layout = maskLayer.layout();
        final int closeId = maskLayer.closeId();
        final String key = maskLayer.key();
        long delayDuration = maskLayer.delayDuration();

        final FragmentActivity activity = (FragmentActivity) ContextUtils.getContext(joinPoint.getThis());
        if(activity != null){
            if(MaskHelper.isShowMaskLayer(activity,key)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addMask(activity,layout,closeId,key);
                    }
                },delayDuration);
            }

        }
    }

    private void addMask(final FragmentActivity activity, int layout, int closeId, final String key){
        MaskFragment.newInstance(layout,closeId,key)
                .show(activity.getSupportFragmentManager(),MaskFragment.class.getSimpleName());
        MaskHelper.saveAlreadyShowMaskLayer(activity,key);
    }


}
