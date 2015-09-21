package org.ship.shipservice.service.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.OrderBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.Coupon;
import org.ship.shipservice.entity.Oil;
import org.ship.shipservice.entity.Order;
import org.ship.shipservice.repository.BankDao;
import org.ship.shipservice.repository.CouponDao;
import org.ship.shipservice.repository.OilStationDao;
import org.ship.shipservice.repository.OrderDao;
import org.ship.shipservice.repository.UserDao;
import org.ship.shipservice.rest.BankController;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.MyConstant;
import org.ship.shipservice.utils.rsa.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.sj.pay.client.PayResponse;
import com.sj.pay.client.SjPayClient;
import com.sj.pay.client.domain.OrderRes;
import com.sj.pay.client.domain.PayConfirmInfo;
import com.sj.pay.client.domain.PayConfirmRes;
import com.sj.pay.client.domain.PayInfo;
import com.sj.pay.client.domain.PaymentOrder;
/**
 * @author zhf
 *
 */
@Component
@Transactional
public class OrderService {
	private static Logger logger = LoggerFactory.getLogger(BankController.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OilStationDao oilStationDao;
	
	@Autowired
	private CouponDao couponDao;
	
	@Autowired
	private BankDao bankDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired  
    private RedisTemplate<String, String> redisTemplate;  
	
	public Page<Order> findByUserIdAndStatus(Integer userId ,Integer status,PageRequest pageRequest){
//		Page<Order> list = orderDao.findByUserIdAndStatus(userId, status,pageRequest);
//		return list;
		return null;
	}
	
	/**
	 * 
	 * @param userId
	 * @param status 1-进行中   9-已完成
	 * @return
	 */
	public ResultList queryOrderUserList(Long userId, Integer status, int page, int pageSize){
		//订单状态  0-新建 1-已付款  2-配送中 3-已收货 4-已完成 11-预订未付款 12预订已付款    99-删除
		ResultList r = new ResultList();
		String statusStr = "4";
		switch (status) {
			case 1:
				statusStr = "0,1,2,3,11,12";
				break;
			case 9:
				statusStr = "4";
				break;
			default:
				return r;
		}
		int start = (page - 1)*pageSize;
		List<Object[]> list = orderDao.queryOrderForUserId(userId, statusStr, start, pageSize);
		List<OrderBean> result = new ArrayList<OrderBean>();
		//o.id,o.os_id,s.name,o.product_id,o.product_name,o.type,o.money,o.price,o.num,o.status,o.order_no,
		//o.sft_order_no,o.book_time,o.create_time
		for(Object[] o : list){
			OrderBean ob = new OrderBean();
			ob.setId(Long.valueOf(o[0]+""));
			ob.setOsId(Long.valueOf(o[1]+""));
			ob.setOsName(o[2]+"");
			ob.setProductId(o[3]+"");
			ob.setProductName(o[4]+"");
			ob.setType(Integer.valueOf(o[5]+""));
			ob.setMoney(o[6]+"");
			ob.setPrice(o[7]+"");
			ob.setNum(Integer.valueOf(o[8]+""));
			ob.setStatus(Integer.valueOf(o[9]+""));
			ob.setOrderNo(o[10]+"");
			ob.setSftOrderNo(o[11]+"");
			ob.setBookTime(o[12]+"");
			ob.setCreateTime(o[13]+"");
			result.add(ob);
		}
		r.setDataList(result);
		r.setPage(page);
		r.setTotal(orderDao.queryOrderForUserIdTotal(userId, statusStr));
		return r;
	}
	
	/**
	 * 
	 * @param order
	 * @param op  1-实时订单
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createOrder(OrderBean order, Integer op) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		SjPayClient client = SjPayClient.getInstance();
		PaymentOrder pOrder = new PaymentOrder();
		try {
			//根据ID获取最新商品信息
			List<Oil> oils = oilStationDao.queryOil(Long.valueOf(order.getProductId()));
			Oil oil = oils.get(0);
			order.setProductName(oil.getName());
			order.setProductDesc(oil.getName());
			order.setPrice(oil.getPrice()+"");
			BigDecimal amount = this.calculateAmount(order.getUserId(), oil, Long.valueOf(order.getProductId()), 
					order.getNum(), order.getOsId(), order.getCouponId());
			amount = new BigDecimal("0.01");
			order.setAmount(amount.toPlainString());
			pOrder.setMerchantNo(HybConstants.MERCHANTNO);
			pOrder.setMerchantOrderNo(CommonUtils.getMerchantOrderNo(order.getUserId()+""));
			pOrder.setProductName(order.getProductName());
			pOrder.setProductDesc(order.getProductDesc());
			pOrder.setCurrency("CNY");
			pOrder.setAmount(order.getAmount());
			pOrder.setNotifyUrl(HybConstants.NOTIFYURL);
			pOrder.setUserIP(CommonUtils.getIp());
			String orderNo = pOrder.getMerchantOrderNo();
			PayResponse<OrderRes> res = client.createPaymentOrder(pOrder);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode())){
					String sftOrderNo = res.getObj().getSftOrderNo();
					String sessionToken = res.getObj().getSessionToken();
					int r = orderDao.addOrder(order.getUserId(), order.getOsId(), oil.getId(), oil.getName(), 1, pOrder.getAmount(),
							order.getPrice(), order.getNum(), 0, orderNo, sftOrderNo, sessionToken, null, null);
					if(r > 0){
						result.put("orderNo", orderNo);
						result.put("orderCreateTime", res.getObj().getOrderCreateTime());
						//result.put("sessionToken", sessionToken);
						result.put("amount", pOrder.getAmount());
						return result;
					}else{
						 throw new RuntimeException("订单创建失败，请稍后再试。");
					}
				}else{
					throw new RuntimeException("订单创建失败，请稍后再试。");
				}
			}else{
				result.put("msg", "订单创建失败，请稍后再试。");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 创建预约订单 
	 *    预约订单保存基本的订单信息到order表， 不往支付那边创建订单。后台推送后由前台
	 * @return
	 */
	public Map<String, String> createMakeOrder(OrderBean order){
		Map<String, String> result = new HashMap<String, String>();
		Date bt = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			bt = format.parse(order.getBookTime());
		} catch (ParseException e) {
		}
		String orderNo = CommonUtils.getMerchantOrderNo(order.getUserId()+"");
		int r = orderDao.addOrder(order.getUserId(), null, null, order.getProductName(), 3, null,
				null, order.getNum(), 11, orderNo, null, null, bt, order.getBookAddr());
		if(r <= 0){
			result.put("msg", "预约失败，请稍后再试。");
		}
		return result;
	}
	
	/**
	 * 创建充值订单
	 * @param order
	 * @param bankId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> rechargeOrder(OrderBean order, Long bankId) throws Exception{
		SjPayClient client = SjPayClient.getInstance();
		Map<String, String> result = new HashMap<String, String>();
		PaymentOrder pOrder = new PaymentOrder();
		pOrder.setMerchantNo(HybConstants.MERCHANTNO);
		pOrder.setMerchantOrderNo(CommonUtils.getRechargeOrderNo(order.getUserId()+""));
		pOrder.setProductName("充值");
		pOrder.setProductDesc("充值");
		pOrder.setCurrency("CNY");
		pOrder.setAmount(order.getAmount());
		pOrder.setNotifyUrl(HybConstants.NOTIFYURL);
		pOrder.setUserIP(CommonUtils.getIp());
		//创建消费信息
		PayResponse<OrderRes> res = client.createPaymentOrder(pOrder);
		if(res.getHttpCode()==200 && res.isSignResult()){
			if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode())){
				String sftOrderNo = res.getObj().getSftOrderNo();
				String sessionToken = res.getObj().getSessionToken();
				int r = orderDao.addOrder(order.getUserId(), null, null, null, 2, pOrder.getAmount(),
						pOrder.getAmount(), 1, 0, pOrder.getMerchantOrderNo(), sftOrderNo, sessionToken, null, null);
				if(r > 0){
					result.put("orderNo", pOrder.getMerchantOrderNo());
					result.put("orderCreateTime", res.getObj().getOrderCreateTime());
					result.put("amount", pOrder.getAmount());
					return result;
				}else{
					result.put("msg", "订单创建失败，请稍后再试。");
				}
			}else{
				result.put("msg", res.getReturnMsg());
			}
		}else{
			result.put("msg", "订单创建失败，请稍后再试。");
		}
		return result;
	}
	
//	/**
//	 * 
//	 * @param order
//	 * @return
//	 */
//	public Map<String, String> createOrder(OrderBean order, Integer op) throws Exception{
//		if(op.equals(1)){
//			return this.createOrder(order);
//		}else{
//			//创建订单
//			List<Oil> oils = oilStationDao.queryOil(Long.valueOf(order.getProductId()));
//			Oil oil = oils.get(0);
//			BigDecimal amount = this.calculateAmount(oil, Long.valueOf(order.getProductId()), 
//					order.getNum(), order.getOsId(), order.getCouponId());
//			String orderNo = CommonUtils.getMerchantOrderNo(order.getUserId()+"");
//			int r = orderDao.addOrder(order.getUserId(), order.getOsId(), oil.getId(), oil.getName(), 1, amount.toPlainString(),
//					order.getPrice(), order.getNum(), 11, orderNo, null, null, null);
//			Map<String, String> result = new HashMap<String, String>();
//			if(r > 0){
//				result.put("orderNo", orderNo);
//				result.put("orderCreateTime", "");
//				result.put("sessionToken", "");
//				result.put("amount", amount.toPlainString());
//			}else{
//				result.put("msg", "订单创建失败，请稍后再试。");
//			}
//			return null;
//		}
//	}
	
	public String precheckForPayment(Long userId, Long bankId, String orderNo){
		try {
			//根据bankId获取 agreementNo（签约协议号）
			//根据orderNo获取sessionToken（会话token）
			SjPayClient client = SjPayClient.getInstance();
			PayInfo info = new PayInfo();
			info.setMerchantNo(HybConstants.MERCHANTNO);
			info.setSessionToken(orderDao.querySessionToken(orderNo, userId));
			//info.setAgreementNo(bankDao.getAgreementNo(bankId, userId));
			Object[] bi = bankDao.queryPayBankForId(userId, bankId);
			Object[] bankInfo = (Object[]) bi[0];
			//t.agreement_no, t.out_member_id,t.real_name,t.id_no, t.id_type,t.mobile_no
			info.setAgreementNo(bankInfo[0]+"");
			info.setOutMemberId(bankInfo[1]+"");
			info.setRealName(RSAUtil.decrypt(bankInfo[2]+""));
			info.setIdNo(RSAUtil.decrypt(bankInfo[3]+""));
			info.setIdType(bankInfo[4]+"");
			info.setMobileNo(RSAUtil.decrypt(bankInfo[5]+""));
			info.setUserIp(CommonUtils.getIp());
			info.setUserIp(CommonUtils.getIp());
			info.setRiskExtItems(this.getExt(info.getOutMemberId()));
			PayResponse<String> res= client.precheckForPayment(info);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode())){
					return null;
				}else{
					return res.getReturnMsg();
				}
			}else{
				 return  "结算失败，请稍后再试。";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String precheckForPayment(String sessionToken, String orderNo,String agreementNo){
		try {
			SjPayClient client = SjPayClient.getInstance();
			PayInfo info = new PayInfo();
			info.setSessionToken(sessionToken);
			info.setAgreementNo(agreementNo);
			String outMemberId = UUID.randomUUID().toString().substring(0, 30);
			info.setOutMemberId(outMemberId);
			info.setUserIp(CommonUtils.getIp());
			info.setRiskExtItems(this.getExt(outMemberId));
			PayResponse<String> res= client.precheckForPayment(info);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode())){
					return null;
				}else{
					return res.getReturnMsg();
				}
			}else{
				 return  "结算失败，请稍后再试。";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 订单支付 先更新数据库 在调用接口 接口失败 事物回滚
	 * @param sessionToken
	 * @param code
	 * @return
	 */
	public Map<String, String> payment(Long userId, String orderNo, String code) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		try {
			SjPayClient client = SjPayClient.getInstance();
			PayConfirmInfo info = new PayConfirmInfo();
			info.setMerchantNo(HybConstants.MERCHANTNO);
			info.setSessionToken(orderDao.querySessionToken(orderNo, userId));
			info.setValidateCode(code);
			info.setSign(true);
			info.setUserIp(CommonUtils.getIp());
			info.setExts("{}");
			PayResponse<PayConfirmRes> res= client.payment(info);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode()) 
						&& HybConstants.PAY_SUCCESS.equalsIgnoreCase(res.getObj().getPaymentStatus())){
					result.put("orderNo", res.getObj().getMerchantOrderNo());
					result.put("status", res.getObj().getPaymentStatus());
					result.put("orderAmount", res.getObj().getOrderAmount());
					result.put("payableAmount", res.getObj().getPayableAmount());
					return result;
				}else{
					throw new RuntimeException(res.getReturnMsg());
				}
			}else{
				throw new RuntimeException("结算失败，请稍后再试。");
			}
		} catch (IOException e) {
			logger.error("接口调用失败 orderNo=" + orderNo,e);
		}
		return result;
	}
	
	/**
	 * 充值支付预
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public Map<String, String> cRechargePay(Long userId, String orderNo) {
		Map<String, String> result = new HashMap<String, String>();
		//根据orderNo获取金额
		String amount = orderDao.queryOrderMoney(orderNo, userId);
		//查询余额
		String userFund = userDao.getUserFund(userId);
		if(StringUtils.isEmpty(userFund)){
			result.put("msg", "用户不存在支付账户。");
			return result;
		}
		
		if(Double.valueOf(amount) > Double.valueOf(userFund)){
			result.put("msg", "账户余额不足。");
			return result;
		}
		//发送短信
		String phone = userDao.getUserPhone(userId);
		String code = CommonUtils.createRandom(true, 6);
		redisTemplate.opsForValue().set(phone, code,30l,TimeUnit.MINUTES);//验证码30分钟失效
		String message=CommonUtils.sendMessage(phone, code);
		if(!message.split(",")[0].equals("0")){
			result.put("msg", "短信验证码发送失败，请稍后再试。");
		}
		return result;
	}
	
	public Map<String, String> rechargePay(Long userId, String orderNo,
			String code) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		String amount = orderDao.queryOrderMoney(orderNo, userId);
		//查询余额
		String userFund = userDao.getUserFund(userId);
		if(StringUtils.isEmpty(userFund)){
			result.put("msg", "用户不存在支付账户。");
			return result;
		}
		
		if(Double.valueOf(amount) > Double.valueOf(userFund)){
			result.put("msg", "账户余额不足。");
			return result;
		}
		String phone = userDao.getUserPhone(userId);
		String rCode = redisTemplate.opsForValue().get(phone);
		if(StringUtils.isEmpty(rCode)){
			result.put("msg", "验证码错误，请重新获取。");
			return result;
		}
		
		if(!rCode.equalsIgnoreCase(code)){
			result.put("msg", "验证码错误，请重新获取。");
			return result;
		}
		
		int rr = orderDao.updateUserOrderStatus(1, orderNo);
		if(rr > 0){
			int r = userDao.deductCharge(userId, amount);
			if(r <= 0){
				throw new RuntimeException("支付失败，请稍后重试。");
			}
		}else{
			result.put("msg", "支付失败，请稍后重试。");
		}
		return result;
	}
	
	/**
	 * 订单支付通知
	 * @param status
	 * @param orderNo
	 * @param amount
	 * @param transAmount
	 * @return
	 * @throws Exception
	 */
	public int updateOrder(Integer status, String orderNo, String amount, String transAmount) throws Exception{
		//记录消费日志
		Long userId = orderDao.queryUserId(orderNo);
		int r = orderDao.insertConsumeLog(userId, orderNo, amount, transAmount, 1, "", status);
		if(r > 0){
			int rr = orderDao.updateBankOrderStatus(status, orderNo);
			if(rr > 0){
				return 1;
			}else{
				throw new RuntimeException("异常");
			}
		}
		//更新订单状态
		return 0;
	}
	
	
	/**
	 * 用户充值通知
	 * @param status
	 * @param orderNo
	 * @param amount
	 * @param transAmount
	 * @return
	 * @throws Exception
	 */
	public int userRecharge(Integer status, String orderNo, String amount, String transAmount) throws Exception{
		Long userId = orderDao.queryUserId(orderNo);
		int r = orderDao.insertConsumeLog(userId, orderNo, amount, transAmount, 2, "", status);
		if(r > 0){
			int rr = orderDao.updateBankOrderStatus(status, orderNo);
			if(rr > 0){
				int rrr = userDao.recharge(userId, transAmount);
				if(rrr > 0){
					return 1;
				}else{
					throw new RuntimeException("异常");
				}
			}else{
				throw new RuntimeException("异常");
			}
		}
		return 0;
	}
	
	public Map<String, String> updateStatus(Long userId, String orderNo, Integer status) {
		Map<String, String> result = new HashMap<String, String>();
		int r = orderDao.updateOrderStatus(status,userId, orderNo);
		if(r <= 0){
			result.put("msg", "订单状态更新失败，请稍后重试。");
		}
		return result;
	}
	
	private String getExt(String outMemberId){
		Map<String, String> exts = new HashMap<String, String>();
		exts.put("outMemberId", outMemberId);
		exts.put("outMemberRegistTime", HybConstants.OUTMEMBERREGISTTIME);
		exts.put("outMemberRegistIP", HybConstants.OUTMEMBERREGISTIP);
		exts.put("outMemberVerifyStatus", HybConstants.OUTMEMBERVERIFYSTATUS);
		exts.put("outMemberName", HybConstants.OUTMEMBERNAME);
		exts.put("outMemberMobile", HybConstants.OUTMEMBERMOBILE);
		return JSON.toJSONString(exts);
	}
	
	private BigDecimal calculateAmount(Long userId, Oil oil, Long productId,Integer num, Long osId, Long couponId){
		BigDecimal totalPtice = oil.getPrice().multiply(new BigDecimal(num));
		//计算优惠券扣除后金额
		totalPtice = calculateCoupon(userId, totalPtice, couponId);
		//计算减免后金额
		List<Object[]> list = oilStationDao.queryDetailById(osId);
		if(list != null && list.size() > 0){
			Object[] o = list.get(0);
			String info = o[11]+"";
			totalPtice = this.calculateDerate(totalPtice, info);
		}
		return totalPtice;
	}
   
	/**
	 * 计算减免
	 * @param totalPtice
	 * @param info
	 * @return
	 */
	private BigDecimal calculateDerate(BigDecimal totalPtice, String info){
		if(!StringUtils.isEmpty(info)){
			String[] ss = info.split(",");
			int deratePrice = 0;
			int max = 0;
			for(String s : ss){
				String[] pp = s.split("-");
				if(Integer.valueOf(pp[0]) > max){
					max = Integer.valueOf(pp[0]);
					deratePrice = Integer.valueOf(pp[1]);
				}
			}
			totalPtice = totalPtice.subtract(new BigDecimal(deratePrice));
		}
		return totalPtice;
	}
	
	/**
	 * 计算优惠券
	 * @param totalPtice
	 * @param couponId
	 * @return
	 */
	private BigDecimal calculateCoupon(Long userId, BigDecimal totalPtice,Long couponId){
		if(StringUtils.isEmpty(couponId)){
			return totalPtice;
		}
		Object[] cou = couponDao.queryCouponInfo(userId, couponId);
		if(cou == null || cou.length <= 0){
			return totalPtice;
		}
		Object[] coupon = (Object[]) cou[0];
		if(coupon[2] == null || Integer.valueOf(coupon[2]+"") <= 0 || totalPtice.floatValue() > Integer.valueOf(coupon[2]+"")){
			totalPtice = totalPtice.subtract(new BigDecimal(coupon[1]+""));
		}
		return totalPtice;
	}
	
	public OrderDao getOrderDao() {
		return orderDao;
	}
	
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public OilStationDao getOilStationDao() {
		return oilStationDao;
	}

	public void setOilStationDao(OilStationDao oilStationDao) {
		this.oilStationDao = oilStationDao;
	}

	public CouponDao getCouponDao() {
		return couponDao;
	}

	public void setCouponDao(CouponDao couponDao) {
		this.couponDao = couponDao;
	}

	public BankDao getBankDao() {
		return bankDao;
	}

	public void setBankDao(BankDao bankDao) {
		this.bankDao = bankDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
