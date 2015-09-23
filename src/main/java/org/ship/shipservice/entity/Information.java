package org.ship.shipservice.entity;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_information")
public class Information extends IdEntity {
	private String title;
	private Integer infoType;
	private Integer infoTypeOne;
	private Integer infoTypeTwo;
	private Integer infoAction;
	private BigDecimal price;
	private String address;
	private String phone;
	private String linkMan;
	private String photo;
	private String descri;
	private Integer isDelete;
	private Long userId;
	private String tag;
	private String city;
	private Date createTime;
	private Integer reviewCount;

	public Information() {

	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "info_type")
	public Integer getInfoType() {
		return infoType;
	}

	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}

	@Column(name = "info_type_one")
	public Integer getInfoTypeOne() {
		return infoTypeOne;
	}

	public void setInfoTypeOne(Integer infoTypeOne) {
		this.infoTypeOne = infoTypeOne;
	}

	@Column(name = "info_type_two")
	public Integer getInfoTypeTwo() {
		return infoTypeTwo;
	}

	public void setInfoTypeTwo(Integer infoTypeTwo) {
		this.infoTypeTwo = infoTypeTwo;
	}

	@Column(name = "info_action")
	public Integer getInfoAction() {
		return infoAction;
	}

	public void setInfoAction(Integer infoAction) {
		this.infoAction = infoAction;
	}

	@Column(name = "price")
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "linkman")
	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	@Column(name = "photo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "descri")
	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	@Column(name = "is_delete")
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "review_count")
	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

}
