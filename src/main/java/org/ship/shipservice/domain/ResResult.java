package org.ship.shipservice.domain;

import org.ship.shipservice.constants.ErrorConstants;
import org.ship.shipservice.utils.CommonUtils;

/**
 * 
 * @author lyhc
 * @param <T>
 */
public class ResResult<T> {
	private String code = ErrorConstants.SUCCESS;
	private String msg = ErrorConstants.getErrorMsg(ErrorConstants.SUCCESS);
	private Integer page;
	private Integer totalSize;
	private Integer totalPages;
	private T result;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
		this.totalPages = (this.totalSize/CommonUtils.S_PAGESIZE) + 1;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
}
