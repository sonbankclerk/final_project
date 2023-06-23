package com.example.demo.vote;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/votes")
public class OvoteController {
	
	@Autowired
	private OvoteService service;
	
	// 투표 유무 확인.
	@GetMapping("/chk/{num}")
	public Map chkVote(@PathVariable("num") int memnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			boolean chk = service.chkVote(memnum);
			map.put("chk", chk);
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 투표 시 추가.
	@PostMapping("")
	public Map save(OvoteDto dto) {
		System.out.println("dto : " + dto);
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			dto = service.save(dto);
			map.put("batnum", dto.getBatnum());
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
