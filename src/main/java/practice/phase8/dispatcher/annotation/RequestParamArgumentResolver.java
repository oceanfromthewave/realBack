package practice.phase8.dispatcher.annotation;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Parameter;

public class RequestParamArgumentResolver implements ArgumentResolver
{
	@Override
	public boolean supports(Parameter parameter)
	{
		return parameter.isAnnotationPresent(RequestParam.class);
	}

	@Override
	public Object resolve(Parameter parameter, HttpServletRequest req)
	{
		RequestParam annotation = parameter.getAnnotation(RequestParam.class);
		return req.getParameter(annotation.value());
	}
}
