package org.ship.shipservice.repository;

import org.ship.shipservice.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountDao extends CrudRepository<Account,Long>{

}
