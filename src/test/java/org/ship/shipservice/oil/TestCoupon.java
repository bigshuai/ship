package org.ship.shipservice.oil;

import java.util.HashMap;
import java.util.Map;

public class TestCoupon  extends TestBase{
	public static void main(String[] args) {
		testCouponList();
		testUserCouponList();
		testAddCoupon();
	}
	
	public static void testCouponList(){
		String url = "http://localhost:8080/shipService/api/v1/coupon/list?osId=1&page=1&pageSize=10";
		System.out.println("优惠券列表 ="+httpGet(url));
	}
	
	public static void testUserCouponList(){
		String url = "http://localhost:8080/shipService/api/v1/coupon/user?page=2&pageSize=10";
		System.out.println("用户优惠券列表 ="+httpGet(url));
	}
	
	public static void testAddCoupon(){
		String url = "http://localhost:8080/shipService/api/v1/coupon/get";
		Map<String, String> params = new HashMap<String, String>();
		params.put("cid","1");
		System.out.println("领取优惠券 ="+httpPost(url, params));
	}
}
