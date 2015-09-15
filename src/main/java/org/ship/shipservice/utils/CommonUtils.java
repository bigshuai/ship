package org.ship.shipservice.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.domain.ResResult;
import org.ship.shipservice.domain.ResultList;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.sun.jna.platform.win32.Guid;

/**
 * @author zhf 通用工具类
 */

public class CommonUtils {
	 static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'a', 'b', 'c', 'd', 'e', 'f' };
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
	 * 上传图片
	 * @param newFileName
	 * @param filedata
	 * @param httpRequest
	 * @return
	 */
	public  static String saveFile(String newFileName, MultipartFile filedata,HttpServletRequest httpRequest) {
		// 根据配置文件获取服务器图片存放路径
		String saveFilePath =httpRequest.getRealPath("")+"/picture";
		/* 构建文件目录 */
		File fileDir = new File(saveFilePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(saveFilePath, newFileName));
			// 写入文件
			out.write(filedata.getBytes());
			out.flush();
			out.close();
			return "43.254.55.158"+httpRequest.getContextPath()+"/headImage/"+newFileName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";		
		}
	}
	/**
	 * HTTP接口 发送短信
	 * http://sms.1xinxi.cn/asmx/smsservice.aspx?name=登录名&pwd=接口密码&mobile
	 * =手机号码&content=内容&sign=签名&stime=发送时间&type=pt&extno=自定义扩展码
	 * 
	 * @param phone
	 * @return
	 */
	public static String sendMessage(String phone,String content) {
		String inputline = "";
		// 发送内容
		String sign = "航运宝";
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(
				"http://web.1xinxi.cn/asmx/smsservice.aspx?");
		// 向StringBuffer追加用户名
		sb.append("name=chenminmin008@126.com");
		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=B57C526FC74B32DEA9A9FBE7ED7A");
		// 向StringBuffer追加手机号码
		sb.append("&mobile="+phone);
		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content=" + URLEncoder.encode("欢迎注册为航运宝会员，您的验证码是"+content+",1分钟有效,如不是本人操作,请忽略。谢谢!"));
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
	
	public static Integer[] getPageInfo(HttpServletRequest request){
		Integer page = null;
		Integer pageSize = null;
		try {
			page = Integer.valueOf(request.getParameter("page"));
			pageSize = Integer.valueOf(request.getParameter("pageSize"));
		} catch (NumberFormatException e) {
		}
		
		if(page == null){
			page = 1;
		}
		
		if(pageSize == null){
			pageSize = 20;
		}
		
		return new Integer[]{page, pageSize};
	}
 	
	public static <T> String printListStr(T list, int page, int totalSize){
		ResResult<T> result = new ResResult<T>();
		result.setPage(page);
		result.setTotalSize(totalSize);
		result.setResult(list);
		return JSON.toJSONString(result);
	}
	
	public static <T> String printListStr(T list, int totalSize){
		return printListStr(list,1 , totalSize);
	}
	
	public static String printListStr(ResultList list){
		ResResult result = new ResResult();
		result.setPage(list.getPage());
		result.setTotalSize(list.getTotal());
		result.setResult(list.getDataList());
		return JSON.toJSONString(result);
	}
	
	public static <T> String printListStr(T list){
		ResResult<T> result = new ResResult<T>();
		result.setResult(list);
		return JSON.toJSONString(result);
	}
	
	public static <T> String printObjStr(T obj, String code, String msg){
		ResResult<T> result = new ResResult<T>();
		result.setCode(code);
		result.setMsg(msg);
		result.setResult(obj);
		return JSON.toJSONString(result);
	}
	
	public static <T> String printObjStr(T obj, String code){
		return printObjStr(obj, code, ErrorConstants.getErrorMsg(code));
	}
	
	public static <T> String printObjStr(T obj){
		ResResult<T> result = new ResResult<T>();
		result.setResult(obj);
		return JSON.toJSONString(result);
	}
	public static <T> String printObjStr(T obj,Integer code,String msg){
		ResResult<T> result = new ResResult<T>();
		result.setResult(obj);
		result.setCode(code+"");
		result.setMsg(msg);
		return JSON.toJSONString(result);
	}
	
	public static <T> String printStr(String code,String msg){
		ResResult<T> result = new ResResult<T>();
		result.setCode(code);
		result.setMsg(msg);
		return JSON.toJSONString(result);
	}
	
	public static String printStr(String code){
		ResResult result = new ResResult();
		result.setCode(code);
		result.setMsg(ErrorConstants.getErrorMsg(code));
		return JSON.toJSONString(result);
	}
	
	public static String printStr(){
		ResResult result = new ResResult();
		return JSON.toJSONString(result);
	}
	public static String printObjStr2(Object obj){
		return JSON.toJSONString(obj);
	}
	
	public static String getMD5(String message){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //logger.info("MD5摘要长度：" + md.getDigestLength());
            byte[] b = md.digest(message.getBytes());
            return byteToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	private static String byteToHexString(byte[] tmp) {
        String s;
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str); // 换后的结果转换为字符串
        //logger.info("MD5摘要信息：" + s);
        return s;
    }
	
	public static String getIp(){
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(StringUtils.isEmpty(ip)){
			ip = "127.0.0.1";
		}
		return ip;
	}
	
	public static String getPayRequestNo(String userId){
		return UUID.randomUUID().toString().substring(0, 25) + userId;
	}
	
	/**
	 * 获取订单号
	 * @param userId
	 * @return
	 */
	public static synchronized String getMerchantOrderNo(String userId){
		//格式  H20150908userIdSystem.currentTimeMillis()5位随机数
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return "H"+format.format(new Date())+ userId +System.currentTimeMillis() + String.format("%05d", new Random().nextInt(10000));
	}
	
	public static String decode(String str){
		try {
			if(!StringUtils.isEmpty(str)){
				return URLDecoder.decode(str,"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String decode2(String str){
		try {
			if(!StringUtils.isEmpty(str)){
				return URLDecoder.decode(URLDecoder.decode(str,"GBK"),"GBK");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		//发送内容
				String content = "尊敬的航运宝用户，你的验证码为 123456"; 
				String sign="航运宝";
				
				// 创建StringBuffer对象用来操作字符串
				StringBuffer sb = new StringBuffer("http://web.1xinxi.cn/asmx/smsservice.aspx?");

				// 向StringBuffer追加用户名
				sb.append("name=chenminmin008@126.com");

				// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
				sb.append("&pwd=B57C526FC74B32DEA9A9FBE7ED7A");

				// 向StringBuffer追加手机号码
				sb.append("&mobile=18516293301");

				// 向StringBuffer追加消息内容转URL标准码
				sb.append("&content="+URLEncoder.encode(content));
				
				//追加发送时间，可为空，为空为及时发送
				sb.append("&stime=");
				
				//加签名
				sb.append("&sign="+URLEncoder.encode(sign));
				
				//type为固定值pt  extno为扩展码，必须为数字 可为空
				sb.append("&type=pt&extno=");
				// 创建url对象
				URL url;
				try {
					url = new URL(sb.toString());
					// 打开url连接
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();

					// 设置url请求方式 ‘get’ 或者 ‘post’
					connection.setRequestMethod("POST");

					// 发送
					BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

					// 返回发送结果
					String inputline = in.readLine();
					System.out.println(inputline);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				

				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				

	}
}
