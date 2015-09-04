package org.ship.shipservice.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.entity.ConsumeInfo;
import org.ship.shipservice.entity.Order;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.entity.UserAdvice;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.service.account.AdviceService;
import org.ship.shipservice.service.account.ConsumeInfoService;
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
import com.alibaba.fastjson.JSONObject;


/**
 * 用户中心
 * 
 * @author zhf
 *
 */
@RestController
@RequestMapping(value = "/api/v1/userInfo")
public class UserInfoRestController {
	private static Logger logger = LoggerFactory
			.getLogger(LoginRestController.class);
	private static JsonMapper mapper = JsonMapper.nonDefaultMapper();
	@Autowired
	private AccountService accountService;
	@Autowired
	private ConsumeInfoService consumeInfoService;
	@Autowired
	private AdviceService adviceService;
	
	/**
	 * 用户中心列表
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/list", params = { "phone" }, method = RequestMethod.POST)
	public String userInfo(@RequestParam("phone") String phone) {
		if (StringUtils.isEmpty(phone)) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			User user = accountService.findByPhone(phone);
			int couponCount = user.getCouponList().size();
			int orderCount = 0;
			if (user.getOrderList().size() != 0) {
				for (Order order : user.getOrderList()) {
					if (order.getStatus() != 4) {
						orderCount = orderCount + 1;
					}
				}
			}
			user.setCouponCount(couponCount);
			user.setOrderCount(orderCount);
			user.setPassword("");
			return CommonUtils.printObjStr(user, 200, "用户中心");
		}
	}
    /**
     * 消费详情
     * @param userId
     * @return
     */
	@RequestMapping(value = "/consumeInfo", params = { "userId" }, method = RequestMethod.POST)
	public String consumeInfo(@RequestParam("userId") Integer userId) {
		if (userId == 0) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			List<ConsumeInfo> cList = consumeInfoService.findByAccountId(
					userId, 0);
			return CommonUtils.printObjStr(cList, 200, "消费详情");
		}
	}

	@RequestMapping(value = "/uploadHeadImg", method = RequestMethod.POST)
	public String uploadImage(
			@RequestParam("userfile") MultipartFile headImage,
			@RequestParam("phone") String phone,HttpServletRequest httpRequest) {
		// 保存相对路径到数据库 图片写入服务器
		if (headImage != null && !headImage.isEmpty()) {
			// 获取图片的文件名
			String fileName = headImage.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName
					.substring(fileName.lastIndexOf(".") + 1);
			// 新的图片文件名 = 手机+获取时间戳+"."图片扩展名
			String newFileName = phone+String.valueOf(System.currentTimeMillis())
					+ "." + extensionName;
			User user = accountService.findByPhone(phone);
			if(user!=null){
				try {
					String url=saveFile(newFileName, headImage,httpRequest);
					user.setUrl(url);
					accountService.getUserDao().save(user);
					System.out.println(url);
					return CommonUtils.printObjStr(url,200, "上传成功");
				} catch (Exception e) {
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_500, MyConstant.JSON_RETURN_MESSAGE_500);
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
			}
			
		} else {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);

		}
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUserName(@RequestParam("phone") String phone,
			@RequestParam("userInfo") String userInfo) {
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(userInfo)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = accountService.findByPhone(phone);
			JSONObject json = JSONObject.parseObject(userInfo);
			if(user!=null){
				if(json.get("username")!=null){
					user.setUsername(json.get("username").toString());
				}
				if(json.get("shipname")!=null){
					user.setShipname(json.get("shipname").toString());
				}
				if(json.get("shipno")!=null){
					user.setShipno(json.get("shipno").toString());
				}
				user = accountService.updateName(user);
				user.setPassword(null);
				if(user.getId()!=0){
					return CommonUtils.printObjStr(user, 200, "更新成功");
				}else{
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
			}
		}
	}
	@RequestMapping(value = "/advice", params = { "userId","advice","email" }, method = RequestMethod.POST)
	private String saveAdvice(@RequestParam("userId") Integer userId, @RequestParam("advice") String advice,@RequestParam("email") String email) {
		if(userId==0||StringUtils.isEmpty(advice)){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);

		}else{
			UserAdvice useradvice = adviceService.findByUserId(userId);
			if(useradvice!=null){
				adviceService.updateAdvice(userId, advice, email);
			}else{
				UserAdvice uadvice  = new UserAdvice();
				uadvice.setUserId(userId);
				uadvice.setAdvice(advice);
				uadvice.setEmail(email);
				adviceService.getAdviceDao().save(uadvice);
			}
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_200, MyConstant.JSON_RETURN_MESSAGE_200);

		}
	}
	private String saveFile(String newFileName, MultipartFile filedata,HttpServletRequest httpRequest) {
		// 根据配置文件获取服务器图片存放路径
		String saveFilePath =httpRequest.getRealPath("")+"/headImage";
		/* 构建文件目录 */
		File fileDir = new File(saveFilePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(saveFilePath, newFileName));
			// 写入文件
			out.write(filedata.getBytes());
			out.flush();
			out.close();
			return "43.254.55.158"+httpRequest.getContextPath()+"/headImage/"+newFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";		
		}
	}

}
