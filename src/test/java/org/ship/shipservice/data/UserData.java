/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.data;

import org.ship.shipservice.entity.User;
import org.springside.modules.test.data.RandomData;

public class UserData {

	public static User randomNewUser() {
		User user = new User();
//		user.setLoginName(RandomData.randomName("user"));
//		user.setName(RandomData.randomName("User"));
//		user.setPlainPassword(RandomData.randomName("password"));

		return user;
	}
}
