package org.ship.shipservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_info_type_two")
public class InfoTypeTwo extends IdEntity {
	private String infoTypeTwo;

	public InfoTypeTwo() {
	}

	public String getInfoTypeTwo() {
		return infoTypeTwo;
	}

	public void setInfoTypeTwo(String infoTypeTwo) {
		this.infoTypeTwo = infoTypeTwo;
	}

}
