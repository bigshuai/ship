package org.ship.shipservice.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_derate")
public class Derate extends IdEntity {
	private Integer osId;
	private String info;
	private Date createTime;
	private Date updateTime;
	
	public Integer getOsId() {
		return osId;
	}
	public void setOsId(Integer osId) {
		this.osId = osId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
}
