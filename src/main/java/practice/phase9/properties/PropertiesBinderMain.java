package practice.phase9.properties;

public class PropertiesBinderMain
{
	public static void main(String[] args) throws Exception
	{
		PropertiesBinder binder = new PropertiesBinder();
		AppProperties properties = binder.bind("application.properties", AppProperties.class);

		System.out.println(properties);
	}
}
