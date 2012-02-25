package com.james.skiller.model;

public class Order {

	private String orderName;
	private String orderStatus;

	public Order(String orderName, String orderStatus) {
		this.orderName = orderName;
		this.orderStatus = orderStatus;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
}