package org.ship.shipservice.service.oil;

import org.ship.shipservice.domain.OilStationBean;
import org.ship.shipservice.domain.ResultList;


public interface OilStationService {
	
	/**
	 * 
	 * @param cityId
	 * @return
	 */
	public ResultList queryOilList(Integer cityId, int page, int pageSize);
	
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
	public ResultList queryCityList();
}
