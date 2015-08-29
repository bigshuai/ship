package org.ship.shipservice.rest;

import java.util.List;

import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.OilStation;
import org.ship.shipservice.service.oil.OilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping(value="/api/v1/oil")
public class OilController {
	private static Logger logger = LoggerFactory.getLogger(OilController.class);
	
	@Autowired
    private OilService oilService;
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/city", method = RequestMethod.GET)
	public String getCity() {
		ResResult<List<City>> result = new ResResult<List<City>>();
		List<City> list = oilService.queryCityList();
		result.setResult(list);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/station", method = RequestMethod.GET)
	public String getStation(@RequestParam("cityId") Integer cityId) {
		ResResult<List<OilStation>> result = new ResResult<List<OilStation>>();
		List<OilStation> list = oilService.queryOilList(cityId);
		result.setResult(list);
		return JSON.toJSONString(result);
	}
	public OilService getOilService() {
		return oilService;
	}
	public void setOilService(OilService oilService) {
		this.oilService = oilService;
	}
}
