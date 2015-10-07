package org.ship.shipservice.service.coupon;

import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.domain.ResultList;

public interface CouponService {
	public ResultList queryCouponList(Long osId, Integer page, Integer pageSize);
	
	public ResultList queryUserCouponList(Long userId, Integer status, Integer page, Integer pageSize);
 	
	public  ResResult<String> getCoupon(Long userId, Long couponId);
	
	public int updateOverTimeCoupon(Integer status, Integer id);
}
