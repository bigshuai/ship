package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.Coupon;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author lyhc
 * 
 */
public interface BankDao extends CrudRepository<Coupon, Long> {

	@Modifying
	@Query(value="select t.id, t.bank_code,t.bank_name, t.bank_cardtype, t.bank_cardno "
			+ "from t_bank t "
			+ "where t.user_id=?1 and t.`status`=1 ORDER BY t.create_time desc", nativeQuery=true)
	public List<Object[]> queryUserBankList(Long userId);
	
	@Modifying
	@Query(value="select t.id, t.bank_code,t.bank_name, t.bank_cardtype, t.bank_cardno "
			+ "from t_bank_agreement t "
			+ "where t.user_id=?1 and t.`status`=1 ORDER BY t.create_time desc", nativeQuery=true)
	public List<Object[]> queryUserBankSList(Long userId);
	
	@Modifying
	@Query(value="insert into t_bank(user_id,request_no,bank_code,bank_name,bank_cardtype,bank_cardno,real_name,"
			+ "id_no,id_type,mobile_no,cvv2,valid_thru,status,create_time) "
			+ "VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10,?11,?12,0,now())", nativeQuery=true)
	public int saveBankInfo(Long userId,String requestNo,String bankCode,String bankName,String bankCardType,
			String bankCardNo,String realName,String idNo,String idType,String mobileNo,String cvv2,String validThru);
	
	@Modifying
	@Query(value="update t_bank set status=1,agreement_no=?1 where request_no=?2)", nativeQuery=true)
	public int updateSign(String agreementNo, String requestNo);
}
