/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.service.account;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.repository.TaskDao;
import org.ship.shipservice.repository.UserDao;
import org.ship.shipservice.service.ServiceException;
import org.ship.shipservice.service.account.ShiroDbRealm.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Clock;
import org.springside.modules.utils.Encodes;

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

	public User findByPhone(String phone){
		return userDao.findByPhone(phone);
	}
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
