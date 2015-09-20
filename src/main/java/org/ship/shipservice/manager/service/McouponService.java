package org.ship.shipservice.manager.service;

import java.util.ArrayList;
import java.util.List;

import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.manager.dao.McouponDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@Transactional
public class McouponService {
	@Autowired
	private McouponDao mcouponDao;
	
	
	public ResultList getCouponList(Long osId, Integer page, Integer pageSize){
		List<CouponBean> result = new ArrayList<CouponBean>();
		ResultList rl = new ResultList();
		int start = (page - 1) * pageSize;
		List<Object[]> list = null;
		int total = 0;
		if(StringUtils.isEmpty(osId)){
			list = mcouponDao.queryCouponAll(start, pageSize);
			total = mcouponDao.queryCouponAllTotal();
		}else{
			list = mcouponDao.queryCouponList(osId, start, pageSize);
			total = mcouponDao.queryCouponTotalList(osId);
		}
		
		for(int i = 0 ; (list != null && i < list.size()); i++){
			Object[] o = list.get(i);
			CouponBean bean = new CouponBean();
			bean.setId(Long.valueOf(o[0] + ""));
			bean.setName(o[1] + "");
			bean.setDesc(o[2] + "");
			bean.setFaceValue(Integer.valueOf(o[3] + ""));
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
}
