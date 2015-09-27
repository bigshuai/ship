package org.ship.shipservice.rest;

import java.util.ArrayList;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.entity.Advert;
import org.ship.shipservice.entity.Version;
import org.ship.shipservice.service.home.HomeService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/home")
public class HomeRestController {
	private static Logger logger = LoggerFactory
			.getLogger(HomeRestController.class);
	@Autowired
	private HomeService homeService;
	/**
	 * 首页图片
	 */
	@RequestMapping(value="getAdvert")
	public String getAdvert(){
		List<Advert> adList = new ArrayList<Advert>();
		Advert ad1 = new Advert();
		ad1.setPicUrl("http://pic.qiantucdn.com/images/slideshow/55dff1b9f0822.jpg");
		ad1.setUrl("http://www.jd.com");
		Advert ad2 = new Advert();
		ad2.setPicUrl("http://pic.qiantucdn.com/images/slideshow/55dff1d0a50e0.png");
		ad2.setUrl("http://www.58pic.com/ ");
		Advert ad3 = new Advert();
		ad3.setPicUrl("http://pic.qiantucdn.com/images/slideshow/55dff1a9ee90c.jpg");
		ad3.setUrl("http://app.91jfyf.com");
		adList.add(ad1);
		adList.add(ad2);
		adList.add(ad3);
		logger.info(CommonUtils.printListStr(adList));
		return CommonUtils.printListStr(adList);
	}
	
	/**
	 * 版本更新
	 */
	@RequestMapping(value="checkVersion")
	public String checkVersion(@RequestParam("version") String version,@RequestParam("vercode") Integer vercode,@RequestParam("type") Integer type){
		if (StringUtils.isEmpty(version)) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			Page<Version> versionPage = homeService.findVersion(type);
			if(type==1){//android
				if(versionPage.getContent().get(0).getVercode()==vercode){
					return CommonUtils.printObjStr(versionPage.getContent().get(0), 200, "版本需要升级");
				}else{
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, "已是最新版本");
				}
			}else if(type==2){//ios
				if(versionPage.getContent().get(0).getName().equals(version)){
					return CommonUtils.printObjStr(versionPage.getContent().get(0), 200, "版本需要升级");
				}else{
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, "已是最新版本");
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
			}
		}
	}
}
