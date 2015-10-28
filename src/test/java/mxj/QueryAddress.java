package mxj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class QueryAddress {

	public void findAddress() {
		String sql = "select * from xx_area";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://121.40.120.10:3306/uhoem?characterEncoding=utf-8&autoReconnect=true";
			String username = "uhoem";
			String password = "uhoem2015.";

			Connection conn = DriverManager.getConnection(url, username, password);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = null;

			HashMap<Long, Address> map = new HashMap<Long, Address>();
			
			HashMap<Long, String> parentIdMap = new HashMap<Long,String>();
			
			List<Address> list = new ArrayList<Address>();
			
			rs = ps.executeQuery();
			while (rs.next()) {
				Long id = Long.valueOf(rs.getLong("id"));
				String name = rs.getString("full_name");
				Long parentId = Long.valueOf(rs.getLong("parent"));
				map.put(id, new Address(id, name, parentId));
//				parentIdMap.put(parentId, name);
			}
			
			for (Map.Entry<Long,Address> addressMap : map.entrySet()) {
				Address address = addressMap.getValue();
				//&& parentIdMap.get(address.getId()) != null
				if(address.getParentId() > 0 ) {
					Address parent = map.get(address.getParentId());
					parent.getSubAddress().add(address);
				}
			}
			for (Map.Entry<Long,Address> addressMap : map.entrySet()) {
				if (addressMap.getValue().getParentId() == 0) {
					list.add(addressMap.getValue());
				}
			}
			
			Gson gson = new GsonBuilder().create();
			System.out.println(gson.toJson(list));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void classified(Map<Long,Address> map) {
		
	}
	
	public static void main(String[] args) {
		QueryAddress qa = new QueryAddress();
		qa.findAddress();
	}
}

class Address {
	private Long id;
	private String name;
	private Long parentId;

	private List<Address> subAddress = new ArrayList<Address>();

	public Address(Long id, String name, Long parentId) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}

	public List<Address> getSubAddress() {
		return subAddress;
	}

	public void setSubAddress(List<Address> subAddress) {
		this.subAddress = subAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}