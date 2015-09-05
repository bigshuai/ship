package org.ship.shipservice.repository;

import java.util.Date;
import java.util.List;

import org.ship.shipservice.entity.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderDao extends CrudRepository<Order, Long>{
	@Modifying
	@Query("select o from Order o where o.userId=?1 and o.status=?2 order by o.createTime desc")
	List<Order> findByUserId(Integer userId,Integer status);
	
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
