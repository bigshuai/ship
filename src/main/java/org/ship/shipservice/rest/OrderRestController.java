package org.ship.shipservice.rest;

import java.io.PrintWriter;
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
	public String getOrderList(@RequestParam("userId") Integer userId,@RequestParam("status") Integer status) {
		if(userId==0){
			return CommonUtils.printStr(MyConstant.JSON_RETURN_CODE_400, MyConstant.JSON_RETURN_MESSAGE_400);
		}else{
			ResultList orders = orderService.findOrderByUserId(userId, status);
			return CommonUtils.printListStr(orders.getDataList());
		}
	}
	
	@RequestMapping(value="/cpo", method = RequestMethod.POST)
	public String createOrder(@RequestBody String body) {
		logger.debug("createOrder start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		OrderBean order = new OrderBean();
		order.setUserId(jo.getLong("uId"));
		order.setProductId(jo.getString("productId"));
		order.setProductName(jo.getString("productName"));
		order.setPrice(jo.getString("price"));
		order.setNum(jo.getInteger("num"));
		order.setCouponId(jo.getLong("couponId"));
		order.setOsId(jo.getLong("osId"));
		
		String r = this.checkOrderParam(order);
		if(StringUtils.isEmpty(r)){
			Map<String, String> res = orderService.createOrder(order);
			if(StringUtils.isEmpty(res.get("msg"))){
				return CommonUtils.printObjStr(res);
			}else{
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res.get("msg"));
			}
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	} 
	
	@RequestMapping(value="/notify", method = RequestMethod.POST)
	public String notify(@RequestBody String body) {
		logger.debug("createOrder start.body=" + body);
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
				logger.info("处理相关逻辑成功");
				
				return "OK";//盛付通后台通过notifyUrl通知商户,商户做业务处理后,需要以字符串(OK)的形式反馈处理结果处理成功,盛付通系统收到此结果后不再进行后续通知
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	} 
	
	private String checkOrderParam(OrderBean order){
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
