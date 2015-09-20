package org.ship.shipservice.oil;

import java.util.HashMap;
import java.util.Map;

public class TestOrder extends TestBase{
	public static void main(String[] args) {
		//testUerList();
		//testAddOrder();
		//testCreateMOrder();
		//testPrecheckForPayment();
		//testPay();
		//createRecharge();
		testupdateStastus();
	}
	
	public static void testUerList(){
		String url = "http://localhost:8080/shipService/api/v1/order/list?userId=1&status=9&page=1&pageSize=10";
		System.out.println("用户订单 ="+httpGet(url));
	}
	
	public static void testAddOrder(){
		String url = "http://localhost:8080/shipService/api/v1/order/cpo";
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "1");
		params.put("productId", "1");
		params.put("productName", "汽油");
		params.put("price", "30");
		params.put("num", "3");
		params.put("couponId", "1");
		params.put("osId", "1");
		params.put("op", "3");  //1 3
		params.put("bookTime", "20150919090909");
		System.out.println("创建订单 ="+httpPost(url, params));
	}
	
	public static void testPrecheckForPayment(){
		String url = "http://localhost:8080/shipService/api/v1/order/pfp";
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "1");
		params.put("orderNo", "H201509191144264339241303249");
		params.put("bankId", "6");
		System.out.println("支付预校验 ="+httpPost(url, params));
	}
	
	public static void testPay(){
		String url = "http://localhost:8080/shipService/api/v1/order/pay";
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "1");
		params.put("orderNo", "H201509191144264339241303249");
		params.put("code", "6444");
		System.out.println("支付预校验 ="+httpPost(url, params));
	}
	
	public static void createRecharge(){
		String url = "http://localhost:8080/shipService/api/v1/order/prc";
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "1");
		params.put("bankId", "6");
		params.put("amount", "6444");
		System.out.println("支付预校验 ="+httpPost(url, params));
	}
	
	public static void testCreateMOrder(){
		String url = "http://localhost:8080/shipService/api/v1/order/cmo";
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "1");
		params.put("productName", "6");
		params.put("num", "6444");
		params.put("bookTime", "20150909090909");
		params.put("addr", "成都");
		System.out.println("支付预校验 ="+httpPost(url, params));
	}
	
	public static void testupdateStastus(){
		String url = "http://localhost:8080/shipService/api/v1/order/us";
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", "1");
		params.put("orderNo", "H201509201144274364220804095");
		params.put("status", "12");
		System.out.println("支付预校验 ="+httpPost(url, params));
	}
}
