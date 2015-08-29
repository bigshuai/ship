package org.ship.shipservice.constants;

public class ErrorConstants {
	public static final String SUCCESS = "0";
	
	public static String getErrorMsg(String code){
		if(SUCCESS.equalsIgnoreCase(code)){
			return "请求完成";
		}
		return "";
	}
	
}
