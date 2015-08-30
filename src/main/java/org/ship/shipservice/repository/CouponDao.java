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
	@Query("select o from Coupon o where o.status=1 and (o.osId=?1 or o.osId=0)")
	public List<Coupon> queryCouponList(Long osId);
	
	@Modifying
	@Query("select o from Coupon o where o.status=1 and o.osId=0")
	public List<Coupon> queryCouponAll();
	
	@Query(value="select count(*) from t_coupon_list t where t.user_id=?1 AND t.coupon_id=?2 and t.status=0 and DATE_FORMAT(t.create_time,'%Y-%m-%d-')= CURDATE()", nativeQuery=true)
	public int checkGetCoupon(Long userId, Long cpId);
	
	@Query(value="select count(*) from t_coupon t where t.id=?1 and t.`status`=1 and t.start_time < NOW() and t.end_time>NOW()", nativeQuery=true)
	public int checkCouponTime(Long cpId);
	
	@Modifying
	@Query(value="INSERT INTO t_coupon_list(coupon_id,user_id,status,coupon_name,face_value,limit_value,start_time,end_time,create_time) "
			+ "SELECT c.id,?1,0,c.`name`,c.face_value,c.limit_value,c.start_time,c.end_time,NOW() FROM t_coupon c where c.id=?2", nativeQuery=true)
	public int saveCoupon(Long userId, Long cpId);
}
