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
	
	@Modifying
	@Query(value="UPDATE t_user_bank set fund=fund+?2 where user_id=?1", nativeQuery=true)
	public int recharge(Long userId, String amount);
	
	@Modifying
	@Query(value="UPDATE t_user_bank set fund=fund-?2 where user_id=?1", nativeQuery=true)
	public int deductCharge(Long userId, String amount);
	
	@Query(value="select fund from t_user_bank where user_id=?1 and status=1", nativeQuery=true)
	public String getUserFund(Long userId);
	
	@Query(value="select phone from t_user where id=?1 and status=0", nativeQuery=true)
	public String getUserPhone(Long userId);
	
	@Modifying
	@Query(value="insert into t_user_bank(user_id,fund,status,pwd) values(?1,?2,?3,?4)", nativeQuery=true)
	public int createUserBank(Long userId, String fund, Integer status, String pwd);
}
