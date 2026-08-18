package practice.phase6;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LoggingAdvice implements MethodInterceptor
{
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		System.out.println("[SPRING AOP LOG] " + invocation.getMethod().getName() + " 시작 ");
		Object result = invocation.proceed();
		System.out.println("[SPRING AOP LOG] " + invocation.getMethod().getName() + " 종료 ");
		return result;
	}
}
