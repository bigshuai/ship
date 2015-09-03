package org.ship.shipservice.rest;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	/**
	 * 用户登陆
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/login",params={"phone","password"},method = RequestMethod.POST)
	public String login(@RequestParam("phone") String phone,@RequestParam("password") String password) {
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = accountService.findUserByPhoneAndPassword(phone,password);
			if(user!=null){
				user.setPassword("");
				String token = CommonUtils.getMD5(user.getId()+System.currentTimeMillis()+"");
				servletContext.setAttribute(user.getId()+"", token);
				user.setToken(token);
				return CommonUtils.printObjStr(user, "200", "用户登陆成功");
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);

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
			return CommonUtils.printObjStr(CommonUtils.createRandom(true, 6), "200", "验证码");
		}
	}
	/**
	 * 重置密码
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/update",params={"phone","password","id","token"},method = RequestMethod.POST)
	public String updatePwd(@RequestParam("phone") String phone,@RequestParam("password") String password,@RequestParam("id") String id ,@RequestParam("token") String token,HttpSession httpSession){
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			if(servletContext.getAttribute(id).equals(token)){
				int count = accountService.updateUser(phone,password);
				if(count!=0){
					return CommonUtils.printStr( "200", "密码更新成功");
				}else{
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
			}
		}
	}
	/**
	 * 用户注册
	 * @param phone
	 * @param password
	 * @param username
	 * @param shipname
	 * @param shipno
	 * @return
	 */
	@RequestMapping(value="/register",params={"phone","password","username","shipname","shipno"},method = RequestMethod.POST)
	public String register(@RequestParam("phone") String phone,@RequestParam("password") String password,@RequestParam("username") String username,@RequestParam("shipname") String shipname,@RequestParam("shipno") String shipno){
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = new User();
			user.setPhone(phone);
			user.setPassword(password);
			user.setUsername(username);
			user.setShipname(shipname);
			user.setShipno(shipno);
			user.setRegisterDate(new Date());
			user.setBalance(new BigDecimal(0.00d));
			if(accountService.findByPhone(phone)==null){
				user = accountService.registerUser(user);
				if(user.getId()!=0){
					user.setPassword("");
					String token = CommonUtils.getMD5(user.getId()+System.currentTimeMillis()+"");
					servletContext.setAttribute(user.getId()+"", token);
					user.setToken(token);
					return CommonUtils.printObjStr(user, "200", "用户注册成功");
				}else{
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
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
