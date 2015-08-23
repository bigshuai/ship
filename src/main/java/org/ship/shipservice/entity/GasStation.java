package org.ship.shipservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_gas_station")
public class GasStation extends IdEntity {
	private String name;
	private String desc;
	private String address;
	private Integer credit;// 信誉等级
	private Integer quality;// 品质
	private Integer service;// 服务
	private Integer status;

	public GasStation() {
	}

	public GasStation(Long id) {
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

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
