package com.example.demo.likebnt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/olikebtn")
public class OlikebtnController {
	@Autowired
	private OlikebtnService service;
	
	// 좋아요 추가
	@PostMapping
	public Map sava(OlikebtnDto dto) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			OlikebtnDto result = service.save(dto);
			map.put("dto", result);
		} catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 좋아요 삭제
	@DeleteMapping("")
	public Map delOlikebtn(int likebtn) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			service.delOlikebtn(likebtn);
		} catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
		
}
