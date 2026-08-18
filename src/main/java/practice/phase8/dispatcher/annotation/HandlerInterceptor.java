package practice.phase8.dispatcher.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;

public interface HandlerInterceptor
{
	default boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Method handler)
	{
		return true;
	}

	default void postHandle(HttpServletRequest req, HttpServletResponse resp, Method handler)
	{
	}
}
