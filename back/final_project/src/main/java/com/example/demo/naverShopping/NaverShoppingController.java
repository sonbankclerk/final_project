package com.example.demo.naverShopping;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/naver")
public class NaverShoppingController {
	
	@Autowired
	private NaverShoppingService service;
	
	// 네이버 쇼핑 리스트 들고오자 빠끄!
	@GetMapping("/{query}")
	public Map searchShopping(@PathVariable("query") String query) {
		System.out.println("query : " + query);
		return service.search(query);
	}
	
}
