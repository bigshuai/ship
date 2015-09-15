package org.ship.shipservice.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

public class OilStationBean{
	private Integer id;
	private String name;
	private String desc;
	private String address;
	private String picUrl;
	private String latitude;
	private String longitude;
	private String phone;
	private Integer cityId;
	private Float credit;// 信誉等级
	private Float quality;// 品质
	private Float service;// 服务
	private Integer appraiseNum;//评论数
	private Integer couponFlag;
	private String derate;//500-30#200-20
	private Integer status;
	private Date createTime;
	private Date updateTime;
	private List<OilBean> oils = new ArrayList<OilBean>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAppraiseNum() {
		return appraiseNum;
	}

	public void setAppraiseNum(Integer appraiseNum) {
		this.appraiseNum = appraiseNum;
	}

	public Float getCredit() {
		return credit;
	}

	public void setCredit(Float credit) {
		this.credit = credit;
	}

	public Float getQuality() {
		return quality;
	}

	public void setQuality(Float quality) {
		this.quality = quality;
	}

	public Float getService() {
		return service;
	}

	public void setService(Float service) {
		this.service = service;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getCouponFlag() {
		return couponFlag;
	}

	public void setCouponFlag(Integer couponFlag) {
		this.couponFlag = couponFlag;
	}

	public String getDerate() {
		return derate;
	}

	public void setDerate(String derate) {
		this.derate = derate;
	}

	public List<OilBean> getOils() {
		return oils;
	}

	public void setOils(List<OilBean> oils) {
		this.oils = oils;
	}
	
	public void addOil(OilBean oil){
		this.oils.add(oil);
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
