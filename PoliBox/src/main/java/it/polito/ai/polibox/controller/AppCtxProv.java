package it.polito.ai.polibox.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AppCtxProv implements ApplicationContextAware {
	private static ApplicationContext context; 
	public static ApplicationContext getApplicationContext() { 
		return context; 
		} 
	@Override public void setApplicationContext(ApplicationContext ctx) { context = ctx; } 
}
