package org.gx.notes.aop.log;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.gx.notes.aop.Path;

/**
 * 面向切面日志打印，主需要将注解加在方法上，就可以打印当前方法的参数值，可替代断点调试，更加方便快捷
 * Created by Administrator on 2017/9/11 0011.
 */
@Aspect
class LogAop {

    public static final String TAG = LogAop.class.getSimpleName();

    /**
     * 切入点注解的路径
     */
    public static final String ANNOTATION_PATH = Path.AOP_PKG + ".log.Log";
    /**
     * 切入点值
     */
    public static final String POINTCUT =
            Path.AOP_POINTCUT_PREFIX + ANNOTATION_PATH + Path.AOP_POINTCUT_SUFFIX;
    /**
     * 替换的方法
     */
    public static final String METHOD = "logMethod()";


    @Pointcut(POINTCUT)
    public void logMethod(){}

    @Before(METHOD)
    public void logBefore(JoinPoint joinPoint){

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] paramsValues = joinPoint.getArgs();
        String[] paramsNames = signature.getParameterNames();
//        Class[] paramsTypes = signature.getParameterTypes();

        StringBuilder sb = new StringBuilder();
        sb.append(signature.toLongString())
                .append("\n")
                .append("[\n");

        for(int i=0;i< paramsValues.length;i++){

            sb.append("\t{")
//                    .append(paramsTypes[i])
//                    .append("=>")
                    .append(paramsNames[i])
                    .append(":")
                    .append(paramsValues[i])
                    .append("}")
                    .append(",\n");

        }
        sb.deleteCharAt(sb.length()-2);
        sb.append("]");
        android.util.Log.e(TAG,sb.toString());
    }

}
