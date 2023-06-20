package com.example.demo.bookmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/obookmark")
public class ObookmarkController {

	@Autowired
	private ObookmarkService service;

	// 전체목록 검색
	@GetMapping("")
	public Map getAll() {
		ArrayList<ObookmarkDto> list = service.getAll();
		Map map = new HashMap();
		map.put(list, list);
		return map;
	}
	
	//북마크 추가
	@PostMapping("")
	public Map save(ObookmarkDto dto) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ObookmarkDto result = service.save(dto);
			map.put("dto", result);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 북마크번호로 삭제
	@DeleteMapping("/{bmnum}")
	public Map del(@PathVariable("bmnum") int bmnum) {
		boolean flag = true;
		Map map = new HashMap();
		try {
			service.delOreport(bmnum);
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
