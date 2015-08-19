package org.ship.shipservice.service.account;

import org.ship.shipservice.entity.User;

public interface IAccountService {
	public User getUser(Integer phone,String password);
}
