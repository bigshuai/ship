/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.repository;

import org.ship.shipservice.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
	User findByPhoneAndPassword(String phone,String password);
	User findByPhone(String phone);
	@Modifying
	@Query("update User u set u.password = ?2 where u.phone = ?1 and u.status=0")
	int updateUser(String phone, String password);
	@Modifying
	@Query("update User u set u.username = ?2 where u.phone = ?1 and u.status=0")
	int updateName(String phone, String username);
	User save(User user);
}
