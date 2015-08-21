/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.ship.shipservice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name = "t_user")
public class User extends IdEntity {
	private String username;
	private String password;
	private String email;
	private String phone;
	private String shipname;
	private String shipno;
	private String company;
	private int status;
	private Date registerDate;
	public User() {
	}

	public User(Long id) {
		this.id = id;
	}

	@Column(name="user_name")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="pwd")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@NotBlank
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
    @Column(name="ship_name")
	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname;
	}
	@Column(name="ship_no")
	public String getShipno() {
		return shipno;
	}

	public void setShipno(String shipno) {
		this.shipno = shipno;
	}
    @Column(name="company")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
    @Column(name="create_time")
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}