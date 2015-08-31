package org.ship.shipservice.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.ship.shipservice.domain.CouponBean;
import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.domain.ResultList;

import com.alibaba.fastjson.JSON;

/**
 * @author zhf 通用工具类
 */

public class CommonUtils {
	/**
	 * 随机生成验证码
	 * 
	 * @param numberFlag
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890"
				: "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return retStr;
	}

	/**
	 * HTTP接口 发送短信
	 * http://sms.1xinxi.cn/asmx/smsservice.aspx?name=登录名&pwd=接口密码&mobile
	 * =手机号码&content=内容&sign=签名&stime=发送时间&type=pt&extno=自定义扩展码
	 * 
	 * @param phone
	 * @return
	 */
	public static String sendMessage(Integer phone, String content) {
		String inputline = "";
		// 发送内容
		String sign = "签名";
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(
				"http://sms.1xinxi.cn/asmx/smsservice.aspx?");
		// 向StringBuffer追加用户名
		sb.append("name=登录名、手机或qq");
		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=fa246d0262c3925617b0c72bb20eeb");
		// 向StringBuffer追加手机号码
		sb.append("&mobile=13339196131,15375379376");
		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content=" + URLEncoder.encode(content));
		// 追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");
		// 加签名
		sb.append("&sign=" + URLEncoder.encode(sign));
		// type为固定值pt extno为扩展码，必须为数字 可为空
		sb.append("&type=pt&extno=");
		// 创建url对象
		try {
			URL url = new URL(sb.toString());
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");
			// 发送
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			// 返回发送结果
			inputline = in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputline;
	}
	
	public static <T> String printListStr(T list, int size){
		ResResult<ResultList<T>> result = new ResResult<ResultList<T>>();
		ResultList<T> reulstList = new ResultList<T>();
		reulstList.setDataList(list);
		reulstList.setSize(size);
		result.setResult(reulstList);
		return JSON.toJSONString(result);
	}
	
	public static <T> String printListStr(T list){
		ResResult<ResultList<T>> result = new ResResult<ResultList<T>>();
		ResultList<T> reulstList = new ResultList<T>();
		reulstList.setDataList(list);
		try {
			Method m = List.class.getMethod("size");
			Object size = m.invoke(list);
			reulstList.setSize(Integer.valueOf(size.toString()));
			result.setResult(reulstList);
			return JSON.toJSONString(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static <T> String printObjStr(T obj){
		ResResult<T> result = new ResResult<T>();
		result.setResult(obj);
		return JSON.toJSONString(result);
	}
	
	public static String printObjStr2(Object obj){
		return JSON.toJSONString(obj);
	}
}
