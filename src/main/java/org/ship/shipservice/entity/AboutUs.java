package org.ship.shipservice.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_hyb")
public class AboutUs extends IdEntity {
	private String registProtocol;
	private String aboutUs;
	private String payProtocol;
	private Date createTime;

	@Column(name = "regist_protocol")
	public String getRegistProtocol() {
		return registProtocol;
	}

	public void setRegistProtocol(String registProtocol) {
		this.registProtocol = registProtocol;
	}

	@Column(name = "about_us")
	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
	}

	@Column(name = "pay_protocol")
	public String getPayProtocol() {
		return payProtocol;
	}

	public void setPayProtocol(String payProtocol) {
		this.payProtocol = payProtocol;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
