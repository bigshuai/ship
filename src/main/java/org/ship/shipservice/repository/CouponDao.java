package org.ship.shipservice.repository;

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
public interface CouponDao extends CrudRepository<Coupon, Long> {
	@Modifying
	@Query(value="select o.id, o.name,o.`desc`, o.face_value, o.limit_value,o.os_id,o.type,o.status,o.effective_day,o.start_time,o.end_time "
			+ "from t_coupon o where o.status=1 and (o.os_id=?1 or o.os_id=0) order by id desc limit ?2,?3", nativeQuery=true)
	public List<Object[]> queryCouponList(Long osId, Integer start, Integer size);
	
	@Query(value="select count(1) from t_coupon o where o.status=1 and (o.os_id=?1 or o.os_id=0)", nativeQuery=true)
	public int queryCouponTotalList(Long osId);
	
	@Modifying
	@Query(value="select o.id, o.name,o.`desc`, o.face_value, o.limit_value,o.os_id,o.type,o.status,o.effective_day,o.start_time,o.end_time "
			+ "from t_coupon o where o.status=1 and o.os_id=0 order by o.id desc limit ?1, ?2", nativeQuery=true)
	public List<Object[]> queryCouponAll(Integer start, Integer size);
	
	@Query(value="select count(1) from t_coupon o where o.status=1 and o.os_id=0", nativeQuery=true)
	public int queryCouponAllTotal();
	
	@Query(value="select count(*) from t_coupon_list t where t.user_id=?1 AND t.coupon_id=?2 and t.status=0 and DATE_FORMAT(t.create_time,'%Y-%m-%d-')= CURDATE()", nativeQuery=true)
	public int checkGetCoupon(Long userId, Long cpId);
	
	@Query(value="select count(*) from t_coupon t where t.id=?1 and t.`status`=1 and t.start_time < NOW() and t.end_time>NOW()", nativeQuery=true)
	public int checkCouponTime(Long cpId);
	
	@Modifying
	@Query(value="INSERT INTO t_coupon_list(coupon_id,user_id,status,coupon_name,face_value,limit_value,start_time,end_time,create_time) "
			+ "SELECT c.id,?1,0,c.`name`,c.face_value,c.limit_value,c.start_time,c.end_time,NOW() FROM t_coupon c where c.id=?2", nativeQuery=true)
	public int saveCoupon(Long userId, Long cpId);

	@Modifying
	@Query(value="SELECT t.coupon_id, t.`status`,t.coupon_name, t.face_value,t.limit_value,t.start_time,t.end_time,"
			+ "t.create_time FROM t_coupon_list t where t.user_id=?1 "
			+ "order by t.create_time desc limit ?2,?3", nativeQuery=true)
	List<Object[]> queryUserCouponList(Long userId, Integer start, Integer size);
	
	@Query(value="SELECT count(1) FROM t_coupon_list t where t.user_id=?1", nativeQuery=true)
	public int queryUserCouponTotal(Long userId);
	
	@Modifying
	@Query(value="SELECT t.coupon_id, t.`status`,t.coupon_name, t.face_value,t.limit_value,t.start_time,t.end_time,"
			+ "t.create_time FROM t_coupon_list t where t.user_id=?1 and t.status =?2 "
			+ "order by t.create_time desc limit ?3,?4", nativeQuery=true)
	List<Object[]> queryUserCouponList(Long userId, Integer status, Integer start, Integer size);
	
	@Query(value="SELECT count(1) FROM t_coupon_list t where t.user_id=?1 and t.status =?2", nativeQuery=true)
	public int queryUserCouponSTotal(Long userId, Integer status);
	
	@Modifying
	@Query(value="SELECT t.coupon_id, t.face_value,t.limit_value"
			+ " FROM t_coupon_list t where t.user_id=?1 and coupon_id=?2 and t.status=0", nativeQuery=true)
	Object[] queryCouponInfo(Long userId, Long couponId);
}
