package mza.thy.infrastructure;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@ConditionalOnExpression("${aspect.enabled:true}")
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
/*
    @Around("@annotation(mza.thy.infrastructure.TrackExecutionTime)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = point.proceed();
        long endtime = System.currentTimeMillis();
        log.info("Class Name: " + point.getSignature().getDeclaringTypeName() + ". Method Name: " + point.getSignature().getName() + ". Time taken for Execution is : " + (endtime - startTime) + "ms");
        return object;
    }
 */
}
