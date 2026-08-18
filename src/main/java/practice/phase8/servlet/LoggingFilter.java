package practice.phase8.servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class LoggingFilter implements Filter
{
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		long start = System.currentTimeMillis();
		System.out.println("[FILTER] 요청 시작");
		chain.doFilter(request, response);

		long elapsed = System.currentTimeMillis() - start;
		System.out.println("[FILTER] 요청 종료, " + elapsed + "ms");
	}
}
