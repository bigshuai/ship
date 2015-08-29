/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.City;
import org.ship.shipservice.entity.OilStation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author lyhc
 *
 */
public interface OilDao extends CrudRepository<OilStation, Long> {
	@Modifying
	@Query("select o.name,o.desc,o.address,o.credit,o.quality,o.service, count(*) from OilStation o,Appraise a where o.cityId=?1 and o.status=1 and o.id=a.osId and a.status=1 group by o.name,o.desc,o.address,o.credit,o.quality,o.service")
	List<Object[]> findByCityId(Integer cityId);
	
	@Modifying
	@Query("select o.name,o.desc,o.address,o.credit,o.quality,o.service, count(*) from OilStation o,Appraise a where o.status=1 and o.id=a.osId and a.status=1 group by o.name,o.desc,o.address,o.credit,o.quality,o.service")
	List<OilStation> findOsAll();
	
	@Modifying
	@Query("select o from OilStation o  where o.cityId=?1 and o.status=1")
	OilStation queryDetailById(Integer id);
	
	@Modifying
	@Query("select o from City o  where o.status=1")
	public List<City> queryCityList();
}
