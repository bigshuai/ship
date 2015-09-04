package org.ship.shipservice.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 公司名称:  
 * 项目名称： 
 * 模块名称： 
 * @Title:	RequestUtil.java
 * @Description: 
 * @author:    lyhc
 * Create at:  11:24:40 PM      Jan 6, 2013
 * @version:
 */
public class RequestUtil {
	public static Logger LOG = Logger.getLogger(RequestUtil.class);
	
	/**
	 * 
	 * @param body
	 * @return
	 */
	public static JSONObject convertBodyToJsonObj(String body){
		body = body.replaceAll("=&", "=null&");
		String[] values = body.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for(int i = 0 ; i < values.length ; i++){
			String[] target = values[i].split("=");
			map.put(target[0], target[1]);
		}
		JSONObject obj = new JSONObject();
		obj.putAll(map);
		return obj;
	}
	
	/**
	 * 
	 * @param path
	 * @param request
	 * @param response
	 */
	public static void forward(String path, HttpServletRequest request, ServletResponse response){
		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Exception e) {
			LOG.error("forward error:" + e);
		}
	}
	
	/**
	 * 
	 * @param location
	 * @param request
	 * @param response
	 */
	public static void sendRedirect(String location, ServletRequest request, HttpServletResponse response){
		try {
			response.sendRedirect(location);
		} catch (IOException e) {
			LOG.error("forward error:" + e);
		}
	}
}
