package practice.phase1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

public class SimpleContainer
{
	public Object create(Class<?> type) throws Exception
	{
		if(!type.isAnnotationPresent(Component.class))
		{
			throw new IllegalArgumentException(type + " has no @Component");
		}
		Constructor<?> constructor = type.getDeclaredConstructors()[0];
		Class<?>[] paramTypes = constructor.getParameterTypes();
		Object[] params = new Object[paramTypes.length];
		for(int i = 0; i < params.length; i++)
		{
			params[i] = create(paramTypes[i]);
		}
		return constructor.newInstance(params);
	}
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Component
{

}
