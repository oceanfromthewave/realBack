package practice.phase8.dispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MiniDispatcherServlet extends HttpServlet
{
	public interface Handler
	{
		void handle(HttpServletRequest req, HttpServletResponse resp) throws IOException;
	}

	private final Map<String, Handler> handlerMapping = new HashMap<>();

	public void registerHandler(String path, Handler handler)
	{
		handlerMapping.put(path, handler);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String path = req.getPathInfo();
		Handler handler = handlerMapping.get(path);

		if(handler == null)
		{
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No handler for" + path);
			return;
		}
		handler.handle(req, resp);
	}
}
