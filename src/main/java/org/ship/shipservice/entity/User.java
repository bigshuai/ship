package org.ship.shipservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
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
	private Picture picture;//头像
	private int status;
	private Date registerDate;
	private Account account;//我的钱包
	private List<CouponList> couponList = new ArrayList<CouponList>();
	private List<Order> orderList = new ArrayList<Order>();
	public User() {
	}

	public User(Long id) {
		this.id = id;
	}

	@Column(name = "user_name")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "pwd")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email")
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

	@Column(name = "ship_name")
	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname;
	}

	@Column(name = "ship_no")
	public String getShipno() {
		return shipno;
	}

	public void setShipno(String shipno) {
		this.shipno = shipno;
	}

	@Column(name = "company")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "create_time")
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	@OneToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="pid",unique=true)
	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}
	//主键相等的一对一
	@OneToOne
	@PrimaryKeyJoinColumn
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	@OrderBy(value = "create_time desc")
	public List<CouponList> getCouponList() {
		return couponList;
	}
   
	public void setCouponList(List<CouponList> couponList) {
		this.couponList = couponList;
	}
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="user_id")
	@OrderBy(value="create_time desc")
	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}