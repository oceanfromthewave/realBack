package practice.phase6;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler
{

	private final Object target;

	public LoggingInvocationHandler(Object target)
	{
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		System.out.println("[LOG] " + method.getName() + " 시작");
		Object result = method.invoke(target, args);
		System.out.println("[LOG] " + method.getName() + " 종료");
		return result;
	}
}

