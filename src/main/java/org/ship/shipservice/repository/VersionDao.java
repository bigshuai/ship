package org.ship.shipservice.repository;

import org.ship.shipservice.entity.Version;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface VersionDao extends CrudRepository<Version, Long>{
Page<Version> findByType(Integer type,Pageable pageable);
}
