package org.ship.shipservice.repository;

import org.ship.shipservice.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteDao extends CrudRepository<Favorite, Long>{
Page<Favorite> findByUserIdAndType(Integer userId,Integer type,Pageable pageable);
}
