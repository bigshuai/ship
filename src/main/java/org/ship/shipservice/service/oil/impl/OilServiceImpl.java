package org.ship.shipservice.service.oil.impl;

import java.util.ArrayList;
import java.util.List;

import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.OilStation;
import org.ship.shipservice.repository.OilDao;
import org.ship.shipservice.service.oil.OilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OilServiceImpl implements OilService {
	private OilDao oilDao;
	
	public List<OilStation> queryOilList(Integer cityId){
		List<Object[]> list = oilDao.findByCityId(cityId);
		List<OilStation> result = new ArrayList<OilStation>();
		for(Object[] o : list){
			OilStation os = new OilStation();
			//o.name,o.desc,o.address,o.credit,o.quality,o.service, count(*) 
			os.setName(o[0]+"");
			os.setDesc(o[1]+"");
			os.setAddress(o[2]+"");
			os.setCredit(Float.valueOf(o[3]+""));
			os.setQuality(Float.valueOf(o[4]+""));
			os.setService(Float.valueOf(o[5]+""));
			os.setAppraiseNum(Integer.valueOf(o[6]+""));
			result.add(os);
		}
		return result;
	}
	
	public List<City> queryCityList(){
		return oilDao.queryCityList();
	}

	public OilDao getOilDao() {
		return oilDao;
	}

	@Autowired
	public void setOilDao(OilDao oilDao) {
		this.oilDao = oilDao;
	}
}
