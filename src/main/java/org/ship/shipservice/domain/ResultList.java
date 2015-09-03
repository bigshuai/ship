package org.ship.shipservice.domain;

public class ResultList {
	private Integer page;
	private Integer total;
	private Object dataList;
	
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Object getDataList() {
		return dataList;
	}
	public void setDataList(Object dataList) {
		this.dataList = dataList;
	}
}
