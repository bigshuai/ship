package org.ship.shipservice.service.account;

import org.ship.shipservice.entity.UserAdvice;
import org.ship.shipservice.repository.AdviceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class AdviceService {
	private AdviceDao adviceDao;
    
	public UserAdvice findByUserId(Integer userId){
		return adviceDao.findByUserId(userId);
	}
	
	public int updateAdvice(Integer userId,String advice,String email){
		return adviceDao.updateAdvice(userId,advice,email);
	}
	public AdviceDao getAdviceDao() {
		return adviceDao;
	}

	@Autowired
	public void setAdviceDao(AdviceDao adviceDao) {
		this.adviceDao = adviceDao;
	}

}
