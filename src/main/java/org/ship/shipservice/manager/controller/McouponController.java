package org.ship.shipservice.manager.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.manager.service.McouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/m/coupon")
public class McouponController implements HybConstants{
	@Autowired
    private McouponService mcouponService;
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		ResultList rl = mcouponService.getCouponList(1L, 1, 20);
		request.setAttribute("rl", rl);
		return "coupon/index";
	}
}
