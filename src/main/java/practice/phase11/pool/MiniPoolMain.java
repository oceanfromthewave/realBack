package practice.phase11.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MiniPoolMain
{
	public static void main(String[] args) throws Exception
	{
		String url = "jdbc:h2:mem:phase11pool;DB_CLOSE_DELAY=-1";

		try(Connection conn = DriverManager.getConnection(url, "sa", ""); Statement st = conn.createStatement())
		{
			st.execute("CREATE TABLE users (id BIGINT PRIMARY KEY, name VARCHAR(50), email VARCHAR(100))");
			st.execute("INSERT INTO users VALUES (1, 'jaehyun', 'a@b.com')");
		}

		MiniConnectionPool pool = new MiniConnectionPool(url, "sa", "", 5);

		long start = System.nanoTime();
		for(int i = 0; i < 100; i++)
		{
			Connection conn = pool.borrow();
			try(PreparedStatement ps = conn.prepareStatement("SELECT id, name, email FROM users WHERE id = ?"))
			{
				ps.setLong(1, 1);
				try(ResultSet rs = ps.executeQuery())
				{
					rs.next();
				}
			}
			finally
			{
				pool.release(conn);
			}
		}
		long elapsedMs = (System.nanoTime() - start) / 1_000_000;
		System.out.println("100 calls (pooled connection) took " + elapsedMs + "ms");
	}
}
