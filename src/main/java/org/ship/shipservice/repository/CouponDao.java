package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.Coupon;
import org.ship.shipservice.entity.Oil;
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
	
	@Modifying
	@Query(value="select o from t_coupon_list o where o.status=1 and o.osId=0", nativeQuery=true)
	public Coupon checkGetCoupon(Long userId, Long cpId);
}
