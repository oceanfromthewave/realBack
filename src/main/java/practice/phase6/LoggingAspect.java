package practice.phase6;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect
{

	@Around("execution(* practice.phase6.OrderService.placeOrder(..))")
	public Object logging(ProceedingJoinPoint joinPoint) throws Throwable
	{
		System.out.println("[ASPECT LOG] " + joinPoint.getSignature().getName() + " 시작");
		Object result = joinPoint.proceed();
		System.out.println("[ASPECT LOG] " + joinPoint.getSignature().getName() + " 종료");
		return result;
	}
}
