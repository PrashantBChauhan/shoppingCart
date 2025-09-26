package com.ShopingCart.api.utility;

import java.util.ArrayList;
import java.util.List;

import com.ShopingCart.api.model.request.Item;

public class ProductHelper {
		
	public static List<Item> getItemList() {
		String menShirt = "Men's Shirt";
		String womenDress = "Women's dress";
		
		List<Item> itemList = new ArrayList<>();
		itemList.add(new Item(1, menShirt, 500) );
		itemList.add(new Item(2, menShirt, 700) );
		itemList.add(new Item(3, womenDress, 1500) );
		itemList.add(new Item(4, womenDress, 800) );
		itemList.add(new Item(5, womenDress, 2500) );
		itemList.add(new Item(6, womenDress, 550) );
		itemList.add(new Item(7, womenDress, 850) );
		itemList.add(new Item(8, menShirt, 950) );
		itemList.add(new Item(9, menShirt, 1500) );
		itemList.add(new Item(10, menShirt, 1200) );
		itemList.add(new Item(11, menShirt, 900) );
		itemList.add(new Item(12, womenDress, 750) );
		return itemList;
	}
	
}
