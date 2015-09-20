package org.ship.shipservice.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.BankBean;
import org.ship.shipservice.domain.BankInfo;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.service.bank.BankService;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.HybException;
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
	@RequestMapping(value="/instList", method = RequestMethod.GET)
	public String getInstList() {
		logger.debug("getBankInfo start.bcn=");
		String bankCardType = request.getParameter("bankCardType");
		String bankCode = request.getParameter("bankCode");
		List<BankInfo> infos = bankService.getInstList(bankCardType, bankCode);
		return CommonUtils.printListStr(infos);
	}
	
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
	public String getUserBankList(@RequestParam("userId") Long userId) {
		logger.debug("getUserBankList start。");
		ResultList rl = bankService.getUserBankList(userId);
		return CommonUtils.printListStr(rl);
	}
	
	/**
	 * 绑定银行卡 预校验
	 * @param couponId
	 * @return
	 */
	@RequestMapping(value="/pfs", method = RequestMethod.POST)
	public String precheckForSign(@RequestBody String body) {
		logger.debug("getCoupon start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		String requestNo = CommonUtils.getPayRequestNo(jo.getString(HybConstants.USERID));
		BankBean bank = new BankBean();
		bank.setUserId(jo.getLong(HybConstants.USERID));
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
			try {
				String res = bankService.precheckForSign(bank);
				if(StringUtils.isEmpty(res)){
					Map<String, String> map = new HashMap<String, String>();
					map.put("requestNo", requestNo);
					return CommonUtils.printObjStr(map);
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res);
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
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
		String userId = jo.getString(HybConstants.USERID);
		String requestNo = jo.getString("requestNo");
		String code = jo.getString("code");
		String r = this.checkSign(userId, requestNo, code);
		if(StringUtils.isEmpty(r)){
			try {
				String res = bankService.sign(userId, requestNo, code);
				if(StringUtils.isEmpty(res)){
					return CommonUtils.printStr();
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res);
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
			}
			
		}else{
			return CommonUtils.printStr(ErrorConstants.PARAM_ERRO, "参数异常");
		}
	}
	
	/**
	 * 解约银行卡
	 * @param couponId
	 * @return
	 */
	@RequestMapping(value="/unsign", method = RequestMethod.POST)
	public String unsign(@RequestBody String body) {
		logger.debug("getCoupon start.body=" + body);
		JSONObject jo = RequestUtil.convertBodyToJsonObj(body);
		Long userId = jo.getLong(HybConstants.USERID);
		Long bankId = jo.getLong("bankId");
		String r = "";
		if(StringUtils.isEmpty(r)){
			try {
				String res = bankService.unsign(userId, bankId);
				if(StringUtils.isEmpty(res)){
					return CommonUtils.printStr();
				}else{
					return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, res);
				}
			} catch (Exception e) {
				return CommonUtils.printStr(ErrorConstants.PRECHECK_FOR_SIGN_ERROR, e.getMessage());
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
