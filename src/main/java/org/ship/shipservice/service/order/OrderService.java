package org.ship.shipservice.service.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.domain.OrderBean;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.entity.Coupon;
import org.ship.shipservice.entity.Oil;
import org.ship.shipservice.entity.Order;
import org.ship.shipservice.repository.CouponDao;
import org.ship.shipservice.repository.OilStationDao;
import org.ship.shipservice.repository.OrderDao;
import org.ship.shipservice.rest.BankController;
import org.ship.shipservice.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	
	private OrderDao orderDao;
	
	@Autowired
	private OilStationDao oilStationDao;
	
	@Autowired
	private CouponDao couponDao;
	
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
	 * @return
	 */
	public Map<String, String> createOrder(OrderBean order) {
		SjPayClient client = SjPayClient.getInstance();
		PaymentOrder pOrder = new PaymentOrder();
		Map<String, String> result = new HashMap<String, String>();
		try {
			//根据ID获取最新商品信息
			List<Oil> oils = oilStationDao.queryOil(Long.valueOf(order.getProductId()));
			Oil oil = oils.get(0);
			order.setProductName(oil.getName());
			order.setProductDesc(oil.getName());
			order.setPrice(oil.getPrice()+"");
			BigDecimal amount = this.calculateAmount(oil, Long.valueOf(order.getProductId()), 
					order.getNum(), order.getOsId(), order.getCouponId());
			order.setAmount(amount.toPlainString());
			pOrder.setMerchantNo(HybConstants.MERCHANTNO);
			pOrder.setMerchantOrderNo(CommonUtils.getMerchantOrderNo(order.getUserId()+""));
			pOrder.setProductName(order.getProductName());
			pOrder.setProductDesc(order.getProductDesc());
			pOrder.setCurrency("CNY");
			pOrder.setAmount(order.getAmount());
			pOrder.setNotifyUrl(HybConstants.NOTIFYURL);
			pOrder.setUserIP(CommonUtils.getIp());
			PayResponse<OrderRes> res = client.createPaymentOrder(pOrder);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode())){
					String orderNo = pOrder.getMerchantOrderNo();
					String sftOrderNo = res.getObj().getSftOrderNo();
					String sessionToken = res.getObj().getSessionToken();
					int r = orderDao.addOrder(order.getUserId(), order.getOsId(), oil.getId(), oil.getName(), 1, pOrder.getAmount(),
							order.getPrice(), order.getNum(), 0, orderNo, sftOrderNo, sessionToken, null);
					if(r > 0){
						result.put("orderNo", orderNo);
						result.put("orderCreateTime", res.getObj().getOrderCreateTime());
						result.put("sessionToken", sessionToken);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
			info.setExts(this.getExt(outMemberId));
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
	public Map<String, String> payment(String sessionToken, String code){
		Map<String, String> result = new HashMap<String, String>();
		try {
			SjPayClient client = SjPayClient.getInstance();
			PayConfirmInfo info = new PayConfirmInfo();
			info.setSessionToken(sessionToken);
			info.setValidateCode(code);
			info.setSign(true);
			info.setUserIp(CommonUtils.getIp());
			PayResponse<PayConfirmRes> res= client.payment(info);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode()) 
						&& HybConstants.PAY_SUCCESS.equalsIgnoreCase(res.getObj().getPaymentStatus())){
					result.put("orderNo", res.getObj().getMerchantOrderNo());
					result.put("status", res.getObj().getPaymentStatus());
					result.put("orderAmount", res.getObj().getOrderAmount());
					result.put("payableAmount", res.getObj().getPayableAmount());
					try {
						orderDao.updateOrderStatus(9, res.getObj().getMerchantOrderNo());
					} catch (Exception e) {
						logger.error("订单更新失败 orderNo=" + res.getObj().getMerchantOrderNo() ,e);
					}
					return null;
				}else{
					result.put("msg", res.getReturnMsg());
				}
			}else{
				result.put("msg", "结算失败，请稍后再试。");
			}
		} catch (IOException e) {
			logger.error("接口调用失败 sessionToken=" + sessionToken,e);
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
	
	private BigDecimal calculateAmount(Oil oil, Long productId,Integer num, Long osId, Long couponId){
		BigDecimal totalPtice = oil.getPrice().multiply(new BigDecimal(num));
		
		//获取优惠详细
		List<Object[]> list = oilStationDao.queryDetailById(osId);
		if(list != null && list.size() > 0){
			Object[] o = list.get(0);
			String info = o[11]+"";
			totalPtice = this.calculateDerate(totalPtice, info);
		}
		
		//获取优惠券信息
		totalPtice = calculateCoupon(totalPtice, couponId);
		return totalPtice;
	}
   
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
	
	private BigDecimal calculateCoupon(BigDecimal totalPtice,Long couponId){
		Coupon coupon = couponDao.findOne(couponId);
		if(coupon.getLimitValue() == null || coupon.getLimitValue() <= 0 || totalPtice.floatValue() > coupon.getLimitValue()){
			totalPtice = totalPtice.subtract(new BigDecimal(coupon.getFaceValue()));
		}
		return totalPtice;
	}
	
	public OrderDao getOrderDao() {
		return orderDao;
	}
	@Autowired
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
}
