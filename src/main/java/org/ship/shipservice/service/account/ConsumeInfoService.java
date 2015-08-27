package org.ship.shipservice.service.account;

import java.util.List;

import org.ship.shipservice.entity.ConsumeInfo;
import org.ship.shipservice.repository.ConsumeInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
@Transactional
public class ConsumeInfoService {
	private ConsumeInfoDao consumeInfoDao;

	public List<ConsumeInfo> findByAccountId(Integer userId, int pageNo) {
		return consumeInfoDao.findByUserId(userId);
	}

	public ConsumeInfoDao getConsumeInfoDao() {
		return consumeInfoDao;
	}

	@Autowired
	public void setConsumeInfoDao(ConsumeInfoDao consumeInfoDao) {
		this.consumeInfoDao = consumeInfoDao;
	}

}
