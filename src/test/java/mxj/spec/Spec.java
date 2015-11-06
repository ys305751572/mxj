package mxj.spec;

import java.util.ArrayList;
import java.util.List;

public class Spec {

	private int id;
	private String name;
	
	private List<SpecValues> valuesList = new ArrayList<SpecValues>();
	
	
	
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
}
