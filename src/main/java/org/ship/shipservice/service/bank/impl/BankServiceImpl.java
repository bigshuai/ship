package org.ship.shipservice.service.bank.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.domain.BankBean;
import org.ship.shipservice.domain.BankInfo;
import org.ship.shipservice.domain.ResultList;
import org.ship.shipservice.repository.BankDao;
import org.ship.shipservice.service.bank.BankService;
import org.ship.shipservice.utils.CardType;
import org.ship.shipservice.utils.CommonUtils;
import org.ship.shipservice.utils.rsa.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sj.pay.client.PayResponse;
import com.sj.pay.client.SjPayClient;
import com.sj.pay.client.domain.PrecheckSign;
import com.sj.pay.client.domain.SignRes;

@Component
@Transactional
public class BankServiceImpl implements BankService {
	@Autowired
	private BankDao bankDao;
	
	@Override
	public BankInfo getBankInfo(String bankCardNo) {
		BankInfo info = new BankInfo();
		info.setBankCode("COMM");
		info.setBankName("通银行交");
		info.setBankCardType("DR");
		info.setBankCardTypeName("储蓄卡");
		return info;
	}
	
	public ResultList getUserBankList(Long userId){
		ResultList rl = new ResultList();
		List<BankInfo> result = new ArrayList<BankInfo>();
		//t.id, t.bankCode,t.bankName, t.bankCardType
		List<Object[]> list = bankDao.queryUserBankList(userId);
		for(int i = 0 ; (list != null && i < list.size()); i++){
			Object[] o = list.get(i);
			BankInfo bean = new BankInfo();
			bean.setId(Long.valueOf(o[0] + ""));
			bean.setBankCode(o[1] + "");
			bean.setBankName((o[2] + ""));;
			bean.setBankCardType(o[3] + "");
			bean.setBankCardTypeName(CardType.valueOf((o[3] + "")).getName());
			String cadrNo = RSAUtil.decrypt(o[4] + "");
			bean.setCardNo(cadrNo.substring(cadrNo.length()-4));
			bean.setAgreementNo((o[5] + ""));
			result.add(bean);
		}
		rl.setDataList(result);
		return rl;
	}

	@Override
	public String precheckForSign(BankBean bank) {
		try {
			//调用签约预校验
			SjPayClient client = SjPayClient.getInstance();
			PrecheckSign sign = new PrecheckSign();
			sign.setMerchantNo(HybConstants.MERCHANTNO);
			sign.setRequestNo(bank.getRequestNo());
			sign.setOutMemberId(UUID.randomUUID().toString().substring(0, 30));
			sign.setBankCode(bank.getBankCode());
			sign.setBankCardType(bank.getBankCardType());
			sign.setBankCardNo(bank.getBankCardNo());
			sign.setRealName(bank.getRealName());
			sign.setIdNo(bank.getIdNo());
			sign.setIdType("IC");
			sign.setMobileNo(bank.getMobileNo());
			//sign.setCvv2(bank.getCvv2());
			//sign.setValidThru(bank.getValidThru());
			sign.setUserIp(CommonUtils.getIp());
			PayResponse<String> res = client.precheckForSign(sign);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode())){
					//签约成功，发送短信，返回到验证码页面
					//保存银行卡信息到t_bank
					//加密
					String bankCardNo = RSAUtil.encrypt(bank.getBankCardNo());
					String realName = RSAUtil.encrypt(bank.getRealName());
					String idNo = RSAUtil.encrypt(bank.getIdNo());
					String mobileNo = RSAUtil.encrypt(bank.getMobileNo());
					if(bankCardNo == null || realName==null||idNo==null||mobileNo==null){
						return "签约校验失败，请稍后再试。";
					}
					int r = bankDao.saveBankInfo(bank.getUserId(), bank.getRequestNo(), bank.getBankCode(), bank.getBankName(),
						bank.getBankCardType(),bankCardNo, realName, idNo, "IC",
						mobileNo, bank.getCvv2(), bank.getValidThru());
					if(r > 0){
						return null;
					}else{
						return "签约校验失败，请稍后再试。";
					}
				}else{
					return res.getReturnMsg();
				}
			}else{
				return "签约校验失败，请稍后再试。";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String sign(String userId, String requestNo, String code) {
		SjPayClient client = SjPayClient.getInstance();
		try {
			PayResponse<SignRes> res= client.checkSign(HybConstants.MERCHANTNO, requestNo, code);
			if(res.getHttpCode()==200 && res.isSignResult()){
				if(HybConstants.TEST || HybConstants.SUCCESS.equalsIgnoreCase(res.getReturnCode())){
					//签约成功，发送短信，返回到验证码页面
					//保存银行卡信息到t_bank
					String agreementNo = HybConstants.TEST?"22222":res.getObj().getAgreementNo();
					int r = bankDao.updateSign(agreementNo, requestNo);
					if(r > 0){
						return null;
					}else{
						return "签约失败，请稍后再试。";
					}
				}else{
					return res.getReturnMsg();
				}
			}else{
				return "签约失败，请稍后再试。";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
