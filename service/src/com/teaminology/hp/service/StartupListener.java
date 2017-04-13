package com.teaminology.hp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.teaminology.hp.dao.ITermDao;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private ITermDao termDao;
	
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		//Update all tokens
		termDao.deleteAllTokens();
	}
}