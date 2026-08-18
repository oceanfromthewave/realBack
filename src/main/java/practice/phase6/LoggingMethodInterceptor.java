package practice.phase6;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class LoggingMethodInterceptor implements MethodInterceptor
{

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable
	{
		System.out.println("[CGLIB LOG] " + method.getName() + " 시작");
		Object result = proxy.invokeSuper(obj, args);
		System.out.println("[CGLIB LOG] " + method.getName() + " 종료");
		return result;
	}
}

