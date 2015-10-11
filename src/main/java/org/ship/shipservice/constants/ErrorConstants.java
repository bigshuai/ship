package org.ship.shipservice.constants;

public class ErrorConstants {
	
	/**成功*/
	public static final String SUCCESS = "200";
	
	public static final String PARAM_ERRO = "10010001";
	
	/**优惠券领取异常*/
	public static final String COUPON_GET_ERROR = "10020001";
	/**优惠券已经领取*/
	public static final String COUPON_GET_EXIST_DAY_ERROR = "10020002";
	/**优惠券已过期*/
	public static final String COUPON_GET_OVERTIME_ERROR = "10020003";
	
	public static final String BANK_GET_INFO_ERROR = "10030001";
	
	public static final String PRECHECK_FOR_SIGN_ERROR = "10030002";
	
	public static String getErrorMsg(String code){
		if(SUCCESS.equalsIgnoreCase(code)){
			return "请求完成";
		}else if(COUPON_GET_EXIST_DAY_ERROR.equalsIgnoreCase(code)){
			return "今天已经领取过该优惠券";
		}else if(COUPON_GET_OVERTIME_ERROR.equalsIgnoreCase(code)){
			return "优惠券已过期";
		}else if(COUPON_GET_ERROR.equalsIgnoreCase(code)){
			return "优惠券领取异常";
		}else if(BANK_GET_INFO_ERROR.equalsIgnoreCase(code)){
			return "获取卡信息失败";
		}
		return "";
	}
	
}
