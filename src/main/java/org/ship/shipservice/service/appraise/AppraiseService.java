package org.ship.shipservice.service.appraise;

import java.util.ArrayList;
import java.util.List;

import org.ship.shipservice.domain.AppraiseBean;
import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.Appraise;
import org.ship.shipservice.repository.AppraiseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author zhf
 * 
 */
@Component
@Transactional
public class AppraiseService {
	private AppraiseDao appraiseDao;

	public void saveOilAppraise(Appraise appraise) {
		appraiseDao.save(appraise);
	}

	public AppraiseDao getAppraiseDao() {
		return appraiseDao;
	}

	@Autowired
	public void setAppraiseDao(AppraiseDao appraiseDao) {
		this.appraiseDao = appraiseDao;
	}

	public ResultList getOilAppraise(String osId, Integer page, Integer pageSize) {
		ResultList rl = new ResultList();
		int start = (page - 1) * pageSize;
		List<AppraiseBean> result = new ArrayList<AppraiseBean>();
		List<Object[]> list = appraiseDao.queryAppraiseList(Integer.valueOf(osId), start, pageSize);
		int total = appraiseDao.queryAppraiseTotalList(Integer.valueOf(osId));
		for(int i = 0 ; (list != null && i < list.size()); i++){
			Object[] o = list.get(i);
			AppraiseBean bean = new AppraiseBean();
			bean.setOsId(Long.valueOf(o[0] + ""));
			bean.setUserId(Long.valueOf(o[1] + ""));
			bean.setUserName(o[2] + "");
			bean.setService(Integer.valueOf(o[3] + ""));
			bean.setQuality(Integer.valueOf(o[4] + ""));
			bean.setCredit(Integer.valueOf(o[5] + ""));
			bean.setContent(o[6] + "");
			bean.setCreateTime(o[7] + "");
			result.add(bean);
		}
		rl.setDataList(result);
		rl.setPage(page);
		rl.setTotal(total);
		return rl;
	}
}
