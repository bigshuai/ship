package org.ship.shipservice.oil;

import java.util.HashMap;
import java.util.Map;


public class TestBank  extends TestBase{
	public static void main(String[] args) {
		testBankInfo();
		testUserBankList();
		//testPrecheckForSign();
		testSign();
	}
	
	public static void testBankInfo(){
		String url = "http://localhost:8080/shipService/api/v1/bank/info?bcn=000000";
		System.out.println("银行卡信息 ="+httpGet(url));
	}
	
	public static void testUserBankList(){
		String url = "http://localhost:8080/shipService/api/v1/bank/list";
		System.out.println("银行卡信息 ="+httpGet(url));
	}
	
	public static void testPrecheckForSign(){
		String url = "http://localhost:8080/shipService/api/v1/bank/pfs";
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid","123");
		params.put("bankCode","ICBC");
		params.put("bankName", "工商银行");
		params.put("bankCardType","DR");
		params.put("bankCardNo","P800069610785555");
		params.put("realName","王洪侠");
		params.put("idNo","210124198508162281");
		params.put("idType","IC");
		params.put("mobileNo","13166344393");
		System.out.println("领取优惠券 ="+httpPost(url, params));
	}
	
	public static void testSign(){
		String url = "http://localhost:8080/shipService/api/v1/bank/sign";
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid","1");
		params.put("requestNo","92976a01-0f35-41a7-acea-f0-123");
		params.put("code", "123456");
		System.out.println("领取优惠券 ="+httpPost(url, params));
	}
}
