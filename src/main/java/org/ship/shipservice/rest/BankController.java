package org.ship.shipservice.rest;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.BankBean;
import org.ship.shipservice.domain.BankInfo;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.domain.UserBean;
import org.ship.shipservice.service.bank.BankService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping(value="/api/v1/bank")
public class BankController implements HybConstants{
	private static Logger logger = LoggerFactory.getLogger(BankController.class);
	@Autowired
	private  HttpServletRequest request;
	
	@Autowired
    private BankService bankService;
	
	/**
	 * 根据银行卡查询信息
	 * @param phone
	 * @return
	 */
	@RequestMapping(value="/info", method = RequestMethod.GET)
	public String getBankInfo(@RequestParam("bcn") String bcn) {
		logger.debug("getBankInfo start.bcn="+bcn);
		BankInfo info = bankService.getBankInfo(bcn);
		if(info != null){
			return CommonUtils.printListStr(info);
		}else{
			return CommonUtils.printStr(ErrorConstants.BANK_GET_INFO_ERROR);
		}
	}
	
	/**
	 * 获取用户绑定银行卡列表
	 * @return
	 */
	@RequestMapping(value="/list", method = RequestMethod.GET)
	public String getUserBankList() {
		logger.debug("getUserBankList start。");
		UserBean ub = (UserBean)request.getSession().getAttribute(SESSION_USER);
		Long userId = 1L;
		ResultList rl = bankService.getUserBankList(userId);
		return CommonUtils.printListStr(rl);
	}
	
	/**
	 * 绑定银行卡
	 * @param couponId
	 * @return
	 */
	@RequestMapping(value="/pfs", method = RequestMethod.POST)
	public String precheckForSign(@RequestBody String body) {
		logger.debug("getCoupon start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		String requestNo = CommonUtils.getPayRequestNo(jo.getString("uid"));
		BankBean bank = new BankBean();
		bank.setUserId(jo.getLong("uid"));
		bank.setRequestNo(requestNo);
		bank.setBankCode(jo.getString("bankCode"));
		bank.setBankName(CommonUtils.decode(jo.getString("bankName")));
		bank.setBankCardType(jo.getString("bankCardType"));
		bank.setBankCardNo(jo.getString("bankCardNo"));
		bank.setRealName(CommonUtils.decode(jo.getString("realName")));
		bank.setIdNo(jo.getString("idNo"));
		bank.setIdType(jo.getString("idType"));
		bank.setMobileNo(jo.getString("mobileNo"));
		String r = this.checkPrecheckForSign(bank);
		if(StringUtils.isEmpty(r)){
			String res = bankService.precheckForSign(bank);
			if(StringUtils.isEmpty(res)){
				Map<String, String> map = new HashMap<String, String>();
				map.put("requestNo", requestNo);
				return CommonUtils.printObjStr(map);
			}else{
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res);
			}
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	}
	
	/**
	 * 绑定银行卡
	 * @param couponId
	 * @return
	 */
	@RequestMapping(value="/sign", method = RequestMethod.POST)
	public String sign(@RequestBody String body) {
		logger.debug("getCoupon start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		String userId = jo.getString("uid");
		String requestNo = jo.getString("requestNo");
		String code = jo.getString("code");
		String r = this.checkSign(userId, requestNo, code);
		if(StringUtils.isEmpty(r)){
			String res = bankService.sign(userId, requestNo, code);
			if(StringUtils.isEmpty(res)){
				return CommonUtils.printStr();
			}else{
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res);
			}
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	}
	
	private String checkPrecheckForSign(BankBean bank){
		return null;
	}
	
	private String checkSign(String userId, String requestNo, String code){
		return null;
	}

	public BankService getBankService() {
		return bankService;
	}

	public void setBankService(BankService bankService) {
		this.bankService = bankService;
	}
}