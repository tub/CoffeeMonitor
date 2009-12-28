package uk.org.tubs.coffee;

import java.io.File;

import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;

public class Main {
	public static void main(String args[]) {
		// create and configure beans
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
		
		if(args.length > 0 && args[0] != null && !"".equals(args[0].trim())){
			File confFile = new File(args[0]);
			if(!confFile.exists())
				throw new RuntimeException("Couldn't find specified config file: "+args[0]);
			if(!confFile.isFile())
				throw new RuntimeException("Specified config file is a directory, should be a properties file: " +args[0]);
			
			FileSystemResource conf = new FileSystemResource(confFile);
			PropertyOverrideConfigurer propertyOverrideConfigurer = new PropertyOverrideConfigurer();
			propertyOverrideConfigurer.setLocation(conf);
			context.addBeanFactoryPostProcessor(propertyOverrideConfigurer);
		}
		context.setConfigLocation("context.xml");
		context.refresh();
	}
}
