package org.ship.shipservice.manager.dao;

import java.util.List;

import org.ship.shipservice.entity.Coupon;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author lyhc
 * 
 */
public interface MorderDao extends CrudRepository<Coupon, Long> {
	@Modifying
	@Query(value="select o.id,o.product_name,o.num,o.status,o.order_no,o.book_time,o.book_addr,o.create_time,u.phone,u.user_name,u.ship_name "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11 "
			+ "order by o.create_time desc limit ?1,?2", nativeQuery=true)
	List<Object[]> queryMakeOrderList(Integer start, Integer size);
	
	@Query(value="select count(1) "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11", nativeQuery=true)
	int queryMakeOrderTotal();
	
	@Modifying
	@Query(value="select o.id,o.product_name,o.num,o.status,o.order_no,o.book_time,o.book_addr,o.create_time,u.phone,u.user_name,u.ship_name "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11 "
			+ "order by o.create_time desc limit ?1,?2", nativeQuery=true)
	List<Object[]> queryOrderList(Integer start, Integer size);
	
	@Query(value="select count(1) "
			+ "from t_order o, t_user u where o.user_id=u.id and o.status=11", nativeQuery=true)
	int queryOrderTotal();
}
