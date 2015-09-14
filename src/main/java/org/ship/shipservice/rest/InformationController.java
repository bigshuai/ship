package org.ship.shipservice.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.InfoType;
import org.ship.shipservice.entity.Information;
import org.ship.shipservice.repository.impl.InformationDaoImpl;
import org.ship.shipservice.service.information.InformationService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;









import com.alibaba.fastjson.JSON;

@RestController
@RequestMapping(value="/api/v1/info")
public class InformationController {
	private static Logger logger = LoggerFactory
			.getLogger(LoginRestController.class);
	private static JsonMapper mapper = JsonMapper.nonDefaultMapper();
	@Autowired
	private InformationService informationService;
	@Autowired
	private InformationDaoImpl inforDaoImpl;
	@Autowired
    private OilStationService oilStationService;
	/**
	 * 资讯类型
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/infoType",method = RequestMethod.POST)
	public String findInfoType(@RequestParam("id") long id){
		if (id<1||id>5) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			InfoType infoType = informationService.findInfoTypeById(id);
			ResultList r = oilStationService.queryCityList();
			infoType.setCityList((ArrayList<City>)r.getDataList());
			return CommonUtils.printObjStr(infoType, 200, "资讯类型");
		}
	}
	/**
	 * 上传资讯图片
	 * @param picture
	 * @param phone
	 * @param httpRequest
	 * @return
	 */
	@RequestMapping(value = "/uploadPicture", method = RequestMethod.POST)
	public String uploadImage(
			@RequestParam("picture") MultipartFile picture,HttpServletRequest httpRequest) {
		// 保存相对路径到数据库 图片写入服务器
		if (picture != null && !picture.isEmpty()) {
			// 获取图片的文件名
			String fileName = picture.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName
					.substring(fileName.lastIndexOf(".") + 1);
			// 新的图片文件名 = 手机+获取时间戳+"."图片扩展名
			String newFileName = String.valueOf(System.currentTimeMillis())
					+ "." + extensionName;
				try {
					String url=CommonUtils.saveFile(newFileName, picture,httpRequest);
					System.out.println(url);
					return CommonUtils.printObjStr(url,200, "上传成功");
				} catch (Exception e) {
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_500, MyConstant.JSON_RETURN_MESSAGE_500);
				}
		} else {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);

		}
	}
	/**
	 * 保存或更新资讯 
	 * @param info
	 * @return
	 */
	@RequestMapping(value="saveInfo",method=RequestMethod.POST)
	public String saveInformation(@RequestParam("info") String info){
		Information infor = JSON.parseObject(info, Information.class);
		infor=informationService.getInfoDao().save(infor);
		if(infor.getId()!=null){
			return CommonUtils.printObjStr(infor, 200, "资讯详情");
		}else{
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}
	}
	/**
	 * 获取资讯列表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value="getInfo",method=RequestMethod.POST)
	public String queryInfomation(@RequestParam("param") String param){
		Information infor = JSON.parseObject(param, Information.class);
		String str =" ";
		if(infor.getInfoType()!=null){
			str+="info.infoType="+infor.getInfoType();
		}
		if(infor.getInfoAction()!=null){
			str+=" and info.infoAction="+infor.getInfoAction();
		}
		List<Information> infoList=inforDaoImpl.findInfoByParam(str);
		return CommonUtils.printListStr(infoList);
	}
}
