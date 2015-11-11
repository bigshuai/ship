package org.ship.shipservice.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import com.sdo.common.lang.StringUtil;
import com.sj.pay.client.MasResponse;
import com.sj.pay.constants.PayConstants;
import com.sj.pay.sign.MD5;
import com.sj.pay.sign.RSA;
import com.sj.pay.sign.SNKRSA;

public class BaseClient implements PayConstants
/*     */{
	/*     */public BaseClient() {
	}

	/*     */
	/*     */protected Map<String, String> getPublicParameters()
	/*     */{
		/* 33 */Map<String, String> params = new HashMap();
		/* 34 */params.put("charset", "UTF-8");
		/* 35 */params.put("requestTime",
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		/* 36 */return params;
		/*     */}

	/*     */
	/*     */protected  MasResponse sendMsg(String uri, Map<String, String> params,
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
		/* 75 */return response;
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */protected String sign(String signType, String data)
	/*     */{
		/* 85 */if (signType == "SNKRSA")
			/* 86 */return SNKRSA
					.sign(data,
							"BwIAAAAkAABSU0EyAAQAAAEAAQCrTJTlKZuEV4WLWoI2lIwdJxexLYZJSesm4lmIvRybVeoBvIDT5Ytm9zupmELzSWgAQR6AjZvy3LbTzW538CiJNCKRF3lMkqexHpzlz52bZymEFmVfT0bdPMinWfkBC6S294NKfGHCPOdZpaYfO8w3T++CPb4WxEQY/VVbivf2uoe1Rr/GOfh8I/ktvOb1ebt84UFQ4JO67qff9YIhDaanhTXkB2mZ5VnZiqMKXqaRzUEhPGQuE1QgcfabU6BpCfe9eK9+8bEougzU8J9BuO139sMGh5F6as6p2SA8BNuf94OHe8qJGDWkNiwacLKJ/jZz3ugZKE3Bd7WzuNwRlL/Bac9z73mH4x7KwYkLPEI4aI/AcfQOBd1J2o3zAIckKS2Hjz9tCK8j0cOS2mpuacx9/C4f/uRJ2wV6mQ/lJq5VND3H8mNqdFBf8QIyb15KfUzmr5lXokwAQFd63Mri3ZyVIDQypJolyx1PBfMm4ML6zl1kxl3jpENcWBl7tLHjTKhueLrb28M4XMGRC4qQlCSNFA5M4Q5T7AAAlL4Iv8sPIKE/nCyor3icm6L9uoUt8K4UQ8qRkgSN664zcm/cowmHaRXLixpNNfCHY5oJwqSdRWgKkYceFyYwYoagIvUZ9z3dCIiJfUecZOYrStiyQxxFctJG3sCosYRlBISKzRmYkgL0jUiBie72L4luYXd6bmSVl3NBgTTi7kmNvVmsZa9hM1nRRRpvMYX5pcL2enp4hRzPdUOgy1o4NUegDr73vnA=",
							"utf-8");
		/* 87 */if (signType == "RSA") {
			/* 88 */return RSA
					.sign(data,
							"MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDYXZmMYfdpEQVgFUCg8lj70EFmXrDbHNRxCBVjxdvtTUZzY33ckiuP31axZarqvuaPZOCvpjlNc0ZNamxOVBzF/qlhlNmavcvM8ZWs7xyjmm4i1apCMscWrLdEkSH3nPNBZuv2duJWkt3l/KrQvME6vfuSTFqGpNFWvWIpORkaAkKpZ2XdFJg9cQdck7CvXmsOf5UpNP/VjpBT8vZfE5PLLuI6CsUv/qywV2S+3S5NXRH3pzhq8xCZIL/lWrG06hXQ4CqUup6/xdhtMralM8IRUgjDtKBb+963QpYCuFtb5d2kaWGs6tD3gGXaryhComCFav3OgwkMoCvJUdJcPUTXAgMBAAECggEBAIe1XaPxpo8d//MeIWVR3IJFQ2AEMIWedZlX6qKj7afP+kpRsxXwEKay/NtT23pVtqNmMI+7gOGVVhkkkT4n/8woSPFNvZcTSIsJVEodyCbNrBrpTVssIjeUqXa2WUnIBcOV3JbARoLqp4ejjThTzBceJnbCsV0Wb78qFjGpAJeQf5kFhkx+BK88e5Z06C3JRbQHULmz5oLVRU48Zgcn1Jhp3ZdfKTzO9eIThq/wszcLTzRCVocx1IF66Q4UCmLE2owyR1LhZ4ZA6YioMCZPbMjAeUYPSucT8PrsNPhQZOtoF1bW5nCeAYuQuUyIQBbRNX9bbMDYj9OP7OFabuTJB2ECgYEA+jkBRITPXl6JG/9Pauz6sXFzdu6gm97y9RYHKBW9C7xc0GxIQM4OlVAkUJzIv6pOT5G+kl4T/e02VmX0ffwRp4u4Tna5n4G4Gocsqa4DcECdnvhMbqKlww/5VCv1Fvrt6Q/Rq9dmannwbiA0jcHBknHnCAaS70UDe67xj0Wqwr8CgYEA3Vx5tvYeSFLo/3Y5gcQf0f5Ruk27gx9mu0SvtLSYFjyxExqYAJo8ZwkNJYZDmCcu9J8rh8LboxYKgcFIVKMdefWb7JOHyf8wAiudZMZECt32TaEN71iDgiH9yhM2uQ05J+RINe4zj/7Gbj+FH+ijP1gNi0JOVeoj+lfJUtMQO+kCgYEAg+nFh0/U2tVPxxjDz4T7bMx4qLyIo2PYBekFANblAOjerWpIdRGskn7bhjwBgTnRaxVUuGksdPO3b7j0Oe7Hh+Ka2ZKxrSt/2Uxl+VYpreYCsqoH8VOBu+IR+ZPq86B6CCI00TkPXxbF7+i+i/UXjZLKz2pX0Bg8C9pgsr1xlpUCgYB02LWe8He3sZwwDRX5+67YSCiX8SRD6LVvsKgW+SU2x76o2ObXmpK7yLlZz2+qxzQwCD0QIrmRcrcFGyO1GY0brZwq2w1YgQ20d5VTdpzAJ7416AfVCaIRdSPkIRRHxkUfW48KeLxbDB9uXrVEzKYvb6lmkw+KpldrdB9fSu5M0QKBgQCTFBEbqMimjsf36fdEbgc4MCoqIpmnA/DCJeFiJgox1FRT4wMt9diAhLLwpdzGBfyyuZJ3eSE32eeUSE0PZyrZY9sCMCMhGNl3DtyDVmhUNrsV1+VaGBQjVObKzY3LcVy4LraOB1XVV8sZs9g2Fz6IVosIwnyla/rdBWcagaRWEg==",
							"utf-8");
			/*     */}
		/* 90 */return MD5.sign(data, "abcdefg", "utf-8");
		/*     */}

	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */
	/*     */protected boolean verify(String signMsg, String signType,
			String responseBody)
	/*     */{
		/* 101 */if ("test".equalsIgnoreCase("prod")) {
			/* 102 */if ("RSA".equalsIgnoreCase(signType))
				/* 103 */return RSA
						.verify(responseBody,
								signMsg,
								"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnyHboN3r1OJP8PSBwXXpP0bbKQ60VWiJ1FzBEJ7Ek2kJXvQdTpgwgupcj2ihMW8/DCJjh7Pe/Nay6qtR9bAV1A/Wc5/WktpQvwxMeLLR5Se0a2KiShTT8kNgXg/dr7SHdnjTOHnSjpBgRUXytkcqMJstivARAWJtmzWp+sNJmj0NCjstgn48nhPrd+0yKQ+LaKpLiexpBOqmc9OYM66HnUFcczO4+NyVInTUUZ2vgbD389/H+FU03jOyAxgEqezATQrgltnoxMf3feyQF1Baq1x31oGk5skRP/WOLVmdaPsyAIU+PpyJcCO6cZzPYvzclNQq2x4W1B9FdbRz9p52WwIDAQAB",
								"UTF-8");
			/* 104 */if (StringUtil.equalsIgnoreCase("SNKRSA", signType)) {
				/* 105 */return SNKRSA
						.verify(responseBody,
								signMsg,
								"ACQAAASAAACUAAAABgIAAAAkAABSU0ExAAQAAAEAAQCvPKuUnRKvI8ed92Q/xoqhfFtCEDzd8wQt0M775b6egKsgRGOKEqzYI3LhNQKLnhVxOQ/0Y2V85ez2eUp1fP5WMMj8oWdqGBJDbXEQUK0jKvdDBRlWv0RB/XMSvbBBSHDZTrPckvjWn8OU6C+5uHVFSoxSNFEXfNw4JHZ93wXQpw==",
								"UTF-8");
				/*     */}
			/* 107 */System.out.println("unknow signType" + signMsg);
			/*     */}
		/*     */else {
			/* 110 */if (("RSA".equalsIgnoreCase(signType))
					|| ("SNKRSA".equalsIgnoreCase(signType))) {
				/* 111 */return RSA
						.verify(responseBody,
								signMsg,
								"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC69veKW1X9GETEFr49gu9PN8w7H6alWec8wmF8SoP3tqQLAflZp8g83UZPX2UWhClnm53P5ZwesaeSTHkXkSI0iSjwd27N07bc8puNgB5BAGhJ80KYqTv3Zovl04C8AepVmxy9iFniJutJSYYtsRcnHYyUNoJai4VXhJsp5ZRMqwIDAQAB",
								"utf-8");
				/*     */}
			/* 113 */System.out.println("unknow signType" + signMsg);
			/*     */}
		/*     */
		/* 116 */return false;
		/*     */}
	/*     */
}
