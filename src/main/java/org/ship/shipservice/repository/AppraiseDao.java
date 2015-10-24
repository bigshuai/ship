/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.Appraise;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AppraiseDao extends CrudRepository<Appraise, Long> {

	@Modifying
	@Query(value="SELECT s.os_id, u.id,u.user_name,s.service,s.quality,s.credit,s.content,s.create_time,u.url,s.total_appraise "
			+ "FROM t_appraise s,t_user u where s.user_id=u.id and s.status=1 and s.os_id=?1 "
			+ "order by s.create_time desc limit ?2,?3", nativeQuery=true)

	List<Object[]> queryAppraiseList(Integer osId, int start, Integer pageSize);

	@Query(value="SELECT COUNT(1) from t_appraise WHERE status=1 and os_id=?1", nativeQuery=true)
	int queryAppraiseTotalList(Integer osId);
}
