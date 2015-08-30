package org.ship.shipservice.service.coupon.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.entity.Coupon;
import org.ship.shipservice.repository.CouponDao;
import org.ship.shipservice.service.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@Transactional
public class CouponServiceImpl implements CouponService{
	@Autowired
	private CouponDao couponDao;
	
	@Override
	public List<CouponBean> queryCouponList(Long osId) {
		List<CouponBean> result = new ArrayList<CouponBean>();
		List<Coupon> list = null;
		if(StringUtils.isEmpty(osId)){
			list = couponDao.queryCouponAll();
		}else{
			list = couponDao.queryCouponList(osId);
		}
		for(int i = 0 ; (list != null && i < list.size()); i++){
			CouponBean bean = new CouponBean();
			Coupon c = list.get(i);
			try {
				PropertyUtils.copyProperties(bean, c);
				result.add(bean);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Map<String, String> getCoupon(Long userId, Long couponId){
		//先查询该用户 该优惠券是否已经领取
		
		return null;
	}

	public CouponDao getCouponDao() {
		return couponDao;
	}

	public void setCouponDao(CouponDao couponDao) {
		this.couponDao = couponDao;
	}
}
