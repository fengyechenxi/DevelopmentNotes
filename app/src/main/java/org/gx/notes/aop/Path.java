package org.gx.notes.aop;

/**
 * aop的路径配置，这里将代码迁移到其他包名的项目，只需要更改AOP_PKG字段，其余两个不需要更改
 * Created by Administrator on 2017/9/29 0029.
 */

public class Path {
    public static final String AOP_PKG = "org.gx.notes.aop";
    public static final String AOP_POINTCUT_PREFIX = "execution(@";
    public static final String AOP_POINTCUT_SUFFIX = " * *(..))";

}