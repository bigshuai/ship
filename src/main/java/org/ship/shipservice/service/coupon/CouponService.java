package org.ship.shipservice.service.coupon;

import java.util.List;

import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResResult;

public interface CouponService {
	public List<CouponBean> queryCouponList(Long osId);
	
	public List<CouponBean> queryUserCouponList(Long userId, Integer status);
 	
	public  ResResult<String> getCoupon(Long userId, Long couponId);
}
