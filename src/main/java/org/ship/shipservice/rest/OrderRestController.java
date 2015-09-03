package org.ship.shipservice.rest;

import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.service.order.OrderService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="/api/v1/order")
public class OrderRestController {
	private static Logger logger = LoggerFactory.getLogger(OrderRestController.class);
	
	@Autowired
    private OrderService orderService;
	
	/**
	 * 获取订单列表
	 * @param userId status
	 * @return
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public String getOrderList(@RequestParam("userId") Integer userId,@RequestParam("status") Integer status) {
		if(userId==0){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			ResultList orders = orderService.findOrderByUserId(userId, status);
			return CommonUtils.printListStr(orders.getDataList());
		}
	}
}
