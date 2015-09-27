package org.ship.shipservice.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.OrderBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.service.order.OrderService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.ship.shipservice.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.sj.pay.sign.MD5;


@RestController
@RequestMapping(value="/api/v1/order")
public class OrderRestController {
	private static Logger logger = LoggerFactory.getLogger(OrderRestController.class);
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
    private OrderService orderService;
	
	/**
	 * 获取订单列表
	 * @param userId status
	 * @return
	 */
	@RequestMapping(value="/list", method = RequestMethod.POST)
	public String getOrderList(@RequestParam("userId") Long userId,@RequestParam("status") Integer status) {
		if(userId==0){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			Integer[] pageInfo = CommonUtils.getPageInfo(request);
			ResultList list = orderService.queryOrderUserList(userId, status,pageInfo[0], pageInfo[1]);
			return CommonUtils.printListStr(list);
		}
	}
	
	@RequestMapping(value="/cmo", method = RequestMethod.POST)
	public String createMakeOrder(@RequestBody String body) {
		logger.debug("createMakeOrder start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		OrderBean order = new OrderBean();
		order.setUserId(jo.getLong("userId"));
		order.setProductName(jo.getString("productName"));
		order.setNum(jo.getInteger("num"));
		order.setBookTime(jo.getString("bookTime"));
		order.setBookAddr(CommonUtils.decode(jo.getString("addr")));
		//订单类型 1-正常加油订单  11-预约加油
		String r = this.checkOrderParam(order);
		if(StringUtils.isEmpty(r)){
			try {
				Map<String, String> res = orderService.createMakeOrder(order);
				if(StringUtils.isEmpty(res.get("msg"))){
					return CommonUtils.printObjStr(res);
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
			}
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	}
	
	/**
	 * 创建订单
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/cpo", method = RequestMethod.POST)
	public String createOrder(@RequestBody String body) {
		logger.debug("createOrder start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		OrderBean order = new OrderBean();
		order.setUserId(jo.getLong("userId"));
		order.setOrderNo(jo.getString("orderNo"));
		order.setProductId(jo.getString("productId"));
		order.setProductName(jo.getString("productName"));
		order.setPrice(jo.getString("price"));
		order.setNum(jo.getInteger("num"));
		order.setCouponId(jo.getLong("couponId"));
		order.setOsId(jo.getLong("osId"));
		//订单类型 1-正常加油订单
		String r = this.checkOrderParam(order);
		if(StringUtils.isEmpty(r)){
			try {
				Map<String, String> res = orderService.createOrder(order, 1);
				if(StringUtils.isEmpty(res.get("msg"))){
					return CommonUtils.printObjStr(res);
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
			}
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	} 
	
	/**
	 * 支付预校验
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/pfp", method = RequestMethod.POST)
	public String precheckForPayment(@RequestBody String body) {
		logger.debug("precheckForPayment start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		//String sessionToken = jo.getString("sessionToken");
		Long userId =  jo.getLong("userId");
		String orderNo = jo.getString("orderNo");
		//String agreementNo = jo.getString("agreementNo");
		Long bankId = jo.getLong("bankId");
		
		String r = this.checkPfpOrderParam(userId, bankId, orderNo);
		if(StringUtils.isEmpty(r)){
			String res = orderService.precheckForPayment(userId, bankId, orderNo);
			if(StringUtils.isEmpty(res)){
				return CommonUtils.printStr();
			}else{
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res);
			}
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	} 
	
	/**
	 * 支付
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/pay", method = RequestMethod.POST)
	public String pay(@RequestBody String body) {
		logger.debug("pay start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		Long userId =  jo.getLong("userId");
		String orderNo = jo.getString("orderNo");
		String code = jo.getString("code");
		String r = null;
		if(StringUtils.isEmpty(r)){
			try {
				Map<String, String> res = orderService.payment(userId, orderNo, code);
				if(StringUtils.isEmpty(res.get("msg"))){
					return CommonUtils.printObjStr(res);
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
			}
			
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	} 
	
	/**
	 * 账号余额支付预校验
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/crcp", method = RequestMethod.POST)
	public String cRechargePay(@RequestBody String body) {
		logger.debug("cRechargePay start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		Long userId =  jo.getLong("userId");
		String orderNo = jo.getString("orderNo");
		String r = null;
		if(StringUtils.isEmpty(r)){
			try {
				Map<String, String> res = orderService.cRechargePay(userId, orderNo);
				if(StringUtils.isEmpty(res.get("msg"))){
					return CommonUtils.printObjStr(res);
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
			}
			
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	}
	
	/**
	 * 账号余额支付
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/rcp", method = RequestMethod.POST)
	public String rechargePay(@RequestBody String body) {
		logger.debug("rechargePay start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		Long userId =  jo.getLong("userId");
		String orderNo = jo.getString("orderNo");
		String code = jo.getString("code");
		String r = null;
		if(StringUtils.isEmpty(r)){
			try {
				Map<String, String> res = orderService.rechargePay(userId, orderNo, code);
				if(StringUtils.isEmpty(res.get("msg"))){
					return CommonUtils.printObjStr(res);
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
			}
			
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	} 
	
	/**
	 * 创建充值订单
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/prc", method = RequestMethod.POST)
	public String recharge(@RequestBody String body) {
		logger.debug("recharge start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		String amount = jo.getString("amount");
		Long userId = jo.getLong("userId");
		Long bankId = jo.getLong("bankId");
		String r = null;
		if(StringUtils.isEmpty(r)){
			try {
				OrderBean order = new OrderBean();
				order.setUserId(userId);
				order.setAmount(amount);
				Map<String, String> res = orderService.rechargeOrder(order, bankId);
				if(StringUtils.isEmpty(res.get("msg"))){
					return CommonUtils.printObjStr(res);
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
			}
			
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	} 
	
	/**
	 * 更新订单状态
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/us", method = RequestMethod.POST)
	public String updateStatus(@RequestBody String body) {
		logger.debug("updateStatus start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		Long userId =  jo.getLong("userId");
		String orderNo = jo.getString("orderNo");
		Integer status = jo.getInteger("status");
		Map<String, String> res = orderService.updateStatus(userId, orderNo, status);
		if(StringUtils.isEmpty(res.get("msg"))){
			return CommonUtils.printObjStr(res);
		}else{
			return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
		}
	}
	
	/**
	 * 支付通知
	 * @param body
	 * @return
	 */
	@RequestMapping(value="/notify", method = RequestMethod.GET)
	public String notify(@RequestBody String body) {
		logger.debug("notify start.body=" + body);
		try{
			request.setCharacterEncoding("UTF-8");
			String Name = request.getParameter("Name");
			String Version = request.getParameter("Version");
			String Charset = request.getParameter("Charset");
			String TraceNo = request.getParameter("TraceNo");
			String MsgSender = request.getParameter("MsgSender");
			String SendTime = request.getParameter("SendTime");
			String MerchantNo = request.getParameter("MerchantNo");
			String InstCode = request.getParameter("InstCode");
			String OrderNo = request.getParameter("OrderNo");
			String OrderAmount = request.getParameter("OrderAmount");
			String TransNo = request.getParameter("TransNo");
			String TransAmount = request.getParameter("TransAmount");
			String TransStatus = request.getParameter("TransStatus");
			String TransType = request.getParameter("TransType");
			String TransTime = request.getParameter("TransTime");
			String ErrorCode = request.getParameter("ErrorCode");
			String ErrorMsg = request.getParameter("ErrorMsg");
			String Ext1 = request.getParameter("Ext1");
			String SignType = request.getParameter("SignType");
			String BankSerialNo = request.getParameter("BankSerialNo");
			String SignMsg = request.getParameter("SignMsg");
			logger.info("获取的盛付通后台的通知消息内容为：\nName=" + Name + "\nVersion="
					+ Version + "\nCharset=" + Charset + "\nTraceNo=" + TraceNo
					+ "\nMsgSender=" + MsgSender + "\nSendTime=" + SendTime
					+ "\nMerchantNo=" + MerchantNo + "\nInstCode=" + InstCode
					+ "\nOrderNo=" + OrderNo + "\nOrderAmount=" + OrderAmount
					+ "\nTransNo=" + TransNo + "\nTransAmount=" + TransAmount
					+ "\nTransStatus=" + TransStatus + "\nTransType="
					+ TransType + "\nTransTime=" + TransTime + "\nErrorCode="
					+ ErrorCode + "\nErrorMsg=" + ErrorMsg + "\nExt1=" + Ext1
					+ "\nSignType=" + SignType + "\nBankSerialNo=" + BankSerialNo
					+ "\nSignMsg=" + SignMsg);
			//第一步进行相关的验签操作
			String encryptCode = Name + Version + Charset + TraceNo + MsgSender
					+ SendTime + InstCode + OrderNo + OrderAmount + TransNo
					+ TransAmount + TransStatus + TransType + TransTime
					+ MerchantNo + ErrorCode + ErrorMsg + Ext1 
					+ SignType;
			logger.info("加签原始串："+encryptCode);
			//默认进行MD5验签操作
			String signMd5Msg=MD5.sign(encryptCode, HybConstants.MERCHANTMY, "gb2312");
			logger.info("加签串："+signMd5Msg);
			if(SignMsg!=null&&SignMsg.equalsIgnoreCase(signMd5Msg)){
				//处理自己相关的逻辑，可以选择入库，然后，前台隔断时间扫描数据库获取相关标识是否获取到数据
				logger.info("处理相关逻辑成功 OrderNo=" + OrderNo);
				//根据orderNo更新数据库状态
				int r = 0;
				try {
					if(OrderNo.startsWith("U")){
						//用户充值订单
						r = orderService.userRecharge(Integer.valueOf(TransStatus), OrderNo, OrderAmount, TransAmount);
					}else{
						r = orderService.updateOrder(Integer.valueOf(TransStatus), OrderNo, OrderAmount, TransAmount);
					}
					if(r > 0){
						return "OK";//盛付通后台通过notifyUrl通知商户,商户做业务处理后,需要以字符串(OK)的形式反馈处理结果处理成功,盛付通系统收到此结果后不再进行后续通知
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	} 
	
	private String checkOrderParam(OrderBean order){
		return null;
	}
	
	private String checkPfpOrderParam(Long userId, Long bankId, String orderNo){
		return null;
	}
	
	private String checkPfpOrderParam(String sessionToken, String orderNo, String agreementNo){
		return null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
}
