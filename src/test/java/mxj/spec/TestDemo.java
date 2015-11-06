package mxj.spec;

import java.util.ArrayList;
import java.util.List;

public class TestDemo {

	public static void main(String[] args) {
		
		Product p1 = new Product();
		p1.setId(1);
		p1.setName("鞋子1");
		
		Product p2 = new Product();
		p2.setId(2);
		p2.setName("鞋子2");
		
		Spec spec1 = new Spec();
		spec1.setId(1);
		spec1.setName("尺寸");
		
		Spec spec2 = new Spec();
		spec2.setId(2);
		spec2.setName("颜色");
		
		SpecValues v1 = new SpecValues();
		v1.setId(1);
		v1.setName("38");
		
		SpecValues v2 = new SpecValues();
		v2.setId(2);
		v2.setName("39");
		
		SpecValues v3 = new SpecValues();
		v3.setId(3);
		v3.setName("红色");
		
		SpecValues v4 = new SpecValues();
		v4.setId(4);
		v4.setName("黄色");
		
		p1.getSpecList().add(spec1);
		p1.getSpecList().add(spec2);
		
		p1.getValues().add(v1);
		p1.getValues().add(v3);
		
		p2.getSpecList().add(spec1);
		p2.getSpecList().add(spec2);
		
		p2.getValues().add(v1);
		p2.getValues().add(v4);
		
		p1.getProductList().add(p1);
		p1.getProductList().add(p2);
		
		List<Product> pList = new ArrayList<Product>();
		
		pList.add(p1);
		pList.add(p2);
		
		List<Spec> list = p1.getSpecList();
		for (Spec spec : list) {
			System.out.print(spec.getName() + ":");
			List<SpecValues> vs = p1.getValues();
			for (SpecValues specValues : vs) {
				
				List<Product> pList2 = p1.getProductList();
				for (Product product : pList2) {
					List<SpecValues> v2List = product.getValues();
					for (SpecValues specValues2 : v2List) {
						if(specValues2.getId() == specValues.getId()) {
							System.out.print("==pid:" + product.getId() + "==name:" + specValues.getName() + "|");
							
						}
					}
				}
			}
			System.out.println();
		}
	}
}
