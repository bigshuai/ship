package org.ship.shipservice.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_coupon")
public class Coupon extends IdEntity {
	private String name;
	private String desc;
	private Double faceValue;
	private Integer limitValue;
	private Integer type;
	private Integer effectiveDay;
	private Date startTime;
	private Date endTime;
	private Date createTime;
	private Integer status;

	public Coupon() {
	}

	public Coupon(String name, String desc, Double faceValue,
			Integer limitValue, Integer type, Integer effectiveDay,
			Date startTime, Date endTime, Date createTime, Integer status) {
		this.name = name;
		this.desc = desc;
		this.faceValue = faceValue;
		this.limitValue = limitValue;
		this.type = type;
		this.effectiveDay = effectiveDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.createTime = createTime;
		this.status = status;
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

	public Double getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(Double faceValue) {
		this.faceValue = faceValue;
	}

	public Integer getLimitValue() {
		return limitValue;
	}

	public void setLimitValue(Integer limitValue) {
		this.limitValue = limitValue;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getEffectiveDay() {
		return effectiveDay;
	}

	public void setEffectiveDay(Integer effectiveDay) {
		this.effectiveDay = effectiveDay;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
