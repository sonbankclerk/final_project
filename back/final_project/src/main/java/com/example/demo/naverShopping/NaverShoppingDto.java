package com.example.demo.naverShopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverShoppingDto {
	private long productId;
	private String title;
	private String link;
	private	String img;
	private String mallName;
	private String maker;
	private int price;
}
