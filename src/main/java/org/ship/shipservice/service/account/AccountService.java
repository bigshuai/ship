/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.service.account;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.ship.shipservice.entity.User;
import org.ship.shipservice.repository.OrderDao;
import org.ship.shipservice.repository.UserDao;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired  
    private RedisTemplate<String, String> redisTemplate;  

	public User findUserByPhoneAndPassword(String phone,String password) {
		return userDao.findByPhoneAndPassword(phone,password);
	}

	public User registerUser(User user) {
		//创建用户账号
		User u = userDao.save(user);
		userDao.createUserBank(user.getId(), "0", 1, "");
		return u;
	}

	public Map<String, String> orderVerify(Long userId, String orderNo, boolean sendSms){
		Map<String, String> result = new HashMap<String, String>();
		String money = orderDao.queryOrderMoney(orderNo, userId);
		String fond = userDao.getUserFund(userId);
		if(StringUtils.isEmpty(fond)){
			result.put("", "用户未开通余额支付。");
			return result;
		}
		
		if(Double.valueOf(money) > Double.valueOf(fond)){
			result.put("", "用户余额不足。");
			return result;
		}
		//发送短信
		if(sendSms){
			String phone = userDao.getUserPhone(userId);
			String code = CommonUtils.createRandom(true, 6);
			redisTemplate.opsForValue().set(phone, code,30l,TimeUnit.MINUTES);//验证码30分钟失效
			String message=CommonUtils.sendMessage(phone, code);
			if(!message.split(",")[0].equals("0")){
			}else{
				result.put("", "短信发送失败。");
				return result;
			}
		}
		return result;
	}
	
	public Map<String, String> balancePayment(Long userId, String orderNo,
			String code) {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String> res = this.orderVerify(userId, orderNo, false);
		if(!StringUtils.isEmpty(res.get("msg"))){
			return res;
		}
		//扣除余额  更新订单状态
		String amount = orderDao.queryOrderMoney(orderNo, userId);
		int r = userDao.deductCharge(userId, amount);
		if(r > 0){
			int rr = orderDao.updateOrderStatus(21, userId, orderNo);
			if(rr > 0){
				//记录消费日志
				int rrr = orderDao.insertConsumeLog(userId, orderNo, amount, amount, 1, code, 1);
				if(rrr > 0){
					
				}else{
					throw new RuntimeException("支付失败，请稍后再试。");
				}
			}else{
				throw new RuntimeException("支付失败，请稍后再试。");
			}
		}else{
			result.put("msg", "支付失败，请稍后再试。");
		}
		return result;
	}
	
	public int updateUser(String phone,String password) {
		return userDao.updateUser(phone,password);
	}
	public User updateName(User user) {
		return userDao.save(user);
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

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
