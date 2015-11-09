package org.ship.shipservice.domain;

public class OrderBean {
	//o.id,o.os_id,s.name,o.product_id,o.product_name,o.type,o.money,o.price,o.num,
	//o.status,o.order_no,o.sft_order_no,o.book_time,o.create_time
	private Long id;
	private Long osId;
	private String osName;
	private String productId;
	private String productName;
	private String productDesc;
	private Integer type;
	private String money;
	private Integer num;
	private String price;
	private Integer status;
	private String orderNo;
	private String sftOrderNo;
	private String bookTime;
	private String bookAddr;
	private String createTime;
	
	private Long userId;
	private String phone;
	private String userName;
    private String shipName;
	private String merchantOrderNo;
	private Long couponId;
	private String amount;
	private String osPicUrl;
	private String consumeCode;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}
	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public Long getOsId() {
		return osId;
	}
	public void setOsId(Long osId) {
		this.osId = osId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSftOrderNo() {
		return sftOrderNo;
	}
	public void setSftOrderNo(String sftOrderNo) {
		this.sftOrderNo = sftOrderNo;
	}
	public String getBookTime() {
		return bookTime;
	}
	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getBookAddr() {
		return bookAddr;
	}
	public void setBookAddr(String bookAddr) {
		this.bookAddr = bookAddr;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getShipName() {
		return shipName;
	}
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOsPicUrl() {
		return osPicUrl;
	}
	public void setOsPicUrl(String osPicUrl) {
		this.osPicUrl = osPicUrl;
	}
	public String getConsumeCode() {
		return consumeCode;
	}
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}
	
}
