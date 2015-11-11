package org.ship.shipservice.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sj.pay.client.MasResponse;
import com.sj.pay.client.PayResponse;
import com.sj.pay.client.domain.Inst;
import com.sj.pay.client.domain.OrderRes;
import com.sj.pay.client.domain.PayConfirmInfo;
import com.sj.pay.client.domain.PayConfirmRes;
import com.sj.pay.client.domain.PayInfo;
import com.sj.pay.client.domain.PaymentOrder;
import com.sj.pay.client.domain.PrecheckSign;
import com.sj.pay.client.domain.SignRes;

public class OrderPayClient extends BaseClient {
	private static OrderPayClient instance;

	private OrderPayClient() {
	}

	public static OrderPayClient getInstance() {
		if (instance == null) {
			instance = new OrderPayClient();
		}
		return instance;
	}
public static void main(String[] args) throws IOException {
	Map<String, String> params = new HashMap();
	params.put("merchantNo", "508581");
	params.put("merchantOrderNo", "H42934812218");
	params.put("productName", "机油");
	params.put("productDesc", "机油");
	params.put("currency", "CNY");
	params.put("amount", "1");
	params.put("notifyUrl", "http://139.196.51.164:8080/shipService/api/v1/order/notify");
	params.put("userIP", "10.174.159.100");
	
	OrderPayClient.getInstance().sendMsg(
			"/api-acquire-channel/services/express/getInstList", params,
			"RSA");
}
public   MasResponse sendMsg(String uri, Map<String, String> params,
		String signType) throws IOException {
	/* 40 */HttpClient client = new HttpClient();
	/* 41 */HttpConnectionManagerParams httpParams = client
			.getHttpConnectionManager().getParams();
	/* 42 */httpParams.setParameter("http.protocol.content-charset",
			"UTF-8");
	/*     */
	/* 44 */PostMethod postMethod = new PostMethod(
			"https://mgw.shengpay.com/mas" + uri);
	/* 45 */postMethod.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded; charset=UTF-8");
	/* 46 */List<NameValuePair> pairs = new ArrayList();
	/* 47 */params.putAll(getPublicParameters());
	/* 48 */Iterator<String> iter = params.keySet().iterator();
	/* 49 */while (iter.hasNext()) {
		/* 50 */String key = (String) iter.next();
		/* 51 */pairs.add(new NameValuePair(key, (String) params.get(key)));
		/*     */}
	/* 53 */postMethod.addParameters((NameValuePair[]) pairs
			.toArray(new NameValuePair[1]));
	/*     */
	/* 55 */RequestEntity requestEntity = postMethod.getRequestEntity();
	/* 56 */ByteArrayOutputStream baos = new ByteArrayOutputStream();
	/* 57 */requestEntity.writeRequest(baos);
	/* 58 */String requestBody = baos.toString();
	System.out.println("请求参数" + requestBody);
	/* 59 */postMethod.addRequestHeader("signType", signType);
	/* 60 */postMethod.addRequestHeader("signMsg",
			sign(signType, requestBody));
	/* 61 */int httpCode = client.executeMethod(postMethod);
	/* 62 */String responseBody = postMethod.getResponseBodyAsString();
	/*     */
	/* 64 */Header signMsgHeader = postMethod.getResponseHeader("signMsg");
	/* 65 */Header signTypeHeader = postMethod
			.getResponseHeader("signType");
	/* 66 */boolean signResult = verify(signMsgHeader.getValue(),
			signTypeHeader.getValue(), responseBody);
	/*     */
	/*     */
	/* 69 */MasResponse response = new MasResponse();
	/* 70 */response.setHttpCode(httpCode);
	/* 71 */response.setSignResult(signResult);
	/* 72 */response.setSignMsg(signMsgHeader.getValue());
	/* 73 */response.setSignType(signTypeHeader.getValue());
	/* 74 */response.setResponseBody(responseBody);
	System.out.println(responseBody);
	/* 75 */return response;
	/*     */}

	public PayResponse<Inst> getInstList(String merchantNo,
			String bankCardType, String bankCode) throws IOException {
		Map<String, String> params = new HashMap();
		params.put("merchantNo", merchantNo);
		params.put("bankCardType", bankCardType);
		params.put("bankCode", "");
		MasResponse response = sendMsg(
				"/api-acquire-channel/services/express/getInstList", params,
				"RSA");
		PayResponse<Inst> result = new PayResponse();
		result.setHttpCode(response.getHttpCode());
		result.setSignResult(response.isSignResult());
		result.setSignMsg(response.getSignMsg());
		result.setSignType(response.getSignType());
		System.out.println(response.getResponseBody());
		if ((response.getHttpCode() == 200) && (response.isSignResult())) {
			JSONObject obj = JSON.parseObject(response.getResponseBody());
			String returnCode = obj.getString("returnCode");
			result.setReturnCode(returnCode);
			result.setReturnMsg(obj.getString("returnMessage"));
			if ("SUCCESS".equalsIgnoreCase(returnCode)) {
				List<Inst> list = JSON.parseArray(obj.getString("bankList"),
						Inst.class);
				result.setList(list);
			}
		}
		return result;
	}

	public PayResponse<String> precheckForSign(PrecheckSign sign)
			throws IOException {
		Map<String, String> params = new HashMap();
		params.put("merchantNo", sign.getMerchantNo());
		params.put("requestNo", sign.getRequestNo());
		// params.put("isResendValidateCode", sign.isResendValidateCode());
		params.put("outMemberId", sign.getOutMemberId());
		params.put("bankCode", sign.getBankCode());
		params.put("bankCardType", sign.getBankCardType());
		params.put("bankCardNo", sign.getBankCardNo());
		params.put("realName", sign.getRealName());
		params.put("idNo", sign.getIdNo());
		params.put("idType", sign.getIdType());
		params.put("mobileNo", sign.getMobileNo());
		params.put("cvv2", sign.getCvv2());
		params.put("validThru", sign.getValidThru());
		params.put("userIp", sign.getUserIp());
		MasResponse response = sendMsg(
				"/api-acquire-channel/services/express/precheckForSign",
				params, "RSA");

		PayResponse<String> result = new PayResponse();
		result.setHttpCode(response.getHttpCode());
		result.setSignResult(response.isSignResult());
		result.setSignMsg(response.getSignMsg());
		result.setSignType(response.getSignType());
		System.out.println(response.getResponseBody());
		if ((response.getHttpCode() == 200) && (response.isSignResult())) {
			JSONObject obj = JSON.parseObject(response.getResponseBody());
			String returnCode = obj.getString("returnCode");
			result.setReturnCode(returnCode);
			result.setReturnMsg(obj.getString("returnMessage"));
			if ("SUCCESS".equalsIgnoreCase(returnCode)) {
				result.setObj(response.getResponseBody());
			}
		}
		return result;
	}

	public PayResponse<SignRes> checkSign(String merchantNo, String requestNo,
			String validateCode) throws IOException {
		Map<String, String> params = new HashMap();
		params.put("merchantNo", merchantNo);
		params.put("requestNo", requestNo);
		params.put("validateCode", validateCode);
		MasResponse response = sendMsg(
				"/api-acquire-channel/services/express/sign", params, "RSA");

		PayResponse<SignRes> result = new PayResponse();
		result.setHttpCode(response.getHttpCode());
		result.setSignResult(response.isSignResult());
		result.setSignMsg(response.getSignMsg());
		result.setSignType(response.getSignType());
		if ((response.getHttpCode() == 200) && (response.isSignResult())) {
			JSONObject obj = JSON.parseObject(response.getResponseBody());
			String returnCode = obj.getString("returnCode");
			result.setReturnCode(returnCode);
			result.setReturnMsg(obj.getString("returnMessage"));
			if ("SUCCESS".equalsIgnoreCase(returnCode)) {
				result.setObj(new SignRes(obj.getString("agreementNo")));
			}
		}
		return result;
	}

	public PayResponse<OrderRes> createPaymentOrder(PaymentOrder order)
			throws IOException {
		Map<String, String> params = new HashMap();
		params.put("merchantNo", order.getMerchantNo());
		params.put("merchantOrderNo", order.getMerchantOrderNo());
		params.put("productName", order.getProductName());
		params.put("productDesc", order.getProductDesc());
		params.put("currency", order.getCurrency());
		params.put("amount", order.getAmount());
		params.put("notifyUrl", order.getNotifyUrl());
		params.put("userIP", order.getUserIP());
		System.out.println("创建订单参数" + params.toString());
		MasResponse response = sendMsg(
				"/api-acquire-channel/services/express/createPaymentOrder",
				params, "RSA");

		PayResponse<OrderRes> result = new PayResponse();
		result.setHttpCode(response.getHttpCode());
		result.setSignResult(response.isSignResult());
		result.setSignMsg(response.getSignMsg());
		result.setSignType(response.getSignType());
		System.out.println(JSON.toJSON(response));
		if ((response.getHttpCode() == 200) && (response.isSignResult())) {
			JSONObject obj = JSON.parseObject(response.getResponseBody());
			String returnCode = obj.getString("returnCode");
			result.setReturnCode(returnCode);
			result.setReturnMsg(obj.getString("returnMessage"));
			if ("SUCCESS".equalsIgnoreCase(returnCode)) {
				result.setObj((OrderRes) JSON.parseObject(obj.toJSONString(),
						OrderRes.class));
			}
		}
		return result;
	}

	public PayResponse<String> precheckForPayment(PayInfo info)
			throws IOException {
		Map<String, String> params = new HashMap();
		params.put("merchantNo", info.getMerchantNo());
		params.put("sessionToken", info.getSessionToken());
		params.put("agreementNo", info.getAgreementNo());
		// params.put("isResendValidateCode", info.isResendValidateCode());
		params.put("outMemberId", info.getOutMemberId());
		params.put("bankCode", info.getBankCode());
		params.put("bankCardType", info.getBankCardType());
		params.put("bankCardNo", info.getBankCardNo());
		params.put("realName", info.getRealName());
		params.put("idNo", info.getIdNo());
		params.put("idType", info.getIdType());
		params.put("mobileNo", info.getMobileNo());
		params.put("cvv2", info.getCvv2());
		params.put("validThru", info.getValidThru());
		params.put("userIp", info.getUserIp());
		params.put("riskExtItems", info.getRiskExtItems());
		MasResponse response = sendMsg(
				"/api-acquire-channel/services/express/precheckForPayment",
				params, "RSA");

		PayResponse<String> result = new PayResponse();
		result.setHttpCode(response.getHttpCode());
		result.setSignResult(response.isSignResult());
		result.setSignMsg(response.getSignMsg());
		result.setSignType(response.getSignType());
		if ((response.getHttpCode() == 200) && (response.isSignResult())) {
			JSONObject obj = JSON.parseObject(response.getResponseBody());
			String returnCode = obj.getString("returnCode");
			result.setReturnCode(returnCode);
			result.setReturnMsg(obj.getString("returnMessage"));
			if ("SUCCESS".equalsIgnoreCase(returnCode)) {
				result.setObj(response.getResponseBody());
			}
		}
		return result;
	}

	public PayResponse<PayConfirmRes> payment(PayConfirmInfo info)
			throws IOException {
		Map<String, String> params = new HashMap();
		params.put("merchantNo", info.getMerchantNo());
		params.put("sessionToken", info.getSessionToken());
		params.put("validateCode", info.getValidateCode());
		// params.put("isSign", info.isSign());
		params.put("userIp", info.getUserIp());
		params.put("outMemberId", info.getOutMemberId());
		params.put("riskExtItems", info.getRiskExtItems());

		MasResponse response = sendMsg(
				"/api-acquire-channel/services/express/payment", params, "RSA");

		PayResponse<PayConfirmRes> result = new PayResponse();
		result.setHttpCode(response.getHttpCode());
		result.setSignResult(response.isSignResult());
		result.setSignMsg(response.getSignMsg());
		result.setSignType(response.getSignType());
		if ((response.getHttpCode() == 200) && (response.isSignResult())) {
			JSONObject obj = JSON.parseObject(response.getResponseBody());
			String returnCode = obj.getString("returnCode");
			result.setReturnCode(returnCode);
			result.setReturnMsg(obj.getString("returnMessage"));
			if ("SUCCESS".equalsIgnoreCase(returnCode)) {
				result.setObj((PayConfirmRes) JSON.parseObject(
						response.getResponseBody(), PayConfirmRes.class));
			}
		}
		return result;
	}

	public PayResponse<SignRes> unSign(String merchantNo, String agreementNo,
			String principalId) throws IOException {
		Map<String, String> params = new HashMap();
		params.put("merchantNo", merchantNo);
		params.put("agreementNo", agreementNo);
		params.put("principalId", principalId);

		MasResponse response = sendMsg(
				"/api-acquire-channel/services/express/unsign", params, "RSA");

		PayResponse<SignRes> result = new PayResponse();
		result.setHttpCode(response.getHttpCode());
		result.setSignResult(response.isSignResult());
		result.setSignMsg(response.getSignMsg());
		result.setSignType(response.getSignType());
		if ((response.getHttpCode() == 200) && (response.isSignResult())) {
			JSONObject obj = JSON.parseObject(response.getResponseBody());
			String returnCode = obj.getString("returnCode");
			result.setReturnCode(returnCode);
			result.setReturnMsg(obj.getString("returnMessage"));
			"SUCCESS".equalsIgnoreCase(returnCode);
		}

		return result;
	}

	public String getRequestNo(long userId) {
		return UUID.randomUUID().toString().substring(0, 30) + "-" + userId;
	}

}
