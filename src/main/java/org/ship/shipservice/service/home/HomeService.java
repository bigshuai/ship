package org.ship.shipservice.service.home;

import org.ship.shipservice.entity.AboutUs;
import org.ship.shipservice.entity.Version;
import org.ship.shipservice.repository.AboutUsDao;
import org.ship.shipservice.repository.VersionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公共查询
 * 
 * @author zhf
 *
 */
@Component
@Transactional
public class HomeService {

	private VersionDao versionDao;
	
	private AboutUsDao aboutUsDao;
	
	public Page<Version> findVersion(Integer type) {
		PageRequest page = new PageRequest(0, 1, new Sort("createTime"));
		return versionDao.findByType(type,page);
	}

	public AboutUs findAboutUs(){
		return aboutUsDao.findAll().iterator().next();
	}
	public VersionDao getVersionDao() {
		return versionDao;
	}

	@Autowired
	public void setVersionDao(VersionDao versionDao) {
		this.versionDao = versionDao;
	}

	public AboutUsDao getAboutUsDao() {
		return aboutUsDao;
	}
	@Autowired
	public void setAboutUsDao(AboutUsDao aboutUsDao) {
		this.aboutUsDao = aboutUsDao;
	}

}
