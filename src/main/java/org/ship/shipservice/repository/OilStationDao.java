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
	@Query(value = "select t.id,t.name,t.credit,t.coupon_flag,t.num,t.status, "
			+ "case when t.derate_flag=1 then GROUP_CONCAT(d.info) ELSE '' END derate from "
			+ "("
			+ "select o.id,o.name,o.credit,o.coupon_flag,o.derate_flag,o.status, count(*) num "
			+ "from t_oil_station o,t_appraise a "
			+ "where o.city_id=?1 and o.status=1 and o.id=a.os_id and a.status=1 "
			+ "group by o.id,o.name,o.credit,o.coupon_flag,o.derate_flag,o.status"
			+ ") t "
			+ "LEFT JOIN "
			+ "t_derate d on (t.id=d.os_id or d.os_id=0) group by t.id,t.name,t.credit,"
			+ "t.coupon_flag,t.derate_flag,t.num,t.status", nativeQuery = true)
	List<Object[]> findByCityId(Integer cityId);

	@Modifying
	@Query("select o.name,o.desc,o.address,o.credit,o.quality,o.service, count(*) from OilStation o,Appraise a where o.status=1 and o.id=a.osId and a.status=1 group by o.name,o.desc,o.address,o.credit,o.quality,o.service")
	List<OilStation> findOsAll();

	@Modifying
	@Query(value = "select t.id,t.name,t.desc,t.address,t.phone,t.credit,t.quality,t.service,t.coupon_flag,t.num,t.status, "
			+ "case when t.derate_flag=1 then GROUP_CONCAT(d.info) ELSE '' END derate from "
			+ "("
			+ "select o.id,o.name,o.desc,o.address,o.phone,o.credit,o.quality,o.service,o.coupon_flag,o.derate_flag,o.status, count(*) num "
			+ "from t_oil_station o,t_appraise a "
			+ "where o.id=?1 and o.status=1 and o.id=a.os_id and a.status=1 "
			+ "group by o.id,o.name,o.desc,o.address,o.phone,o.credit,o.quality,o.service,o.coupon_flag,o.derate_flag,o.status"
			+ ") t "
			+ "LEFT JOIN "
			+ "t_derate d on (t.id=d.os_id or d.os_id=0) group by t.id,t.name,t.desc,t.address,t.phone,t.credit,t.quality,t.service,"
			+ "t.coupon_flag,t.derate_flag,t.num,t.status", nativeQuery = true)
	List<Object[]> queryDetailById(Long id);

	@Modifying
	@Query("select o from City o  where o.status=1")
	public List<City> queryCityList();
	
	@Modifying
	@Query("select o from Oil o where o.osId=?1")
	public List<Oil> queryOils(Integer osId);
}
