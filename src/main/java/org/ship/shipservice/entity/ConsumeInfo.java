package org.ship.shipservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_consumeInfo")
public class ConsumeInfo extends IdEntity{
	private Integer userId;
	private String describe;
	private Double money;
	public ConsumeInfo(){
		
	}
	public ConsumeInfo(Long id){
		this.id=id;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
}
