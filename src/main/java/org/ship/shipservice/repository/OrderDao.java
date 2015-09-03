package org.ship.shipservice.repository;

import java.util.List;

import org.ship.shipservice.entity.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderDao extends CrudRepository<Order, Long>{
	@Modifying
	@Query("select o from Order o where o.userId=?1 and o.status=?2 order by o.createTime desc")
	List<Order> findByUserId(Integer userId,Integer status);
}
