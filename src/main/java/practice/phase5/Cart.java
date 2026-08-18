package practice.phase5;

import java.util.ArrayList;
import java.util.List;

public class Cart
{
	private final List<String> items = new ArrayList<>();

	public void add(String item)
	{
		items.add(item);
	}

	public List<String> getItems()
	{
		return items;
	}
}
