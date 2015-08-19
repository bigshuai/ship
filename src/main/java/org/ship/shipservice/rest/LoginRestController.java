package org.ship.shipservice.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.service.account.IAccountService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.JsonUtil;
import org.ship.shipservice.utils.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

/**
 * 一键登陆
 * 
 * @author zhf
 *
 */
@RestController
@RequestMapping(value = "/api/v1/login")
public class LoginRestController {
	private static Logger logger = LoggerFactory
			.getLogger(LoginRestController.class);
	@Autowired
    private IAccountService accountService;
	/**
	 * 用户登陆
	 * @param phone
	 * @param password
	 * @return
	 */
	@RequestMapping( method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public String login(@PathVariable("phone") Integer phone,@PathVariable("password") String password) {
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(phone==0||StringUtils.isEmpty(password)){
			map.put("status",MyConstant.JSON_RETURN_CODE_400);
			map.put("msg",MyConstant.JSON_RETURN_MESSAGE_400);
			logger.info(MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = accountService.getUser(phone,password);
			if(user!=null){
				map.put("status",MyConstant.JSON_RETURN_CODE_200);
				map.put("msg","登陆成功");
			}else{
				map.put("status",MyConstant.JSON_RETURN_CODE_200);
				map.put("msg","用户不存在");
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
	@RequestMapping( method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public String getCode(@PathVariable("phone") Integer phone) {
		// CommonUtils.sendMessage(phone, CommonUtils.createRandom(true, 6));
		return CommonUtils.createRandom(true, 6);
	}
}
