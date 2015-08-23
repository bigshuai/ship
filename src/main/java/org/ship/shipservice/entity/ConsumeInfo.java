package org.ship.shipservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_consumeInfo")
public class ConsumeInfo extends IdEntity{
	private Integer accountId;
	private String describe;
	private Double money;
	public ConsumeInfo(){
		
	}
	public ConsumeInfo(Long id){
		this.id=id;
	}
	
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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
