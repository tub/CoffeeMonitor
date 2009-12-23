package uk.org.tubs.coffee;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String args[]){
		// create and configure beans
		ApplicationContext context =
		    new ClassPathXmlApplicationContext(new String[] {"context.xml"});
	}
}
