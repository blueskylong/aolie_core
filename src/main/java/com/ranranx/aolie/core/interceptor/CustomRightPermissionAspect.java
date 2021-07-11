package com.ranranx.aolie.core.interceptor;

import com.ranranx.aolie.core.annotation.CustomRightPermission;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.exceptions.IllegalOperatorException;
import com.ranranx.aolie.core.exceptions.NotLoginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 手动权限注解的权限校验
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021.6.10 0010 21:58
 **/
@Aspect
@Component
public class CustomRightPermissionAspect {
    /**
     * 缓存 调用方法和权限的对应关系 KEY:Clase.methodName,VALUE:RIGHT CODE
     */
    private static final Map<String, String> mapRightSet = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(CustomRightPermissionAspect.class);

    @Pointcut("@annotation(com.ranranx.aolie.core.annotation.CustomRightPermission)")
    public void checkPermission() {
    }

    @Around("checkPermission()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        Class clazz = joinPoint.getTarget().getClass();
        String targetName = clazz.getName();
        String methodName = joinPoint.getSignature().getName();
        String methodKey = targetName + ":" + methodName;
        if (SessionUtils.getLoginUser() == null) {
            throw new NotLoginException(methodKey);
        }
        //如果已缓存
        String rightCode = "";
        if (mapRightSet.containsKey(methodKey)) {
            rightCode = mapRightSet.get(methodKey);
        } else {
            Method method = clazz.getMethod(methodName);
            rightCode = getMethodRightCode(method);
            mapRightSet.put(methodKey, rightCode);
        }
        //进行检查
        if (!SessionUtils.getLoginUser().hasCustomRight(rightCode)) {
            throw new IllegalOperatorException("没有操作权限:" + methodKey);
        }
        return joinPoint.proceed();
    }

    /**
     * 取得方法流程上的权限编码
     *
     * @param method
     * @return
     */
    private String getMethodRightCode(Method method) {
        CustomRightPermission annotation = method.getAnnotation(CustomRightPermission.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            return "";
        }
    }


}

