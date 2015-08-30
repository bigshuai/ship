package org.ship.shipservice.service.coupon;

import java.util.List;
import java.util.Map;

import org.ship.shipservice.domain.CouponBean;

public interface CouponService {
	public List<CouponBean> queryCouponList(Long osId);
	
	public Map<String, String> getCoupon(Long userId, Long couponId);
}
