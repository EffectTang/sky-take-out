package com.sky.aspect;/**
 * @description
 * @author starlord
 * @create 2025/5/22
 */

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.sky.annotations.TestAnnotation;

import java.lang.reflect.Method;

/**
 * @author starlord
 * @description
 * @create 2025/5/22
 */
@Aspect
@Component
public class AutoFillAspect {

    //@Before("execution(* com.sky.service.impl.*.*(..)) && @annotation(com.sky.annotations.TestAnnotation)")
    //public void autoFill(JoinPoint joinPoint , TestAnnotation testAnnotation){
    //    System.out.println("开始进行数据填充");
    //}

    @Before("execution(* com.sky.service.impl.*.*(..)) && @annotation(testAnnotation)")
    public void autoFill1(JoinPoint joinPoint, TestAnnotation testAnnotation) {
        System.out.println("开始进行数据填充");
        System.out.println("作者：" + testAnnotation.author());
        System.out.println(joinPoint.getSignature().getName());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        System.out.println("method:"+method.getName());
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            params.append(arg).append(", ");
        }
        if (params.length() > 0) {
            params.setLength(params.length() - 2); // 移除最后多余的逗号和空格
        }
        System.out.println("参数：" + params);
        // 可以根据 testAnnotation 的属性做进一步处理
    }



}
