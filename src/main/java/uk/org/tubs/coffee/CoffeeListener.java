package uk.org.tubs.coffee;

import uk.org.tubs.coffee.impl.CoffeeStatus;

public interface CoffeeListener {
	public void coffeStatusChanged(CoffeeStatus s);
}
