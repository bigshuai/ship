package org.ship.shipservice.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.BankInfo;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.domain.UrlBean;
import org.ship.shipservice.entity.ConsumeBean;
import org.ship.shipservice.entity.ConsumeInfo;
import org.ship.shipservice.entity.Favorite;
import org.ship.shipservice.entity.Information;
import org.ship.shipservice.entity.OilStation;
import org.ship.shipservice.entity.Order;
import org.ship.shipservice.entity.User;
import org.ship.shipservice.entity.UserAdvice;
import org.ship.shipservice.service.account.AccountService;
import org.ship.shipservice.service.account.AdviceService;
import org.ship.shipservice.service.account.ConsumeInfoService;
import org.ship.shipservice.service.bank.BankService;
import org.ship.shipservice.service.favorite.FavoriteService;
import org.ship.shipservice.service.home.HomeService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.ship.shipservice.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	@Autowired
	private AccountService accountService;
	@Autowired
	private ConsumeInfoService consumeInfoService;
	@Autowired
	private AdviceService adviceService;
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
    private BankService bankService;
	@Autowired
	private HomeService homeService;
	/**
	 * 用户中心列表
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/list", params = { "phone" }, method = RequestMethod.POST)
	public String userInfo(@RequestParam("phone") String phone) {
		if (StringUtils.isEmpty(phone)) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);
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
			ResultList rl = bankService.getUserBankList(user.getId());
			List<BankInfo> dataList  = (ArrayList<BankInfo>) rl.getDataList();
			String fund = accountService.getUserDao().getUserFund(user.getId());
			user.setBalance(fund==null?new BigDecimal("0.00"):new BigDecimal(fund));
			user.setCardCount(dataList.size());
			user.setCouponCount(couponCount);
			user.setOrderCount(orderCount);
			user.setPassword(null);
			user.setCouponList(null);
			user.setOrderList(null);
			logger.info(user.getPhone() + ":用户详情" + user.toString());
			return CommonUtils.printObjStr(user, 200, "用户中心");
		}
	}

	/**
	 * 消费详情
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/consumeInfo", params = { "userId", "page" }, method = RequestMethod.POST)
	public String consumeInfo(@RequestParam("userId") Integer userId,
			@RequestParam("page") Integer page) {
		if (userId == 0) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			PageRequest pageRequest = new PageRequest(page-1, 10);
			Page<ConsumeInfo> cList = consumeInfoService.findByAccountId(userId, pageRequest);
			ResultList list = new ResultList();
			List<ConsumeBean> cbs = new ArrayList<ConsumeBean>();
			List<ConsumeInfo> dd = cList.getContent();
			for(ConsumeInfo info : dd){
				ConsumeBean bean = new ConsumeBean();
				bean.setId(info.getId());
				bean.setMoney(info.getOrderNo().startsWith("U") ? "+"+info.getAmount(): "-"+info.getAmount());
				bean.setDescribe(info.getOrderNo().startsWith("U") ? "充值": "加油");
				bean.setTime(info.getCreateTime());
				cbs.add(bean);
			}
			list.setDataList(cbs);
			list.setPage(page);
			list.setTotal(Integer.valueOf(cList.getTotalElements()+""));
			return CommonUtils.printListStr(list);
		}
	}

	/*
	 * 上传头像
	 */
	@RequestMapping(value = "/uploadHeadImg", method = RequestMethod.POST)
	public String uploadImage(@RequestParam("file") MultipartFile headImage,
			@RequestParam("phone") String phone, HttpServletRequest httpRequest) {
		// 保存相对路径到数据库 图片写入服务器
		if (headImage != null && !headImage.isEmpty()) {
			// 获取图片的文件名
			String fileName = headImage.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName
					.substring(fileName.lastIndexOf(".") + 1);
			// 新的图片文件名 = 手机+获取时间戳+"."图片扩展名
			String newFileName = phone
					+ String.valueOf(System.currentTimeMillis()) + "."
					+ extensionName;
			User user = accountService.findByPhone(phone);
			if (user != null) {
				try {
					String url = saveFile(newFileName, headImage, httpRequest);
					user.setUrl(url);
					accountService.getUserDao().save(user);
					UrlBean urlBean = new UrlBean();
					urlBean.setUrl(url);
					return CommonUtils.printObjStr(urlBean, 200, "上传成功");
				} catch (Exception e) {
					return CommonUtils.printStr(
							MyConstant.JSON_RETURN_CODE_500,
							MyConstant.JSON_RETURN_MESSAGE_500);
				}
			} else {
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
						MyConstant.JSON_RETURN_MESSAGE_400);
			}

		} else {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);

		}
	}

	/*
	 * 更新个人信息
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUserName(@RequestParam("phone") String phone,
			@RequestParam("userInfo") String userInfo) {
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(userInfo)) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			User user = accountService.findByPhone(phone);
			JSONObject json = JSONObject.parseObject(userInfo);
			if (user != null) {
				if (json.get("username") != null) {
					user.setUsername(json.get("username").toString());
				}
				if (json.get("shipname") != null) {
					user.setShipname(json.get("shipname").toString());
				}
				if (json.get("shipno") != null) {
					user.setShipno(json.get("shipno").toString());
				}
				if (json.get("city") != null) {
					user.setCity(json.get("city").toString());
				}
				user = accountService.updateName(user);
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
				if (user.getId() != 0) {
					ResultList rl = bankService.getUserBankList(user.getId());
					List<BankInfo> dataList  = (ArrayList<BankInfo>) rl.getDataList();
					String fund = accountService.getUserDao().getUserFund(user.getId());
					user.setBalance(new BigDecimal(fund));
					user.setCardCount(dataList.size());
					user.setPassword(null);
					user.setCouponList(null);
					user.setOrderList(null);
					return CommonUtils.printObjStr(user, 200, "更新成功");
				} else {
					return CommonUtils.printStr(
							MyConstant.JSON_RETURN_CODE_400,
							MyConstant.JSON_RETURN_MESSAGE_400);
				}
			} else {
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
						MyConstant.JSON_RETURN_MESSAGE_400);
			}
		}
	}

	/*
	 * 我的建议
	 */
	@RequestMapping(value = "/advice", params = { "userId", "advice" }, method = RequestMethod.POST)
	private String saveAdvice(@RequestParam("userId") Integer userId,
			@RequestParam("advice") String advice,
			@RequestParam("email") String email) {
		if (userId == 0 || StringUtils.isEmpty(advice)) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);

		} else {
			UserAdvice useradvice = adviceService.findByUserId(userId);
			if (useradvice != null) {
				adviceService.updateAdvice(userId, advice, email);
			} else {
				UserAdvice uadvice = new UserAdvice();
				uadvice.setUserId(userId);
				uadvice.setAdvice(advice);
				uadvice.setEmail(email);
				adviceService.getAdviceDao().save(uadvice);
			}
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_200,
					MyConstant.JSON_RETURN_MESSAGE_200);

		}
	}

	/*
	 * 我的收藏
	 */
	@RequestMapping(value = "myfavorite")
	public String findFavorite(@RequestParam("userId") Integer userId,
			@RequestParam("type") Integer type,
			@RequestParam("page") Integer page) {
		if (userId == 0 || type == 0) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			PageRequest pageRequest = new PageRequest(page-1, 10);
			Page<Favorite> favorites = favoriteService.findFavorite(userId,
					type, pageRequest);
			return CommonUtils.printObjStr(favorites.getContent(), 200, "我的收藏");
		}
	}
/**
 * 
 * @param userId
 * @param type
 * @param faId
 * @return
 */
	@RequestMapping(value = "deleteFavorite")
	public String deleteFavorite(@RequestParam("id") long id){
		Favorite favorite = favoriteService.getFavoriteDao().findOne(id);
		if(favorite==null){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			favoriteService.getFavoriteDao().delete(favorite);
			return CommonUtils.printStr("200", "删除成功");
		}
	}
	/*
	 * 保存我的收藏
	 */
	@RequestMapping(value = "savefavorite")
	public String saveFavorite(@RequestParam("userId") Integer userId,
			@RequestParam("type") Integer type, @RequestParam("faId") Long faId) {
		if (userId == 0 || type == 0 || faId == 0) {
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
					MyConstant.JSON_RETURN_MESSAGE_400);
		} else {
			BigInteger count = favoriteService.findFavorite(userId,
					type, faId);
			if(count.bitCount()==0){
				Favorite favorite = new Favorite();
				favorite.setUserId(userId);
				favorite.setType(type);
				if (type == 1) {
					OilStation oil = new OilStation();
					oil.setId(faId);
					favorite.setOilStation(oil);
				} else if (type == 2) {
					Information info = new Information();
					info.setId(faId);
					favorite.setInfo(info);
				}
				favorite = favoriteService.save(favorite);
				if (favorite.getId() != 0) {
					return CommonUtils.printStr("200", "收藏成功");
				} else {
					return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
							"收藏失败");
				}
			}else{
				return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400,
						"已收藏过了");
			}
			
		}
	}

	/**
	 * 关于我们
	 * 
	 * @return
	 */
	@RequestMapping(value="aboutUs")
	public String aboutUs(){
		return CommonUtils.printObjStr(homeService.findAboutUs(), 200, "关于航运宝");
	}
	private String saveFile(String newFileName, MultipartFile filedata,
			HttpServletRequest httpRequest) {
		// 根据配置文件获取服务器图片存放路径
		String saveFilePath = httpRequest.getRealPath("") + "/headImage";
		/* 构建文件目录 */
		File fileDir = new File(saveFilePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(saveFilePath,
					newFileName));
			// 写入文件
			out.write(filedata.getBytes());
			out.flush();
			out.close();
			return "http://43.254.55.158" + httpRequest.getContextPath()
					+ "/headImage/" + newFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@RequestMapping(value="/ybp", method = RequestMethod.POST)
	public String yBalancePayment(@RequestBody String body){
		logger.debug("getCoupon start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		Long userId = jo.getLong(HybConstants.USERID);
		String orderNo = jo.getString("orderNo");
		Map<String, String> res = accountService.orderVerify(userId, orderNo, true);
		if(StringUtils.isEmpty(res.get("msg"))){
			return CommonUtils.printStr();
		}else{
			return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
		}
	}
	
	@RequestMapping(value="/bp", method = RequestMethod.POST)
	public String balancePayment(@RequestBody String body){
		logger.debug("getCoupon start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		Long userId = jo.getLong(HybConstants.USERID);
		String orderNo = jo.getString("orderNo");
		String code = jo.getString("code");
		Map<String, String> res = accountService.balancePayment(userId, orderNo, code);
		return null;
	}
}
