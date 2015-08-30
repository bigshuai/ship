package org.ship.shipservice.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.entity.City;
import org.ship.shipservice.service.oil.OilStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping(value="/api/v1/os")
public class OilStationController {
	private static Logger logger = LoggerFactory.getLogger(OilStationController.class);
	@Autowired
	private  HttpServletRequest request;
	
	@Autowired
    private OilStationService oilStationService;
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/city", method = RequestMethod.GET)
	public String getCity() {
		ResResult<List<City>> result = new ResResult<List<City>>();
		List<City> list = oilStationService.queryCityList();
		result.setResult(list);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/station", method = RequestMethod.GET)
	public String getStation(@RequestParam("cityid") Integer cityId) {
		ResResult<List<OilStationBean>> result = new ResResult<List<OilStationBean>>();
		List<OilStationBean> list = oilStationService.queryOilList(cityId);
		result.setResult(list);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String getDetail(@RequestParam("osid") Long osId) {
		ResResult<OilStationBean> result = new ResResult<OilStationBean>();
		OilStationBean os = oilStationService.queryDetail(osId);
		result.setResult(os);
		return JSON.toJSONString(result);
	}

	public OilStationService getOilStationService() {
		return oilStationService;
	}

	public void setOilStationService(OilStationService oilStationService) {
		this.oilStationService = oilStationService;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
