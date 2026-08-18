package practice.phase7.plainjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Phase 7 인프라 코드 (내가 작성) — DB 연결/스키마 준비만 담당.
 * 트랜잭션 로직 자체는 여기 없음. AccountTransferService에서 구현.
 */
public final class Db
{
	private static final String URL = "jdbc:h2:mem:phase7;DB_CLOSE_DELAY=-1";

	private Db() {}

	public static Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(URL, "sa", "");
	}

	public static void initSchema() throws SQLException
	{
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement())
		{
			stmt.execute("DROP TABLE IF EXISTS accounts");
			stmt.execute("CREATE TABLE accounts (id INT PRIMARY KEY, balance INT NOT NULL)");
			stmt.execute("INSERT INTO accounts VALUES (1, 1000)");
			stmt.execute("INSERT INTO accounts VALUES (2, 500)");
		}
	}

	public static int getBalance(int accountId) throws SQLException
	{
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement();
			 var rs = stmt.executeQuery("SELECT balance FROM accounts WHERE id = " + accountId))
		{
			rs.next();
			return rs.getInt("balance");
		}
	}
}
