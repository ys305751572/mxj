package com.bluemobi.pro.entity;

import java.util.List;

/**
 * 订单
 * @author yesong
 *
 */
public class OrderVO {

	private Long id;
	
	private String expire;
	
	private String create_date;
	
	private String order_status; // 0：待支付 1：已支付 2：已取消3：已收货4：已评价
	
	private AddressVO address;
	
	private String sn;
	
	private List<OrderItemVO> orderItems;
	
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getExpire() {
		return expire;
	}
	public void setExpire(String expire) {
		this.expire = expire;
	}
	public List<OrderItemVO> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemVO> orderItems) {
		this.orderItems = orderItems;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AddressVO getAddress() {
		return address;
	}
	public void setAddress(AddressVO address) {
		this.address = address;
	}
}
