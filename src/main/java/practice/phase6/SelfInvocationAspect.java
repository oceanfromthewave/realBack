package practice.phase6;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SelfInvocationAspect
{
	@Around("execution(* practice.phase6.SelfInvocationDemo.inner(..))")
	public Object logging(ProceedingJoinPoint joinPoint) throws Throwable
	{
		System.out.println("[SELF-INVOCATION LOG] inner 가로챔");
		return joinPoint.proceed();
	}
}
