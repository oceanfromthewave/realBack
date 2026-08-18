package practice.phase8.dispatcher.annotation;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Parameter;

public interface ArgumentResolver
{
	boolean supports(Parameter parameter);

	Object resolve(Parameter parameter, HttpServletRequest req);
}
