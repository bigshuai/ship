package org.ship.shipservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_info_action")
public class InfoAction extends IdEntity {
	private String infoAction;

	public InfoAction() {
	}

	public String getInfoAction() {
		return infoAction;
	}

	public void setInfoAction(String infoAction) {
		this.infoAction = infoAction;
	}
}
