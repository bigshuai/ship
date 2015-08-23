package org.ship.shipservice.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.entity.ConsumeInfo;
import org.ship.shipservice.entity.Order;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.service.account.ConsumeInfoService;
import org.ship.shipservice.utils.JsonUtil;
import org.ship.shipservice.utils.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户中心
 * @author zhf
 *
 */
@RestController
@RequestMapping(value="/api/v1/userInfo")
public class UserInfoRestController {
	private static Logger logger = LoggerFactory
			.getLogger(LoginRestController.class);
	@Autowired
    private AccountService accountService;
	@Autowired
	private ConsumeInfoService consumeInfoService;
	
	@RequestMapping(value="/list",params={"phone"},method = RequestMethod.POST)
	public String userInfo(@RequestParam("phone") String phone){
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(StringUtils.isEmpty(phone)){
			map.put("status",MyConstant.JSON_RETURN_CODE_400);
			map.put("msg",MyConstant.JSON_RETURN_MESSAGE_400);
			logger.info(MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = accountService.findByPhone(phone);
			int couponCount = user.getCouponList().size();
			int orderCount = 0;
			if(user.getOrderList().size()!=0){
				for(Order order:user.getOrderList()){
					if(order.getStatus()!=4){
						orderCount=orderCount+1;
					}
				}
			}
			map.put("status", MyConstant.JSON_RETURN_CODE_200);
			map.put("url", user.getPicture()!=null?user.getPicture().getUrl():"");
			map.put("username", user.getUsername());
			map.put("account", user.getAccount().getBalance());
			map.put("couponCount", couponCount);
			map.put("orderCount", orderCount);
		}
		json = JsonUtil.map2json(map);
		return json;
	}
	@RequestMapping(value="/consumeInfo",params={"accountId"},method=RequestMethod.POST )
	public String consumeInfo(@RequestParam("accountId") Integer accountId){
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(accountId==0){
			map.put("status",MyConstant.JSON_RETURN_CODE_400);
			map.put("msg",MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			List<ConsumeInfo> cList= consumeInfoService.findByAccountId(accountId, 0);
		}
		json = JsonUtil.map2json(map);
		return json;
	}
}
