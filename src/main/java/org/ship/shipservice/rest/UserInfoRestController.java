package org.ship.shipservice.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.entity.ConsumeInfo;
import org.ship.shipservice.entity.Order;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.entity.UserAdvice;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.service.account.AdviceService;
import org.ship.shipservice.service.account.ConsumeInfoService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.JsonUtil;
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
			return CommonUtils.printObjStr(mapper.toJson(user).toString()+couponCount+orderCount, 200, "用户登陆成功");
		}
	}

	@RequestMapping(value = "/consumeInfo", params = { "userId" }, method = RequestMethod.POST)
	public String consumeInfo(@RequestParam("userId") Integer userId) {
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if (userId == 0) {
			map.put("status", MyConstant.JSON_RETURN_CODE_400);
			map.put("msg", MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			List<ConsumeInfo> cList = consumeInfoService.findByAccountId(
					userId, 0);
			map.put("result", mapper.toJson(cList));
			map.put("status", MyConstant.JSON_RETURN_CODE_200);
			map.put("msg", "消费详情");
		}
		json = JsonUtil.map2json(map);
		return json;
	}

	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public String uploadImage(
			@RequestParam("headImage") MultipartFile headImage,
			@RequestParam("phone") String phone) {
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		// 保存相对路径到数据库 图片写入服务器
		if (headImage != null && !headImage.isEmpty()) {
			// 获取图片的文件名
			String fileName = headImage.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName
					.substring(fileName.lastIndexOf(".") + 1);
			// 新的图片文件名 = 获取时间戳+"."图片扩展名
			String newFileName = String.valueOf(System.currentTimeMillis())
					+ "." + extensionName;
			User user = accountService.findByPhone(phone);
			try {
				user.setUrl(newFileName);
				accountService.getUserDao().save(user);
				saveFile(newFileName, headImage);
				map.put("status", MyConstant.JSON_RETURN_CODE_200);
				map.put("msg", "图片上传成功");
				map.put("result", mapper.toJson(user));
			} catch (Exception e) {
				// 上传图片失败
				map.put("status", MyConstant.JSON_RETURN_CODE_400);
				map.put("msg", MyConstant.JSON_RETURN_MESSAGE_400);
			}
		} else {
			map.put("status", MyConstant.JSON_RETURN_CODE_200);
			map.put("msg", "图片为空");
		}
		json = JsonUtil.map2json(map);
		return json;
	}

	@RequestMapping(value = "/updateUsername", params = { "phone","username" }, method = RequestMethod.POST)
	public String updateUserName(@RequestParam("phone") String phone,
			@RequestParam("username") String username) {
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(StringUtils.isEmpty(phone)||StringUtils.isEmpty(username)){
			map.put("status", MyConstant.JSON_RETURN_CODE_400);
			map.put("msg", MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			User user = accountService.findByPhone(phone);
			if(user!=null){
				user.setUsername(username);
				int result = accountService.updateName(phone, username);
				if(result==1){
					map.put("code", MyConstant.JSON_RETURN_CODE_200);
					map.put("msg", "用户名更新成功");
					map.put("result", mapper.toJson(user));
				}else{
					map.put("code", MyConstant.JSON_RETURN_CODE_500);
					map.put("msg", MyConstant.JSON_RETURN_MESSAGE_500);
				}
			}else{
				map.put("code", MyConstant.JSON_RETURN_CODE_200);
				map.put("msg", "用户不存在");
			}
		}
		json = JsonUtil.map2json(map);
		return json;
	}
	@RequestMapping(value = "/advice", params = { "userId","advice","email" }, method = RequestMethod.POST)
	private String saveAdvice(@RequestParam("userId") Integer userId, @RequestParam("advice") String advice,@RequestParam("email") String email) {
		String json = "";
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(userId==0||StringUtils.isEmpty(advice)){
			map.put("code", MyConstant.JSON_RETURN_CODE_400);
			map.put("msg", MyConstant.JSON_RETURN_MESSAGE_400);
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
			map.put("code", MyConstant.JSON_RETURN_CODE_200);
			map.put("msg", MyConstant.JSON_RETURN_MESSAGE_200);
		}
		json = JsonUtil.map2json(map);
		return json;
	}
	private void saveFile(String newFileName, MultipartFile filedata) {
		// 根据配置文件获取服务器图片存放路径
		String saveFilePath = "/headImage";
		/* 构建文件目录 */
		File fileDir = new File(saveFilePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		try {
			FileOutputStream out = new FileOutputStream(saveFilePath + "\\"
					+ newFileName);
			// 写入文件
			out.write(filedata.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
