package practice.phase6;

public class OrderServiceImpl implements OrderService
{
	@Override
	public void placeOrder(String orderId)
	{
		System.out.println("주문 처리" + orderId);
	}

	@Override
	public void cancelOrder(String orderId)
	{
		System.out.println("주문 취소" + orderId);
	}
}
