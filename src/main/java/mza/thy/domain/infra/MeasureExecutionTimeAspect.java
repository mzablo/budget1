package mza.thy.domain.infra;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class MeasureExecutionTimeAspect {

 //   @Pointcut("execution (public * mza.thy..*(..))")
    public void executeOnAnyPublicMethodForServices() {
    }

//    @Around("executeOnAnyPublicMethodForServices()")
    public Object runMeasure(ProceedingJoinPoint pjp) throws Throwable {
        val startTime = System.currentTimeMillis();
     //   log.info("Executing method {} started", pjp.getSignature().getName());
        val result = pjp.proceed();

        val endTime = System.currentTimeMillis() - startTime;
        log.info("Processing of method {} ended in time {} ms", pjp.getSignature().getName(), endTime);
        return result;
    }
}
