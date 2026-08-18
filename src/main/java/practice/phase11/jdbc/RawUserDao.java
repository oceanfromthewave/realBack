package practice.phase11.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RawUserDao
{
	private final String url;
	private final String user;
	private final String password;

	public RawUserDao(String url, String user, String password)
	{
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public void insert(long id, String name, String email)
	{
		String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";

		try(Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setLong(1, id);
			ps.setString(2, name);
			ps.setString(3, email);
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public User findById(long id)
	{
		String sql = "SELECT id, name, email FROM users WHERE id = ?";

		try(Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement ps = conn.prepareStatement(sql))
		{
			ps.setLong(1, id);

			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
				}
				return null;
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
}