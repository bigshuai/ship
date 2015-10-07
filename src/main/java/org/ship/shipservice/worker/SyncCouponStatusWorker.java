package org.ship.shipservice.worker;

import java.util.List;

import org.apache.log4j.Logger;
import org.ship.shipservice.repository.CouponDao;
import org.ship.shipservice.service.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component  
public class SyncCouponStatusWorker {
	Logger logger = Logger.getLogger(SyncCouponStatusWorker.class);
	
	@Autowired
	private CouponDao couponDao;
	
	@Autowired
	private CouponService couponService;
	
	@Scheduled(cron="0/5 * * * * ? ") //间隔5秒执行  
	public void process(){  
		logger.error("同步优惠券信息worker......start");
		try {
			//获取用户优惠券信息  检测如果超时 就设置状态
			Integer status = 0;
			Integer start = 0;
			Integer size = 100;
			List<Integer> list = couponDao.queryAllCouponList(status, start, size);
			if(list != null && list.size() > 0){
				logger.error("更新状态 ids = " + list.toString());
				for(Integer id : list){
					try {
						int r = couponService.updateOverTimeCoupon(status, id);
						if(r > 0){
							logger.info("更新状态成功： id="+id);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("同步优惠券信息worker 异常。", e);
		}
		logger.error("同步优惠券信息worker......end");
    }  
}
