package org.ship.shipservice.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_order")
public class Order extends IdEntity {
	private Integer userId;
	private Long osId;
	private Long productId;
	private String productName;
	private Integer type;
	private Double money;
	private BigDecimal price;
	private Integer num;
	private Integer status;
	private String orderNo;
	private String sftOrderNo;
	private String sessionToken;
	private Date bookTime;
	private Date createTime;
	//private OilStation oilStation;
	private List<CouponList> couponList = new ArrayList<CouponList>();

	public Order() {

	}

	public Order(Long id) {
		this.id = id;
	}

	@Column(name = "user_id")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

//	@OneToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "os_id")
//	public OilStation getOilStation() {
//		return oilStation;
//	}
//
//	public void setOilStation(OilStation oilStation) {
//		this.oilStation = oilStation;
//	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	public List<CouponList> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponList> couponList) {
		this.couponList = couponList;
	}
	public Long getOsId() {
		return osId;
	}

	public void setOsId(Long osId) {
		this.osId = osId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSftOrderNo() {
		return sftOrderNo;
	}

	public void setSftOrderNo(String sftOrderNo) {
		this.sftOrderNo = sftOrderNo;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
