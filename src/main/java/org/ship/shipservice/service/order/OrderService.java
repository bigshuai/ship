package org.ship.shipservice.service.order;

import java.util.List;

import org.ship.shipservice.repository.OrderDao;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author zhf
 *
 */
@Component
@Transactional
public class OrderService {
	private OrderDao orderDao;
	
	public ResultList findOrderByUserId(Integer userId ,Integer status){
		ResultList r = new ResultList();
		List<Order> list = orderDao.findByUserId(userId, status);
		r.setDataList(list);
		return r;
	}
   
	public OrderDao getOrderDao() {
		return orderDao;
	}
	 @Autowired
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
}
