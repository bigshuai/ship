package org.ship.shipservice.oil;

public class TestCoupon  extends TestBase{
	public static void main(String[] args) {
		testCouponList();
	}
	
	public static void testCouponList(){
		String url = "http://localhost:8080/shipService/api/v1/coupon/list?osId=1";
		System.out.println("ccc ="+httpGet(url));
	}
}
