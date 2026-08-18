package practice.phase11.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class MiniConnectionPool
{
	private final BlockingQueue<Connection> pool;

	public MiniConnectionPool(String url, String user, String password, int size) throws SQLException
	{
		this.pool = new LinkedBlockingDeque<>(size);
		for(int i = 0; i < size; i++)
		{
			pool.add(DriverManager.getConnection(url, user, password));
		}
	}

	public Connection borrow() throws InterruptedException
	{
		return pool.take();
	}

	public void release(Connection conn)
	{
		pool.offer(conn);
	}
}



