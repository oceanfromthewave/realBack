package practice.phase11.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RawJdbcMain
{
	public static void main(String[] args) throws Exception
	{
		String url = "jdbc:h2:mem:phase11db;DB_CLOSE_DELAY=-1";

		try(Connection conn = DriverManager.getConnection(url, "sa", ""); Statement st = conn.createStatement())
		{
			st.execute("CREATE TABLE users (id BIGINT PRIMARY KEY, name VARCHAR(50), email VARCHAR(100))");
		}

		RawUserDao dao = new RawUserDao(url, "sa", "");
		dao.insert(1, "jaehyun", "a@b.com");

		User found = dao.findById(1);
		System.out.println("found: " + found);

		long start = System.nanoTime();
		for(int i = 0; i < 100; i++)
		{
			dao.findById(1);
		}
		long elapsedMs = (System.nanoTime() - start) / 1_000_000;
		System.out.println("100 calls (new connection every time) took " + elapsedMs + "ms");
	}
}
