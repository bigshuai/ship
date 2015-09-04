package org.ship.shipservice.service.coupon.impl;

import java.util.ArrayList;
import java.util.List;

import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.domain.ResultList;
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
	public ResultList queryCouponList(Long osId, Integer page, Integer pageSize) {
		ResultList rl = new ResultList();
		int start = (page - 1) * pageSize;
		List<CouponBean> result = new ArrayList<CouponBean>();
		List<Object[]> list = null;
		int total = 0;
		//id, name,desc, face_value, limit_value,os_id,type,`status`,effective_day,start_time,end_time
		if(StringUtils.isEmpty(osId)){
			list = couponDao.queryCouponAll(start, pageSize);
			total = couponDao.queryCouponAllTotal();
		}else{
			list = couponDao.queryCouponList(osId, start, pageSize);
			total = couponDao.queryCouponTotalList(osId);
		}
		for(int i = 0 ; (list != null && i < list.size()); i++){
			Object[] o = list.get(i);
			CouponBean bean = new CouponBean();
			bean.setId(Long.valueOf(o[0] + ""));
			bean.setName(o[1] + "");
			bean.setDesc(o[2] + "");
			bean.setFaceValue(Float.valueOf(o[3] + ""));
			bean.setLimitValue(Integer.valueOf(o[4] + ""));
			bean.setOsId(Long.valueOf(o[5] + ""));
			bean.setType(Integer.valueOf(o[6] + ""));
			bean.setStatus(Integer.valueOf(o[7] + ""));
			bean.setEffectiveDay(Integer.valueOf(o[8] + ""));
			bean.setStartTime(o[9] + "");
			bean.setEndTime(o[10] + "");
			result.add(bean);
		}
		rl.setDataList(result);
		rl.setPage(page);
		rl.setTotal(total);
		return rl;
	}
	
	public ResultList queryUserCouponList(Long userId, Integer status, Integer page, Integer pageSize){
		ResultList rl = new ResultList();
		List<Object[]> list = new ArrayList<Object[]>();
		int start = (page - 1) * pageSize;
		int total = 0;
		if(StringUtils.isEmpty(status)){
			list = couponDao.queryUserCouponList(userId, start, pageSize);
			total = couponDao.queryUserCouponTotal(userId);
		}else{
			list = couponDao.queryUserCouponList(userId, status, start, pageSize);
			total = couponDao.queryUserCouponSTotal(userId, status);
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
		rl.setDataList(result);
		rl.setPage(page);
		rl.setTotal(total);
		return rl;
		
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
