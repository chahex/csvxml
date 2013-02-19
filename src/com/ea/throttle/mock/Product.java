package com.ea.throttle.mock;

import java.util.ArrayList;
import java.util.List;

enum EnumProduct{
	FARMVILLE (new String[]{"ORD-FMV:999999"}),
	QQWARE(new String[]{"ORD-QW:425871"}),
	GENERALS(new String[]{"ORD-GG:571428"}),
	MYLLON(new String[]{"ORD-MLN:857142", "ORD-MLN:142587", "ORD-MLN:285714"}),
	DEATHSTAR(new String[]{"ORD-DSTAR:38638"}),
	EXCAVATORY(new String[]{"ORD-EXVT:15213","ORD-EXVT:02140"}),
	EVO(new String[]{"SUBS-PIT-DECR2012AUG-12M","BRANCH-PSY-DECR2012SEPT-12M", "SUBS-BOS-DECR2012AUG-1MO"}),
	RA(new String[]{"ORD-RADR:20408"});

	EnumProduct(String[] ids)
	{
		this.ids = ids;
	}

	private final String[] ids;

	public String[]	getIds()
	{
		return this.ids;
	}
}

public class Product {

	static List<Product> allProducts;
	static int allProductCount;

	static java.util.Random r = new java.util.Random();

	static{
		allProducts = new ArrayList<Product>();
		for(EnumProduct ep : EnumProduct.values())
		{
			for(String s : ep.getIds())
			{
				Product p = new Product();
				p.prodName = ep.name();
				p.prodId = s;
				allProducts.add(p);
			}
		}
		allProductCount = allProducts.size();
	}

	String prodName;
	String prodId;

	private Product(){};

	public static Product getRandomProduct(){
		return allProducts.get(r.nextInt(allProductCount));
	}

}
