package org.ship.shipservice.service.appraise;

import org.ship.shipservice.entity.Appraise;
import org.ship.shipservice.repository.AppraiseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhf
 *
 */
@Component
@Transactional
public class AppraiseService {
private AppraiseDao appraiseDao;


public void saveOilAppraise(Appraise appraise){
	appraiseDao.save(appraise);
}
public AppraiseDao getAppraiseDao() {
	return appraiseDao;
}
@Autowired
public void setAppraiseDao(AppraiseDao appraiseDao) {
	this.appraiseDao = appraiseDao;
}

}
