package practice.phase7.plainjdbc;

import java.sql.SQLException;

public class PlainJdbcMain
{
	public static void main(String[] args) throws SQLException
	{
		Db.initSchema();

		AccountTransferService service = new AccountTransferService();

		System.out.println("--- 정상 이체 ---");
		service.transfer(1, 2, 300);
		System.out.println("id=1 잔액: " + Db.getBalance(1));
		System.out.println("id=2 잔액: " + Db.getBalance(2));

		System.out.println("--- 잔액부족 이체(실패) ---");
		try
		{
			service.transfer(1, 2, 9999);
		}
		catch(RuntimeException e)
		{
			System.out.println("예외 발생: " + e.getMessage());
		}
		System.out.println("id=1 잔액: " + Db.getBalance(1));
		System.out.println("id=2 잔액: " + Db.getBalance(2));
	}
}
