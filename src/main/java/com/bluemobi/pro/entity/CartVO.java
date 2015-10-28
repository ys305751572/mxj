package com.bluemobi.pro.entity;

import java.util.List;

public class CartVO {

	private Long id;

	private List<CartItemVO> list;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CartItemVO> getList() {
		return list;
	}

	public void setList(List<CartItemVO> list) {
		this.list = list;
	}
}
