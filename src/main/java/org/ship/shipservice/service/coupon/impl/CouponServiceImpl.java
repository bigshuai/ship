package org.ship.shipservice.service.coupon.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResResult;
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
	
	public ResResult<String> getCoupon(Long userId, Long couponId){
		//先查询该用户 该优惠券是否已经领取
		int count = couponDao.checkGetCoupon(userId, couponId);
		ResResult<String> result = new ResResult<String>();
		if(count > 0){
			result.setCode(ErrorConstants.COUPON_GET_EXIST_DAY_ERROR);
			result.setMsg(ErrorConstants.getErrorMsg(ErrorConstants.COUPON_GET_EXIST_DAY_ERROR));
		}else{
			int n = couponDao.checkCouponTime(couponId);
			if(n > 0){
				//在有效期内
				int r = couponDao.saveCoupon(userId, couponId);
				if(r > 0){
					result.setCode(ErrorConstants.SUCCESS);
					result.setMsg(ErrorConstants.getErrorMsg(ErrorConstants.SUCCESS));
				}else{
					result.setCode(ErrorConstants.COUPON_GET_ERROR);
					result.setMsg(ErrorConstants.getErrorMsg(ErrorConstants.COUPON_GET_ERROR));
				}
			}else{
				result.setCode(ErrorConstants.COUPON_GET_OVERTIME_ERROR);
				result.setMsg(ErrorConstants.getErrorMsg(ErrorConstants.COUPON_GET_OVERTIME_ERROR));
			}
		}
		return result;
	}

	public CouponDao getCouponDao() {
		return couponDao;
	}

	public void setCouponDao(CouponDao couponDao) {
		this.couponDao = couponDao;
	}
}
