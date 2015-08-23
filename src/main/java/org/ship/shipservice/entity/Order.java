package org.ship.shipservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "t_order")
public class Order extends IdEntity {
	private Integer gasType;
	private Integer type;
	private Double money;
	private Integer num;
	private Integer status;
	private Date bookTime;
	private Date createTime;
	private GasStation gasStation;
	private List<CouponList> couponList = new ArrayList<CouponList>();

	public Order() {

	}

	public Order(Long id) {
		this.id = id;
	}

	public Integer getGasType() {
		return gasType;
	}

	public void setGasType(Integer gasType) {
		this.gasType = gasType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getBookTime() {
		return bookTime;
	}

	public void setBookTime(Date bookTime) {
		this.bookTime = bookTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="gas_station_id")
	public GasStation getGasStation() {
		return gasStation;
	}

	public void setGasStation(GasStation gasStation) {
		this.gasStation = gasStation;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	public List<CouponList> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponList> couponList) {
		this.couponList = couponList;
	}

}
