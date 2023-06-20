package com.example.demo.battle;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.websocket.server.PathParam;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping("/battle")
public class ObattleController {
	
	@Autowired
	private ObattleService service;
	
	@Value("${spring.servlet.multipart.location}")
	private String basepath;
	
	// 테마 바꾸기
	@PutMapping("/manager")
	public Map updateTheme(String theme) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			service.updateTheme(theme);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	
	// 그 주의 테마 얻기.
	@GetMapping("/theme")
	public Map showTheme() {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			// manager는 1
			String theme = service.findById(1).getTheme();
			map.put("theme", theme);			
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 신청하기.
	@PostMapping("")
	public Map save(ObattleDto dto, MultipartFile mf) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			// dto 저장 후.
			
			// 각 dto의 이미지들을 저장할 디렉토리를 만든다.
			File dir = new File(basepath + dto.getRoundcnt());
			dir.mkdir();
			
			// multipartfile에 있는 파일을 저장한다.
			String fname = mf.getOriginalFilename();
			String newpath = dir.getAbsolutePath()+"\\"+fname;
			File newFile = new File(newpath);
			mf.transferTo(newFile);
			
			// 저장된 파일의 경로를 데이터베이스에 저장한다.
			dto.setImg(URLEncoder.encode(newpath, "utf-8"));
			ObattleDto result = service.save(dto);
			map.put("dto", result);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		// 어떠한 이유(에러)로 안될 수도 있으니 값을 넣어서 반환한다.
		map.put("flag", flag);
		return map;
	}

	// 후보 두명 랜덤 뽑아서 보여주기.
	@GetMapping("/manager/random")
	public Map findCandidates() {
		ArrayList<ObattleDto> list = service.findCandidates();
		Map map = new HashMap<>();
		map.put("list", list);
		return map;
	}
	
	
	// 랜덤으로 뽑은 두명을 후보로 확정하기.
	// 두명을 제외한 신청자들을 삭제해야하므로 delete.
	@DeleteMapping("/manager/random")
	public Map deleteRandom(int[] arr) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			// 후보 두명이 들어간 배열에서 한 명씩 뽑는다.
			int num1 = arr[0];
			int num2 = arr[1]; 
			
			// dir에 들어있는 이미지들을 삭제한다.
			ArrayList<ObattleDto> list = service.listNotCandidates(num1, num2);
			for(ObattleDto dto : list) {
				String fname = dto.getImg();
				File delFile = new File(fname);
				if(delFile != null && delFile.exists()) {
					delFile.delete();
				}
			}
			
			// 두명을 제외하고 모두 삭제한다.
			service.deleteNotCandidates(num1, num2);
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		map.put("flag", flag);
		return map;
	}
	
	// 대결 후보 보기.
	@GetMapping("")
	public Map listCandidate() {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<ObattleDto> list = service.listCandidates();
			map.put("list", list);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 투표시 vote = vote + 1
	@PostMapping("/{num}")
	public Map upCnt(@PathParam("num") int num) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			// 투표 수 늘린 후.
			service.upCnt(num);
			// 투표 후보 두명 리스트.
			ArrayList<ObattleDto> list = service.listCandidates();
			map.put("list", list);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// winner 뽑기.
	@GetMapping("/winner")
	public Map findWinner() {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ObattleDto dto = service.findWinner();
			map.put("dto", dto);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 명예의 전당 리스트 뽑기.
	@GetMapping("/winnerlist")
	public Map winnerList() {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<ObattleDto> list = service.winnerList();
			map.put("list", list);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// img 보여주기
	@GetMapping("/imgs/{num}")
	public ResponseEntity<byte[]> readImg(@PathVariable("num") int num){
		// num에 대한 dto 추출.
		ObattleDto dto = service.findById(num);
		String fname = "";
		ResponseEntity<byte[]> result = null;
		
		// 파일 경로와 명 fname에 저장.
		fname = dto.getImg();
		
		// fname이 널이 아니라면
		if(fname != null && fname.length() != 0) {
			try {
				// encoding되어 있떤 fname을 decoder해주고.
				fname = URLDecoder.decode(fname,"utf-8");
				// 파일을 찾아서 뿌려준다.
				File f = new File(fname);
				HttpHeaders header = new HttpHeaders();
				header.add("Content-Type", Files.probeContentType(f.toPath()));
				result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f),header,HttpStatus.OK);
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
