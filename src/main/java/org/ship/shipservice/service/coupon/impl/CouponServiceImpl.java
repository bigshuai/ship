package org.ship.shipservice.service.coupon.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.entity.Coupon;
import org.ship.shipservice.entity.CouponList;
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
	
	public List<CouponBean> queryUserCouponList(Long userId, Integer status){
		List<Object[]> list = new ArrayList<Object[]>();
		if(StringUtils.isEmpty(status)){
			list = couponDao.queryUserCouponList(userId);
		}else{
			list = couponDao.queryUserCouponList(userId, status);
		}
		
		List<CouponBean> result = new ArrayList<CouponBean>();
		for(Object[] o : list){
			CouponBean os = new CouponBean();
			//t.coupon_id, t.`status`,t.coupon_name, t.face_value,t.limit_value,t.start_time,t.end_time,t.create_time 
			os.setId(Long.valueOf(o[0]+""));
			os.setStatus(Integer.valueOf(o[1]+""));
			os.setName(o[2]+"");
			os.setFaceValue((Float.valueOf(o[3]+"")));
			os.setLimitValue((Integer.valueOf(o[4]+"")));
			os.setStartTime(null);
			os.setEndTime(null);
			os.setCreateTime(null);
			result.add(os);
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
