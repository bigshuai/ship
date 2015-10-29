package org.ship.shipservice.service.bank;

import java.util.List;

import org.ship.shipservice.domain.BankBean;
import org.ship.shipservice.domain.BankInfo;
import org.ship.shipservice.domain.ResultList;

public interface BankService {
	public BankInfo getBankInfo(String bankCardNo);
	public int getBankInfo(long userId,String bankCardNo);
	public ResultList getUserBankList(Long userId); 
	
	public String precheckForSign(BankBean bank) throws Exception;
	
	public String sign(String userId, String requestNo, String code) throws Exception;

	public List<BankInfo> getInstList(String bankCardType, String bankCode);

	public String unsign(Long userId, Long bankId) throws Exception;
}		
