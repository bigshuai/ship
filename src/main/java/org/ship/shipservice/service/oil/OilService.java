package org.ship.shipservice.service.oil;

import java.util.List;

import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.OilStation;


public interface OilService {
	public List<OilStation> queryOilList(Integer cityId);
	
	public List<City> queryCityList();
}
