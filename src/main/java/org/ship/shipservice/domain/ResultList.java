package org.ship.shipservice.domain;

public class ResultList<T> {
	private Integer total;
	private Integer size;
	private T dataList;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public T getDataList() {
		return dataList;
	}
	public void setDataList(T dataList) {
		this.dataList = dataList;
	}
}
