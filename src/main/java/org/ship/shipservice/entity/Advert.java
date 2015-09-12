package org.ship.shipservice.entity;

public class Advert extends IdEntity{
	private String picUrl;
	private String url;
	
	public Advert() {
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
