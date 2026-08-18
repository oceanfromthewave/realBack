package practice.phase9.conditional;

public class DemoAutoConfiguration
{
	@Bean
	@ConditionalOnClass("java.sql.Driver")
	public String jdbcFeature()
	{
		return "JDBC feature enabled (java.sql.Driver found)";
	}

	@Bean
	@ConditionalOnClass("com.fake.NotExistLibrary")
	public String fakeFeature()
	{
		return "this should never print";
	}

	@Bean
	public String alwaysOnFeature()
	{
		return "no condition, always registered";
	}

	@Bean
	@Profile("dev")
	public String devDataSource()
	{
		return "H2 in-memory DataSource (dev)";
	}

	@Bean
	@Profile("prod")
	public String prodDataSource()
	{
		return "Oracle DataSource (prod)";
	}
}
