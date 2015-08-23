package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.ConsumeInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConsumeInfoDao extends CrudRepository<ConsumeInfo,Long>{
	@Modifying
	@Query("select c from ConsumeInfo c where c.accountId=?1")
	List<ConsumeInfo> findByAccountId(Integer accountId);
}
