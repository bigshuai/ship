package org.ship.shipservice.repository;

import java.util.Date;

import org.ship.shipservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderDao extends CrudRepository<Order, Long>{
	Page<Order> findByUserIdAndStatus(Integer userId,Integer status,Pageable pageable);
	
	@Modifying
	@Query(value="insert INTO t_order(user_id,os_id,product_id,product_name,type,money,price,num,`status`,order_no,"
			+ "sft_order_no,session_token,book_time,create_time) "
			+ "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,now())", nativeQuery=true)
	public int addOrder(Long userId, Long osId,Long proId, String proName,Integer type, String money, String price,
			Integer num, Integer status,String orderNo, String sftOrderNo, String sessionToken, Date bookTime);
	
	@Modifying
	@Query(value="insert INTO t_order(user_id,os_id,product_id,product_name,type,money,price,num,`status`,order_no,"
			+ "sft_order_no,session_token,book_time,create_time) "
			+ "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,now())", nativeQuery=true)
	public int updateOrder();
}
