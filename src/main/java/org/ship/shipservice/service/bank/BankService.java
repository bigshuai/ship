package org.ship.shipservice.service.bank;

import org.ship.shipservice.domain.BankBean;
import org.ship.shipservice.domain.BankInfo;
import org.ship.shipservice.domain.ResultList;

public interface BankService {
	public BankInfo getBankInfo(String bankCardNo);
	
	public ResultList getUserBankList(Long userId); 
	
	public String precheckForSign(BankBean bank);
	
	public String sign(String userId, String requestNo, String code);
}		
