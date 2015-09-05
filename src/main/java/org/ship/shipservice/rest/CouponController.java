package org.ship.shipservice.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.domain.UserBean;
import org.ship.shipservice.service.coupon.CouponService;
import org.ship.shipservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping(value="/api/v1/coupon")
public class CouponController implements HybConstants{
	private static Logger logger = LoggerFactory.getLogger(CouponController.class);
	@Autowired
	private  HttpServletRequest request;
	
	@Autowired
    private CouponService couponService;
	
	/**
	 * 获取优惠券列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String getCouponList() {
		Long osId = null;
		try {
			osId = Long.valueOf(request.getParameter("osid"));
		} catch (NumberFormatException e) {
		}
		Integer[] pageInfo = CommonUtils.getPageInfo(request);
		logger.debug("getCouponList start.osId="+osId + ",page=" + JSON.toJSONString(pageInfo));
		ResultList list = couponService.queryCouponList(osId, pageInfo[0], pageInfo[1]);
		return CommonUtils.printListStr(list);
	}
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String getUserCouponList(@RequestParam("userId") Long userId) {
		Integer status = null;
		try {
			status = Integer.valueOf(request.getParameter("s"));
		} catch (NumberFormatException e) {
		}
		logger.debug("getUserCouponList start.status="+status);
		Integer[] pageInfo = CommonUtils.getPageInfo(request);
		ResultList list = couponService.queryUserCouponList(userId, status, pageInfo[0], pageInfo[1]);
		return CommonUtils.printListStr(list);
	}
	
	@RequestMapping(value="/get",params = { "cid", "userId"}, method = RequestMethod.POST)
	public String getCoupon(@RequestParam("cid") Long couponId,@RequestParam("userId") Long userId) {
		logger.debug("getCoupon start.couponId="+couponId);
		ResResult<String> result = couponService.getCoupon(userId, couponId);
		return CommonUtils.printObjStr2(result);
	}

	public CouponService getCouponService() {
		return couponService;
	}

	public void setCouponService(CouponService couponService) {
		this.couponService = couponService;
	}
}
