package org.ship.shipservice.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.entity.Order;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

/**
 * 一键登陆
 * 
 * @author zhf
 *
 */
@RestController
@RequestMapping(value="/api/v1/user")
public class LoginRestController {
	private static Logger logger = LoggerFactory
			.getLogger(LoginRestController.class);
	@Autowired 
	private ServletContext servletContext;
	@Autowired
    private AccountService accountService;
	@Autowired  
    private RedisTemplate<String, String> redisTemplate;  
	/**
	 * 用户登陆
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String login(@RequestParam("phone") String phone,@RequestParam("password") String password,@RequestParam("deviceToken") String deviceToken) {
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)||StringUtils.isEmpty(deviceToken)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = accountService.findUserByPhoneAndPassword(phone,password);
			if(user!=null){
				user.setPassword(null);
				String token = CommonUtils.getMD5(deviceToken+System.currentTimeMillis()+"");
				servletContext.setAttribute(user.getId()+"", token);
				user.setToken(token);
				int couponCount = user.getCouponList().size();
				int orderCount = 0;
				if (user.getOrderList().size() != 0) {
					for (Order order : user.getOrderList()) {
						if (order.getStatus() != 4) {
							orderCount = orderCount + 1;
						}
					}
				}
				user.setCouponCount(couponCount);
				user.setOrderCount(orderCount);
				user.setCouponList(null);
				user.setOrderList(null);
				logger.info("用户:"+user.getPhone()+"登陆");
				return CommonUtils.printObjStr(user, 200, "用户登陆成功");
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, "账号或密码错误");
			}
		}
	}
	/**
	 * 获取验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/code", method = RequestMethod.POST)
	public String getCode(@RequestParam("phone") String phone) {
		if(StringUtils.isEmpty(phone)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			String code = CommonUtils.createRandom(true, 6);
			redisTemplate.opsForValue().set(phone, code,30l,TimeUnit.MINUTES);//验证码30分钟失效
			String message=CommonUtils.sendMessage(phone, code);
			if(message.split(",")[0].equals("0")){
				return CommonUtils.printObjStr(code, 200, "验证码");
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, "验证码发送失败");
			}
		}
	}
	/**
	 * 重置密码
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/update",params={"phone","password","code"},method = RequestMethod.POST)
	public String updatePwd(@RequestParam("phone") String phone,@RequestParam("password") String password,@RequestParam("code") String code){
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			if(code.equals(redisTemplate.boundValueOps(phone).get())){
				int count = accountService.updateUser(phone,password);
				if(count!=0){
					return CommonUtils.printStr( "200", "密码更新成功");
				}else{
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, "验证码已失效");
			}
		}
	}
	/**
	 * 用户注册
	 * @param param={phone:123475,password:132245,username:12,shipname:32,shipno=87}
	 * @return
	 */
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public String register(@RequestParam("param") String param){
		User user = JSON.parseObject(param, User.class);
		if(user.getPhone()==null||user.getPassword()==null){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			user.setRegisterDate(new Date());
			user.setBalance(new BigDecimal(0.00d));
			String code = redisTemplate.boundValueOps(user.getPhone()).get();
			if(code!=null&&!code.equals("")){
				if(accountService.findByPhone(user.getPhone())==null&&code.equals(user.getCode())){
					user = accountService.registerUser(user);
					if(user.getId()!=0){
						user.setPassword("");
						String token = CommonUtils.getMD5(user.getId()+System.currentTimeMillis()+"");
						servletContext.setAttribute(user.getId()+"", token);
						user.setToken(token);
						return CommonUtils.printStr("200", "用户注册成功");
					}else{
						return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
					}
				}else{
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, "验证码失效");
			}
		}
	}
	/**
	 * 登出
	 * @param id
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/logout",params="{id,token}",method=RequestMethod.POST)
	public String logout(@RequestParam("id") String id,@RequestParam("token") String token){
		if(StringUtils.isEmpty(id)||StringUtils.isEmpty(token)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else if(servletContext.getAttribute(id).equals(token)){
			servletContext.removeAttribute(id);
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_200, MyConstant.JSON_RETURN_MESSAGE_200);
		}else{
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}
	}
}
