package org.ship.shipservice.repository;


import org.ship.shipservice.entity.ConsumeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ConsumeInfoDao extends CrudRepository<ConsumeInfo,Long>{
	Page<ConsumeInfo> findByUserIdOrderByIdDesc(Integer accountId,Pageable pageable);
}
