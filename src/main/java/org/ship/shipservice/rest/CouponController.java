package org.ship.shipservice.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.domain.UserBean;
import org.ship.shipservice.service.coupon.CouponService;
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
	 * 获取加油站列表
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
		ResResult<List<CouponBean>> result = new ResResult<List<CouponBean>>();
		List<CouponBean> list = couponService.queryCouponList(osId);
		result.setResult(list);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value="/get",params = { "cid" }, method = RequestMethod.POST)
	public String getCoupon(@RequestParam("cid") Long couponId) {
		Long osId = null;
		UserBean ub = (UserBean)request.getSession().getAttribute(SESSION_USER);
		Long userId = 1L;
		ResResult<String> result = couponService.getCoupon(userId, couponId);
		return JSON.toJSONString(result);
	}

	public CouponService getCouponService() {
		return couponService;
	}

	public void setCouponService(CouponService couponService) {
		this.couponService = couponService;
	}
}
