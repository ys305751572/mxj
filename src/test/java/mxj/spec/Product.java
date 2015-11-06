package mxj.spec;

import java.util.ArrayList;
import java.util.List;

public class Product {

	private int id;
	private String name;
	
	private List<Spec> specList = new ArrayList<Spec>();
	
	private List<SpecValues> values = new ArrayList<SpecValues>();

	private List<Product> productList = new ArrayList<Product>();
	
	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Spec> getSpecList() {
		return specList;
	}

	public void setSpecList(List<Spec> specList) {
		this.specList = specList;
	}

	public List<SpecValues> getValues() {
		return values;
	}

	public void setValues(List<SpecValues> values) {
		this.values = values;
	}
	
	
}
