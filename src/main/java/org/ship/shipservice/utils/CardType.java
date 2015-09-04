package org.ship.shipservice.utils;

public enum CardType {
	DR("DR","借记卡"),
	CR("CR","贷记卡");
	
	public CardType getCardType(String type){
		if("DR".equalsIgnoreCase(type)){
			return CardType.DR;
		}else if("CR".equalsIgnoreCase(type)){
			return CardType.CR;
		}
		return null;
	}
	private String code;
	private String name;
	private CardType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
