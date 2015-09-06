package org.ship.shipservice.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_info_type_one")
public class InfoTypeOne extends IdEntity {
	private String infoTypeOne;
	private List<InfoTypeTwo> infoTypeTwoList;

	public InfoTypeOne() {
	}

	public String getInfoTypeOne() {
		return infoTypeOne;
	}

	public void setInfoTypeOne(String infoTypeOne) {
		this.infoTypeOne = infoTypeOne;
	}
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id")
	public List<InfoTypeTwo> getInfoTypeTwoList() {
		return infoTypeTwoList;
	}

	public void setInfoTypeTwoList(List<InfoTypeTwo> infoTypeTwoList) {
		this.infoTypeTwoList = infoTypeTwoList;
	}
}
