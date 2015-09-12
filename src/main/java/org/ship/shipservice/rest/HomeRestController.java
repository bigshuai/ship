package org.ship.shipservice.rest;

import java.util.ArrayList;
import java.util.List;

import org.ship.shipservice.entity.Advert;
import org.ship.shipservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/home")
public class HomeRestController {
	private static Logger logger = LoggerFactory
			.getLogger(HomeRestController.class);
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
}
