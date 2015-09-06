package org.ship.shipservice.repository;

import org.ship.shipservice.entity.InfoType;
import org.springframework.data.repository.CrudRepository;

public interface InfoTypeDao extends CrudRepository<InfoType, Long> {
	InfoType findById(long id);
}
