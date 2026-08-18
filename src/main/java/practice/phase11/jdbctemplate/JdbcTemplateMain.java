package practice.phase11.jdbctemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import practice.phase11.jdbc.User;

public class JdbcTemplateMain
{
	public static void main(String[] args)
	{
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:h2:mem:phase11jt;DB_CLOSE_DELAY=-1");
		config.setUsername("sa");
		config.setPassword("");
		HikariDataSource dataSource = new HikariDataSource(config);

		JdbcTemplate schema = new JdbcTemplate(dataSource);
		schema.execute("CREATE TABLE users (id BIGINT PRIMARY KEY, name VARCHAR(50), email VARCHAR(100))");

		JdbcUserDao dao = new JdbcUserDao(dataSource);
		dao.insert(1, "jaehyun", "a@b.com");

		User found = dao.findById(1);
		System.out.println("found: " + found);

		try
		{
			dao.insert(1, "duplicate", "dup@b.com");
		}
		catch(DuplicateKeyException e)
		{
			System.out.println("caught DuplicateKeyException: " + e.getClass().getSimpleName());
		}
		catch(DataAccessException e)
		{
			System.out.println("caught some other DataAccessException: " + e.getClass().getSimpleName());
		}

		dataSource.close();
	}
}
