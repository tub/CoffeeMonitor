package uk.org.tubs.coffee;

public interface CoffeeMonitor {
	public void addCoffeeListener(CoffeeListener l);
	public boolean removeCoffeeListener(CoffeeListener l);
}
