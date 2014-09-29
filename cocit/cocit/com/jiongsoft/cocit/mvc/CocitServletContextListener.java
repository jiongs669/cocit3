package com.jiongsoft.cocit.mvc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

import com.jiongsoft.cocit.Cocit;

public class CocitServletContextListener implements ServletContextListener, HttpSessionActivationListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Cocit.init(event.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		Cocit.destroy(event.getServletContext());
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent event) {

	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent event) {

	}
}
