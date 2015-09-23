package org.ship.shipservice.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "t_favorite")
public class Favorite extends IdEntity {
	private OilStation oil;
	private Information info;
	private Integer userId;
	private Integer type;
	private Date createTime;

	public Favorite() {
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "oil_id")
	@OrderBy(value = "create_time desc")
	public OilStation getOilStation() {
		return oil;
	}

	public void setOilStation(OilStation oil) {
		this.oil = oil;
	}

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "info_id")
	@OrderBy(value = "create_time desc")
	public Information getInfo() {
		return info;
	}

	public void setInfo(Information info) {
		this.info = info;
	}

}
