package org.ship.shipservice.rest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.JsonUtil;
import org.ship.shipservice.utils.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.JsonMapper;

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
	private static JsonMapper mapper = JsonMapper.nonDefaultMapper();
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
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
			map.put("status",MyConstant.JSON_RETURN_CODE_400);
			map.put("msg",MyConstant.JSON_RETURN_MESSAGE_400);
			logger.info(MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = accountService.findUserByPhoneAndPassword(phone,password);
			if(user!=null){
				map.put("status",MyConstant.JSON_RETURN_CODE_200);
				map.put("msg","登陆成功");
				map.put("result", mapper.toJson(user));
				logger.info("登陆成功");
			}else{
				map.put("status",MyConstant.JSON_RETURN_CODE_500);
				map.put("msg","手机号或密码错误");
//				throw new RestException(HttpStatus.NOT_FOUND, "手机号或密码错误");
			}
		}
		json = JsonUtil.map2json(map);
		return json;
	}
	/**
	 * 获取验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/code", method = RequestMethod.POST)
	public String getCode(@RequestParam("phone") Integer phone) {
		// CommonUtils.sendMessage(phone, CommonUtils.createRandom(true, 6));
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(phone==0){
			map.put("status",MyConstant.JSON_RETURN_CODE_400);
			map.put("msg",MyConstant.JSON_RETURN_MESSAGE_400);
			logger.info(MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			map.put("status",MyConstant.JSON_RETURN_CODE_200);
			map.put("msg",CommonUtils.createRandom(true, 6));
		}
		json = JsonUtil.map2json(map);
		return json;
	}
	/**
	 * 重置密码
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/update",params={"phone","password"},method = RequestMethod.POST)
	public String updatePwd(@RequestParam("phone") String phone,@RequestParam("password") String password){
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
			map.put("status",MyConstant.JSON_RETURN_CODE_400);
			map.put("msg",MyConstant.JSON_RETURN_MESSAGE_400);
			logger.info(MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			int count = accountService.updateUser(phone,password);
			if(count!=0){
				map.put("status",MyConstant.JSON_RETURN_CODE_200);
				map.put("msg","密码更新成功");
			}else{
				map.put("status",MyConstant.JSON_RETURN_CODE_500);
				map.put("msg","用户不存在");
			}
		}
		json = JsonUtil.map2json(map);
		return json;
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
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(password)){
			map.put("status",MyConstant.JSON_RETURN_CODE_400);
			map.put("msg",MyConstant.JSON_RETURN_MESSAGE_400);
			logger.info(MyConstant.JSON_RETURN_MESSAGE_400);
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
					map.put("status",MyConstant.JSON_RETURN_CODE_200);
					map.put("msg","用户注册成功");
					map.put("result", mapper.toJson(user));
				}else{
					map.put("status",MyConstant.JSON_RETURN_CODE_500);
					map.put("msg","用户注册失败");
				}
			}else{
				map.put("status",MyConstant.JSON_RETURN_CODE_200);
				map.put("msg","用户已存在");
			}
			
		}
		json = JsonUtil.map2json(map);
		return json;
	}
	
}
