package practice.phase5;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class CartService
{
	private final Cart brokenCart;
	private final ObjectProvider<Cart> cartProvider;

	public CartService(Cart brokenCart, ObjectProvider<Cart> cartProvider)
	{
		this.brokenCart = brokenCart;
		this.cartProvider = cartProvider;
	}

	public void demo()
	{
		brokenCart.add("A");
		Cart brokenAgain = brokenCart;
		brokenAgain.add("B");
		System.out.println("brokenCart(field, reused every call): " + brokenCart.getItems());

		Cart fresh1 = cartProvider.getObject();
		fresh1.add("X");
		System.out.println("fresh1: " + fresh1.getItems());

		Cart fresh2 = cartProvider.getObject();
		fresh2.add("Y");
		System.out.println("fresh2: " + fresh2.getItems());
	}
}
