package practice.phase7.plainjdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountTransferService
{
	public void transfer(int fromId, int toId, int amount) throws SQLException
	{
		try(Connection conn = Db.getConnection())
		{
			conn.setAutoCommit(false);

			try
			{
				int fromBalance = Db.getBalance(fromId);
				if(fromBalance < amount)
				{
					throw new RuntimeException("잔액 부족: id=" + fromId);
				}

				try(PreparedStatement withdraw = conn.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE id = ?"))
				{
					withdraw.setInt(1, amount);
					withdraw.setInt(2, fromId);
					withdraw.executeUpdate();
				}

				try(PreparedStatement deposit = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?"))
				{
					deposit.setInt(1, amount);
					deposit.setInt(2, toId);
					deposit.executeUpdate();
				}

				conn.commit();
			}
			catch(RuntimeException e)
			{
				conn.rollback();
				throw e;
			}
			finally
			{
				conn.setAutoCommit(true);
			}
		}
	}
}
