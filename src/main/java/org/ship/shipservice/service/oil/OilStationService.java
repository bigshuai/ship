package org.ship.shipservice.service.oil;

import java.util.List;

import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.OilStation;


public interface OilStationService {
	
	/**
	 * 
	 * @param cityId
	 * @return
	 */
	public List<OilStationBean> queryOilList(Integer cityId);
	
	/**
	 * 
	 * @param osId
	 * @return
	 */
	public OilStationBean queryDetail(Long osId);
	
	/**
	 * 
	 * @return
	 */
	public List<City> queryCityList();
}
