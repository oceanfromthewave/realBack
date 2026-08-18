package practice.phase8.dispatcher.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationDispatcherServlet extends HttpServlet
{
	private static class HandlerMethod
	{
		final Object bean;
		final Method method;

		HandlerMethod(Object bean, Method method)
		{
			this.bean = bean;
			this.method = method;
		}
	}

	private final Map<String, HandlerMethod> handlerMapping = new HashMap<>();
	private final List<ArgumentResolver> argumentResolvers = new ArrayList<>();
	private final List<HandlerInterceptor> interceptors = new ArrayList<>();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public AnnotationDispatcherServlet()
	{
		argumentResolvers.add(new RequestParamArgumentResolver());
		interceptors.add(new LoggingHandlerInterceptor());
	}

	public void registerController(Object controller)
	{
		for(Method method : controller.getClass().getDeclaredMethods())
		{
			GetMapping mapping = method.getAnnotation(GetMapping.class);
			if(mapping != null)
			{
				handlerMapping.put(mapping.value(), new HandlerMethod(controller, method));
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String path = req.getPathInfo();
		HandlerMethod handlerMethod = handlerMapping.get(path);

		if(handlerMethod == null)
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No handler for " + path);
			return;
		}

		for(HandlerInterceptor interceptor : interceptors)
		{
			if(!interceptor.preHandle(req, resp, handlerMethod.method))
			{
				return;
			}
		}

		try
		{
			Object[] args = resolveArguments(handlerMethod.method, req);
			Object result = handlerMethod.method.invoke(handlerMethod.bean, args);
			writeResult(result, resp);
		}
		catch(IllegalAccessException | InvocationTargetException e)
		{
			throw new ServletException(e);
		}

		for(HandlerInterceptor interceptor : interceptors)
		{
			interceptor.postHandle(req, resp, handlerMethod.method);
		}
	}

	private void writeResult(Object result, HttpServletResponse resp) throws IOException
	{
		if(result instanceof String str)
		{
			resp.setContentType("text/plain;charset=UTF-8");
			resp.getWriter().println(str);
		}
		else
		{
			resp.setContentType("application/json;charset=UTF-8");
			resp.getWriter().println(objectMapper.writeValueAsString(result));
		}
	}

	private Object[] resolveArguments(Method method, HttpServletRequest req)
	{
		Parameter[] parameters = method.getParameters();
		Object[] args = new Object[parameters.length];

		for(int i = 0; i < parameters.length; i++)
		{
			Parameter parameter = parameters[i];
			for(ArgumentResolver resolver : argumentResolvers)
			{
				if(resolver.supports(parameter))
				{
					args[i] = resolver.resolve(parameter, req);
					break;
				}
			}
		}

		return args;
	}
}
