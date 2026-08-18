package practice.phase11.jdbctemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import practice.phase11.jdbc.User;

import javax.sql.DataSource;
import java.util.List;

public class JdbcUserDao
{
	private final JdbcTemplate jdbcTemplate;

	public JdbcUserDao(DataSource dataSource)
	{
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void insert(long id, String name, String email)
	{
		jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (?, ?, ?)", id, name, email);
	}

	public User findById(long id)
	{
		List<User> result = jdbcTemplate.query("SELECT id, name, email FROM users WHERE id = ?",
				(rs, rowNum) -> new User(rs.getLong("id"), rs.getString("name"), rs.getString("email")), id);
		return result.isEmpty() ? null : result.get(0);
	}
}
