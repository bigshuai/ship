package org.ship.shipservice.service.information;

import org.ship.shipservice.entity.InfoType;
import org.ship.shipservice.repository.InfoTypeDao;
import org.ship.shipservice.repository.InformationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhf
 *
 */
@Component
@Transactional
public class InformationService {
	private InfoTypeDao infoTypeDao;
	private InformationDao infoDao;
	public InfoType findInfoTypeById(long id){
		return infoTypeDao.findById(id);
	}
	public InfoTypeDao getInfoTypeDao() {
		return infoTypeDao;
	}
	@Autowired
	public void setInfoTypeDao(InfoTypeDao infoTypeDao) {
		this.infoTypeDao = infoTypeDao;
	}
	public InformationDao getInfoDao() {
		return infoDao;
	}
	@Autowired
	public void setInfoDao(InformationDao infoDao) {
		this.infoDao = infoDao;
	}
	
	
} 
