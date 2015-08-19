package org.ship.shipservice.rest;

import org.ship.shipservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

/**
 * 一键登陆
 * @author zhf
 *
 */
@RestController
@RequestMapping(value = "/api/v1/login")
public class LoginRestController {
	private static Logger logger = LoggerFactory.getLogger(LoginRestController.class);
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public String login (@PathVariable("id") Integer id){
//		CommonUtils.sendMessage(id, CommonUtils.createRandom(true, 6));
		return CommonUtils.createRandom(true, 6);
	}
}
