package org.ship.shipservice.domain;

import org.ship.shipservice.constants.ErrorConstants;

/**
 * 
 * @author lyhc
 * @param <T>
 */
public class ResResult<T> {
	private String code = ErrorConstants.SUCCESS;
	private String msg = ErrorConstants.getErrorMsg(ErrorConstants.SUCCESS);
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
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
}
