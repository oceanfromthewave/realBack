package practice.phase8.dispatcher.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public class LoggingHandlerInterceptor implements HandlerInterceptor
{
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Method handler)
	{
		System.out.println("[INTERCEPTOR] preHandle: " + handler.getName());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse resp, Method handler)
	{
		System.out.println("[INTERCEPTOR] postHandle: " + handler.getName());
	}
}
