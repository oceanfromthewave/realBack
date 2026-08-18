package practice.phase9.properties;

import java.beans.Introspector;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

public class PropertiesBinder
{
	public <T> T bind(String resourceName, Class<T> targetType) throws Exception
	{
		ConfigurationProperties config = targetType.getAnnotation(ConfigurationProperties.class);
		if(config == null)
		{
			throw new IllegalArgumentException(targetType.getName() + " has no @ConfigurationProperties");
		}

		Properties properties = loadProperties(resourceName);
		T target = targetType.getDeclaredConstructor().newInstance();
		String prefix = config.prefix() + ".";

		for(String key : properties.stringPropertyNames())
		{
			if(!key.startsWith(prefix))
			{
				continue;
			}

			String fieldName = key.substring(prefix.length());
			String value = properties.getProperty(key);
			setField(target, fieldName, value);
		}

		return target;
	}

	private void setField(Object target, String fieldName, String value) throws Exception
	{
		String setterName = "set" + Introspector.decapitalize(fieldName).substring(0, 1).toUpperCase() + Introspector.decapitalize(fieldName).substring(1);

		for(Method method : target.getClass().getMethods())
		{
			if(!method.getName().equals(setterName) || method.getParameterCount() != 1)
			{
				continue;
			}

			Class<?> paramType = method.getParameterTypes()[0];
			if(paramType == int.class)
			{
				method.invoke(target, Integer.parseInt(value));
			}
			else
			{
				method.invoke(target, value);
			}
			return;
		}
	}

	private Properties loadProperties(String resourceName) throws IOException
	{
		Properties properties = new Properties();
		try(InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName))
		{
			if(in == null)
			{
				throw new IOException(resourceName + " not found on classpath");
			}
			properties.load(in);
		}
		return properties;
	}
}
