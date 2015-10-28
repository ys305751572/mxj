package com.bluemobi.pro.entity;

/**
 * 订单项
 * 
 * @author yesong
 *
 */
public class OrderItemVO {

	private Long id;
	private String name;
	private Double price;
	private Integer product_quantity;
	private String image;

	// private ProductVO productList;
	//
	// public ProductVO getProductList() {
	// return productList;
	// }
	//
	// public void setProductList(ProductVO productList) {
	// this.productList = productList;
	// }
	
	
	public Long getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProduct_quantity() {
		return product_quantity;
	}

	public void setProduct_quantity(Integer product_quantity) {
		this.product_quantity = product_quantity;
	}

}
