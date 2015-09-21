package org.ship.shipservice.manager.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.manager.param.OrderParam;
import org.ship.shipservice.manager.service.MorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/m/order")
public class MorderController implements HybConstants{
	@Autowired
    private MorderService morderService;
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		OrderParam param = new OrderParam();
		ResultList rl = morderService.getOrderList(param);
		request.setAttribute("rl", rl);
		return "order/index";
	}
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value="mIndex", method=RequestMethod.GET)
	public String mIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException{
		ResultList rl = morderService.getMakeOrderList(1, 20);
		request.setAttribute("rl", rl);
		return "order/mIndex";
	}
}
