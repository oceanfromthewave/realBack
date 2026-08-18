package practice.phase10.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import practice.phase8.dispatcher.annotation.GetMapping;
import practice.phase8.dispatcher.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlingDispatcherServlet extends HttpServlet
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
	private final Map<Class<? extends Throwable>, HandlerMethod> exceptionHandlers = new HashMap<>();
	private final ObjectMapper objectMapper = new ObjectMapper();

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

	public void registerControllerAdvice(Object advice)
	{
		if(!advice.getClass().isAnnotationPresent(ControllerAdvice.class))
		{
			throw new IllegalArgumentException(advice.getClass().getName() + " has no @ControllerAdvice");
		}

		for(Method method : advice.getClass().getDeclaredMethods())
		{
			ExceptionHandler handler = method.getAnnotation(ExceptionHandler.class);
			if(handler != null)
			{
				exceptionHandlers.put(handler.value(), new HandlerMethod(advice, method));
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

		try
		{
			Object[] args = resolveArguments(handlerMethod.method, req);
			Object result = handlerMethod.method.invoke(handlerMethod.bean, args);
			writeJson(result, HttpServletResponse.SC_OK, resp);
		}
		catch(InvocationTargetException e)
		{
			handleException(e.getCause(), resp);
		}
		catch(IllegalAccessException e)
		{
			throw new ServletException(e);
		}
	}

	private void handleException(Throwable cause, HttpServletResponse resp) throws IOException, ServletException
	{
		HandlerMethod handler = exceptionHandlers.get(cause.getClass());

		if(handler == null)
		{
			throw new ServletException(cause);
		}

		try
		{
			Object result = handler.method.invoke(handler.bean, cause);
			int status = (result instanceof ErrorResponse errorResponse) ? errorResponse.getStatus() : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			writeJson(result, status, resp);
		}
		catch(IllegalAccessException | InvocationTargetException e)
		{
			throw new ServletException(e);
		}
	}

	private void writeJson(Object result, int status, HttpServletResponse resp) throws IOException
	{
		resp.setStatus(status);
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().println(objectMapper.writeValueAsString(result));
	}

	private Object[] resolveArguments(Method method, HttpServletRequest req)
	{
		Parameter[] parameters = method.getParameters();
		Object[] args = new Object[parameters.length];

		for(int i = 0; i < parameters.length; i++)
		{
			RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
			if(requestParam != null)
			{
				args[i] = req.getParameter(requestParam.value());
			}
		}

		return args;
	}
}
