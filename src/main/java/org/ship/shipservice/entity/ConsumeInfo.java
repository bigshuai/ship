package org.ship.shipservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_user_consume_log")
public class ConsumeInfo extends IdEntity{
	private Integer userId;
	private String orderNo;
	private String amount;
	private String transAmount;
	private Integer type;
	private Integer code;
	private Integer status;
	private String createtime;
	
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
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name="create_time")
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
