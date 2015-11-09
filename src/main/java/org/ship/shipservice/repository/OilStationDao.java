/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.Oil;
import org.ship.shipservice.entity.OilStation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author lyhc
 * 
 */
public interface OilStationDao extends CrudRepository<OilStation, Long> {
	@Modifying
	@Query(value = "select o.id,o.name,o.credit,"
			+ "o.coupon_flag,"
			+ "o.status,o.pic_url,o.latitude,o.longitude"
			+ " from t_oil_station o where o.city_id=?1 and o.`status`=1 order by id desc limit ?2,?3", nativeQuery = true)
	List<Object[]> findByCityId(Integer cityId, Integer start, Integer end);
	
	@Modifying
	@Query(value = "select o.id,o.name,o.credit,"
			+ "o.coupon_flag,"
			+ "o.status,o.pic_url,o.latitude,o.longitude"
			+ " from t_oil_station o where o.name like %?1% and o.`status`=1 order by id desc limit ?2,?3", nativeQuery = true)
	List<Object[]> findByOilName(String oilname, Integer start, Integer end);
	@Query(value = "SELECT count(1) FROM t_appraise a where a.status=1 and a.os_id=?1", nativeQuery = true)
	int findAppraise(Integer oid);
	@Query(value = "SELECT info FROM t_derate d where d.os_id=?1 and d.`status`=1", nativeQuery = true)
	String findInfo(Integer oid);
	@Query(value = "select count(1) from t_oil_station o where o.city_id=?1 and o.`status`=1", nativeQuery = true)
	int findCountByCityId(Integer cityId);
	
	@Query(value = "select count(1) from t_oil_station o where o.name like %?1% and o.`status`=1", nativeQuery = true)
	int findCountByOilName(String oilname);

	@Modifying
	@Query("select o.name,o.desc,o.address,o.credit,o.quality,o.service, count(*) from OilStation o,Appraise a where o.status=1 and o.id=a.osId and a.status=1 group by o.name,o.desc,o.address,o.credit,o.quality,o.service")
	List<OilStation> findOsAll();
	
	@Modifying
	@Query(value = "select o.id,o.name,o.desc,o.address,o.phone,o.credit,o.quality,o.service,o.coupon_flag,"
			+ "o.derate_flag,o.status,o.pic_url,o.latitude,o.longitude,"
			+ "(SELECT count(1) FROM t_appraise a where a.status=1 and a.os_id=?1) num,"
			+ "(SELECT info FROM t_derate d where d.os_id=?1  and d.`status`=1) derate"
			+ " from t_oil_station o where o.id=?1 and o.`status`=1", nativeQuery = true)
	List<Object[]> queryDetailById(Long id);
	
	@Modifying
	@Query(value="select sum(a.credit)/count(1) credit,sum(a.service)/count(1) service, sum(a.quality)/count(1) quality "
			+ "from t_appraise a where a.`status`=1 and a.os_id=?1 ", nativeQuery=true)
	List<Object[]> quertCreditForOsId(Long id);

	@Modifying
	@Query("select o from City o  where o.status=1")
	public List<City> queryCityList();
	
	@Modifying
	@Query("select o from Oil o where (o.osId=?1 or o.osId=0) and status=1")
	public List<Oil> queryOils(Integer osId);
	
	@Modifying
	@Query("select o from Oil o where o.id=?1")
	public List<Oil> queryOil(Long id);
}
