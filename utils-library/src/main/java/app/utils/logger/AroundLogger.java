package app.utils.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AroundLogger {

    @Around("execution(* app.*..*.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        Object[] methodArgs = joinPoint.getArgs();

        if (methodArgs.length > 0) {
            log.debug("{} | Called: {}:{}", className, methodName, methodArgs);
        } else {
            log.debug("{} | Called: {}", className, methodName);
        }

        Object result = joinPoint.proceed();

        if (result != null) {
            if (methodArgs.length > 0) {
                log.trace("{} | Executed: {}:{} with result: {}", className, methodName, methodArgs,
                        result);
            } else {
                log.trace("{} | Executed: {} with result: {}", className, methodName, result);
            }

        } else {
            if (methodArgs.length > 0) {
                log.trace("{} | Executed: {}:{}", className, methodName, methodArgs);
            } else {
                log.trace("{} | Executed: {}", className, methodName);
            }
        }

        return result;
    }
}