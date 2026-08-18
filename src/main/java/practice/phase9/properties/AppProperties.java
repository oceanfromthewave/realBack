package practice.phase9.properties;

@ConfigurationProperties(prefix = "app")
public class AppProperties
{
	private String name;
	private int maxConnections;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getMaxConnections()
	{
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections)
	{
		this.maxConnections = maxConnections;
	}

	@Override
	public String toString()
	{
		return "AppProperties{name='" + name + "', maxConnections=" + maxConnections + "}";
	}
}
