package org.ship.shipservice.service.account;


import org.ship.shipservice.entity.ConsumeInfo;
import org.ship.shipservice.repository.ConsumeInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@Transactional
public class ConsumeInfoService {
	private ConsumeInfoDao consumeInfoDao;

	public Page<ConsumeInfo> findByAccountId(Integer userId, PageRequest page) {
		return consumeInfoDao.findByUserId(userId,page);
	}

	public ConsumeInfoDao getConsumeInfoDao() {
		return consumeInfoDao;
	}

	@Autowired
	public void setConsumeInfoDao(ConsumeInfoDao consumeInfoDao) {
		this.consumeInfoDao = consumeInfoDao;
	}

}
