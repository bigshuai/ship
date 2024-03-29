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
			+ "sft_order_no,session_token,book_time,book_addr,create_time,get_coupon_id) "
			+ "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,?13,?14,now(),?15)", nativeQuery=true)
	public int addOrder(Long userId, Long osId,Long proId, String proName,Integer type, String money, String price,
			Integer num, Integer status,String orderNo, String sftOrderNo, String sessionToken, Date bookTime, String bookAddr,Long getCouponId);
	
	@Modifying
	@Query(value="update t_order set money=?1,price=?2,num=?3,`status`=?4,sft_order_no=?5,session_token=?6,get_coupon_id=?8,type=1 "
			+ " where order_no=?7", nativeQuery=true)
	public int updateOrder(String money, String price,
			Integer num, Integer status, String sftOrderNo, String sessionToken, String orderNo, Long getCouponId);
	
	@Modifying
	@Query(value="update t_order set status=?1 where user_id=?2 and order_no=?3", nativeQuery=true)
	public int updateOrderStatus(Integer status, Long userId, String orderNo);
	
	@Modifying
	@Query(value="update t_order set status=?1,pay_type=2 where order_no=?2", nativeQuery=true)
	public int updateUserOrderStatus(Integer status,  String orderNo);
	
	@Modifying
	@Query(value="update t_order set status=?1,pay_type=1 where order_no=?2", nativeQuery=true)
	public int updateBankOrderStatus(Integer status,  String orderNo);
	
	@Modifying
	@Query(value="select o.id,o.os_id,s.name,o.product_id,o.product_name,o.type,o.money,o.price,o.num,"
			+ "o.status,o.order_no,o.sft_order_no,o.book_time,o.create_time,s.pic_url,o.consume_code "
			+ "from t_order o, t_oil_station s where o.os_id=s.id and o.user_id=?1 and (o.status=?2 or o.status=?3"
			+ " or o.status=?4 or o.status=?5 or o.status=?6 or o.status=?7 or o.status=?8 or o.status=?9 or o.status=?10"
			+ " or o.status=?11 or o.status=?12 or o.status=?13) "
			+ "order by o.create_time desc limit ?14,?15", nativeQuery=true)
	List<Object[]> queryOrderForUserId(Long userId,Integer s1,Integer s2,Integer s3,Integer s4,Integer s5,Integer s6
			,Integer s7,Integer s8,Integer s9,Integer s10,Integer s11,Integer s12, Integer start, Integer size);
	
	@Query(value="select count(1) from t_order o where o.user_id=?1 and(o.status=?2 or o.status=?3"
			+ " or o.status=?4 or o.status=?5 or o.status=?6 or o.status=?7 or o.status=?8 or o.status=?9 or o.status=?10"
			+ " or o.status=?11 or o.status=?12 or o.status=?13)", nativeQuery=true)
	public int queryOrderForUserIdTotal(Long userId,Integer s1,Integer s2,Integer s3,Integer s4,Integer s5,Integer s6
			,Integer s7,Integer s8,Integer s9,Integer s10,Integer s11,Integer s12);
	
	@Query(value="select session_token from t_order o where o.order_no=?1 and o.user_id=?2", nativeQuery=true)
	public String querySessionToken(String orderNo, Long userId);
	
	@Query(value="select user_id from t_order o where o.order_no=?1", nativeQuery=true)
	public long queryUserId(String orderNo);
	
	@Query(value="select money from t_order o where o.order_no=?1 and o.user_id=?2", nativeQuery=true)
	public String queryOrderMoney(String orderNo, Long userId);
	
	@Modifying
	@Query(value="insert into t_user_consume_log(user_id,order_no,amount,trans_amount,type,code,status,create_time) values(?1,?2,?3,?4,?5,?6,?7,now())", nativeQuery=true)
	public int insertConsumeLog(Long userId, String orderNo, String amount,String transAmount, int type, String code, int status);
	
	@Query(value="select user_id from t_user_consume_log o where o.order_no=?1", nativeQuery=true)
	public String queryUserIdForLog(String orderNo);
	
	@Query(value="select get_coupon_id from t_order o where o.order_no=?1", nativeQuery=true)
	public Long queryCouponIdForLog(String orderNo);
	
	@Modifying
	@Query(value="select order_no,os_id from t_order o where o.user_id=?1 and o.id=?2", nativeQuery=true)
	List<Object[]> findOrderByOrderId(Integer userId,Integer orderId);
	
	@Modifying
	@Query(value="select num,money from t_order o where o.user_id=?1 and o.order_no=?2", nativeQuery=true)
	List<Object[]> findOrderByOrderNo(Long userId,String orderNo);
}
