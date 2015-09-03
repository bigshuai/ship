package org.ship.shipservice.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.service.oil.OilStationService;
import org.ship.shipservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/os")
public class OilStationController {
	private static Logger logger = LoggerFactory.getLogger(OilStationController.class);
	@Autowired
	private  HttpServletRequest request;
	
	@Autowired
    private OilStationService oilStationService;
	
	/**
	 * 获取城市列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/city", method = RequestMethod.GET)
	public String getCity() {
		logger.debug("getCity start.");
		Integer[] pageInfo = CommonUtils.getPageInfo(request);
		ResultList r = oilStationService.queryCityList();
		return CommonUtils.printListStr(r.getDataList());
	}
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/station", method = RequestMethod.GET)
	public String getStation(@RequestParam("cityid") Integer cityId) {
		logger.debug("getStation start.cityId=" + cityId);
		Integer[] pageInfo = CommonUtils.getPageInfo(request);
		ResultList list = oilStationService.queryOilList(cityId, pageInfo[0], pageInfo[1]);
		return CommonUtils.printListStr(list);
	}
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/detail", method = RequestMethod.GET)
	public String getDetail(@RequestParam("osid") Long osId) {
		logger.debug("getStation start.osId=" + osId);
		OilStationBean os = oilStationService.queryDetail(osId);
		return CommonUtils.printObjStr(os);
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
