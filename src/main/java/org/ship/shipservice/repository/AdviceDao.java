package org.ship.shipservice.repository;

import org.ship.shipservice.entity.UserAdvice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface  AdviceDao extends CrudRepository<UserAdvice, Long>{
	UserAdvice findByUserId(Integer userId);
	@Modifying
	@Query("update UserAdvice a set a.advice=?2 ,a.email=?3 where a.userId =?1 and a.isDeleted=0")
	int updateAdvice(Integer userId,String advice,String email);
}
