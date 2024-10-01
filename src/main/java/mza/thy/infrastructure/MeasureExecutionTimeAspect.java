package mza.thy.infrastructure;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
/*
https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/pointcuts.html
 */
@Aspect
@Slf4j
@Component
@ConditionalOnExpression("${aspect.enabled:true}")//w application.localdev.yml set to true to turn on the time mesurement
public class MeasureExecutionTimeAspect {

    @Pointcut("execution (public * mza.thy..*(..))")
    public void executeOnAnyPublicMethodForServices() {
    }

    @Around("executeOnAnyPublicMethodForServices()")
    public Object runMeasure(ProceedingJoinPoint pjp) throws Throwable {
        val startTime = System.currentTimeMillis();
     //   log.info("Executing method {} started", pjp.getSignature().getName());
        val result = pjp.proceed();

        val endTime = System.currentTimeMillis() - startTime;
        log.info("Processing of method {} ended in time {} ms", pjp.getSignature().getName(), endTime);
        return result;
    }
//inny przyklad:
 /*   @Around("@annotation(mza.thy.infrastructure.TrackExecutionTime)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = point.proceed();
        long endtime = System.currentTimeMillis();
        log.info("Class Name: " + point.getSignature().getDeclaringTypeName() + ". Method Name: " + point.getSignature().getName() + ". Time taken for Execution is : " + (endtime - startTime) + "ms");
        return object;
    }*/
}
