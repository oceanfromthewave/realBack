package practice.phase6;

public class SelfInvocationDemo
{

	public void outer()
	{
		System.out.println("outer 호출");
		this.inner();  // self invocation — 프록시 거치지 않음
	}

	public void inner()
	{
		System.out.println("inner 호출");
	}
}
