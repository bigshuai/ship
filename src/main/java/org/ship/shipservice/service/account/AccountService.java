/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.service.account;


import org.ship.shipservice.entity.User;
import org.ship.shipservice.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户管理类.
 * 
 * @author zhf
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class AccountService {

	private UserDao userDao;

	public User findUserByPhoneAndPassword(String phone,String password) {
		return userDao.findByPhoneAndPassword(phone,password);
	}

	public User registerUser(User user) {
		return userDao.save(user);
	}

	public int updateUser(String phone,String password) {
		return userDao.updateUser(phone,password);
	}
	public int updateName(String phone,String username) {
		return userDao.updateName(phone,username);
	}
	public User findByPhone(String phone){
		return userDao.findByPhone(phone);
	}
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}
	


}
