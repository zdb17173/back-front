package org.fran.back.springboot.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Aspect
@Service
public class LogAspect {
    @Pointcut("@annotation(org.fran.back.springboot.config.aspect.LogAction)")
    public void annotationPointCut() {

    }

    /**
     * 拦截TestRestController @LogAction注解
     * @param joinPoint
     */
    @After("annotationPointCut()")
    public void after(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Object[] args = joinPoint.getArgs();
        Method method = signature.getMethod();
        LogAction action = method.getAnnotation(LogAction.class);
        System.out.println("注解式拦截" + action.name() + "" + args);
    }
}
