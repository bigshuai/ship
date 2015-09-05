package org.ship.shipservice.service.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ship.shipservice.constants.HybConstants;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sj.pay.client.PayResponse;
import com.sj.pay.client.SjPayClient;
import com.sj.pay.client.domain.OrderRes;
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
	
	public ResultList findOrderByUserId(Integer userId ,Integer status){
		ResultList r = new ResultList();
		List<Order> list = orderDao.findByUserId(userId, status);
		r.setDataList(list);
		return r;
	}
	
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
	
	public Map<String, String> precheckForPayment(String sessionToken, String code, String orderNo){
		try {
			SjPayClient client = SjPayClient.getInstance();
			PayInfo info = new PayInfo();
			info.setSessionToken("");
			info.setAgreementNo("");
//			info.setOutMemberId(outMemberId);
			
//			new NameValuePair("outMemberId", UUID.randomUUID().toString()
//					.substring(0, 30)),
//			new NameValuePair("bankCode", "CMBC"),
//			new NameValuePair("bankCardType", "DR"),
//			new NameValuePair("bankCardNo", "6226228023505478"),
//			new NameValuePair("realName", "zhangsan"),
//			new NameValuePair("idNo", "320114198004123791"),
//			new NameValuePair("idType", "IC"),
//			new NameValuePair("mobileNo", "13166344393"),
//			new NameValuePair("userIp", "127.0.0.1"),
//			new NameValuePair(
//					"riskExtItems",
//					"{\"outMemberId\":\"outMemberId\",\"outMemberRegistTime\":\"20110707112233\",\"outMemberRegistIP\":\"127.0.0.1\",\"outMemberVerifyStatus\":\"1\",\"outMemberName\":\"aa\",\"outMemberMobile\":\"15521366554\"}") };

			
			client.precheckForPayment(info);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
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
