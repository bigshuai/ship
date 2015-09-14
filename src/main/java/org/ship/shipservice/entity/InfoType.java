package org.ship.shipservice.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_info_type")
public class InfoType extends IdEntity {
	private String infoType;
	private List<InfoTypeOne> infoTypeOneList;
	private List<InfoAction> infoActionList;
	private List<City> cityList;
	public InfoType() {
	}

	@Column(name = "type")
	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id")
	public List<InfoTypeOne> getInfoTypeOneList() {
		return infoTypeOneList;
	}

	public void setInfoTypeOneList(List<InfoTypeOne> infoTypeOneList) {
		this.infoTypeOneList = infoTypeOneList;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id")
	public List<InfoAction> getInfoActionList() {
		return infoActionList;
	}

	public void setInfoActionList(List<InfoAction> infoActionList) {
		this.infoActionList = infoActionList;
	}
	@Transient
	public List<City> getCityList() {
		return cityList;
	}

	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
	
}
