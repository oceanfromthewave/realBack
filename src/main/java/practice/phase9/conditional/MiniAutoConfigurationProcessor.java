package practice.phase9.conditional;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MiniAutoConfigurationProcessor
{
	private final Map<Class<?>, Object> beans = new HashMap<>();
	private final String activeProfile;

	public MiniAutoConfigurationProcessor(String activeProfile)
	{
		this.activeProfile = activeProfile;
	}

	public void process(Object configuration) throws Exception
	{
		for(Method method : configuration.getClass().getDeclaredMethods())
		{
			if(!method.isAnnotationPresent(Bean.class))
			{
				continue;
			}

			if(!conditionMatches(method))
			{
				System.out.println("[SKIP] " + method.getName() + " - condition not matched");
				continue;
			}

			Object bean = method.invoke(configuration);
			beans.put(method.getReturnType(), bean);
			System.out.println("[REGISTER] " + method.getName() + " -> " + bean);
		}
	}

	private boolean conditionMatches(Method method)
	{
		ConditionalOnClass classCondition = method.getAnnotation(ConditionalOnClass.class);
		if(classCondition != null)
		{
			try
			{
				Class.forName(classCondition.value());
			}
			catch(ClassNotFoundException e)
			{
				return false;
			}
		}

		Profile profileCondition = method.getAnnotation(Profile.class);
		if(profileCondition != null && !profileCondition.value().equals(activeProfile))
		{
			return false;
		}

		return true;
	}

	public <T> T getBean(Class<T> type)
	{
		return type.cast(beans.get(type));
	}
}
