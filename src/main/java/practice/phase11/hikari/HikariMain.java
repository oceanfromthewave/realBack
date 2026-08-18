package practice.phase11.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HikariMain
{
	public static void main(String[] args) throws Exception
	{
		String url = "jdbc:h2:mem:phase11hikari;DB_CLOSE_DELAY=-1";

		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername("sa");
		config.setPassword("");
		config.setMaximumPoolSize(5);

		DataSource dataSource = new HikariDataSource(config);

		try(Connection conn = dataSource.getConnection(); Statement st = conn.createStatement())
		{
			st.execute("CREATE TABLE users (id BIGINT PRIMARY KEY, name VARCHAR(50), email VARCHAR(100))");
			st.execute("INSERT INTO users VALUES (1, 'jaehyun', 'a@b.com')");
		}

		long start = System.nanoTime();
		for(int i = 0; i < 100; i++)
		{
			try(Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT id, name, email FROM users WHERE id = ?"))
			{
				ps.setLong(1, 1);
				try(ResultSet rs = ps.executeQuery())
				{
					rs.next();
				}
			}
		}
		long elapsedMs = (System.nanoTime() - start) / 1_000_000;
		System.out.println("100 calls (HikariCP) took " + elapsedMs + "ms");

		((HikariDataSource) dataSource).close();
	}
}
