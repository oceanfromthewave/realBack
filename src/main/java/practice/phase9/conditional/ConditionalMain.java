package practice.phase9.conditional;

public class ConditionalMain
{
	public static void main(String[] args) throws Exception
	{
		MiniAutoConfigurationProcessor processor = new MiniAutoConfigurationProcessor("dev");
		processor.process(new DemoAutoConfiguration());

		System.out.println("jdbcFeature bean = " + processor.getBean(String.class));
	}
}
