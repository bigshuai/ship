package org.ship.shipservice.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.Appraise;
import org.ship.shipservice.service.appraise.AppraiseService;
import org.ship.shipservice.service.oil.OilStationService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
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
	
	@Autowired
	private AppraiseService appraiseService;
	
	/**
	 * 获取城市列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/city", method = RequestMethod.GET)
	public String getCity() {
		logger.debug("getCity start.");
		String ff = request.getRealPath("");
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

	@RequestMapping(value="/saveAppraise" ,method=RequestMethod.POST)
	public String saveAppraise(@RequestParam("oilAppraise") String oilAppraise){
		if(StringUtils.isEmpty(oilAppraise)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			Appraise appraise = JSON.parseObject(oilAppraise, Appraise.class); 
			appraiseService.saveOilAppraise(appraise);
			return CommonUtils.printStr( "200", "评价成功");
		}
	}
	
	/**
	 * 获取加油站列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/oil", method = RequestMethod.GET)
	public String getOils() {
		logger.debug("getOils start.cityId=" );
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> oil = new HashMap<String, String>();
		oil.put("productName", "柴油");
		list.add(oil);
		
		Map<String, String> oil1 = new HashMap<String, String>();
		oil1.put("productName", "机油");
		list.add(oil1);
		
		Map<String, String> oil2 = new HashMap<String, String>();
		oil2.put("productName", "180");
		list.add(oil2);
		return CommonUtils.printListStr(list);
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
