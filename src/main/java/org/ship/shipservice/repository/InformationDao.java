package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.Information;
import org.springframework.data.repository.CrudRepository;

public interface InformationDao extends CrudRepository<Information, Long> {
	List<Information> findInfoByParam(String param);
}
