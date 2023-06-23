package com.example.demo.battle;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.MembershipKey;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.member.OmemberDto;
import com.example.demo.member.OmemberService;
import com.example.demo.vote.OvoteService;


@RestController
@CrossOrigin(origins ="*")
@RequestMapping("/battles")
public class ObattleController {
	
	@Autowired
	private ObattleService service;
	
	@Autowired
	private OvoteService voteService;
	
	@Autowired
	private OmemberService memberService;
	
	@Value("${spring.servlet.multipart.location}")
	private String basepath;
	
	// 테마 바꾸기
	@PutMapping("/manager")
	public Map updateTheme(String theme) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			OmemberDto dto = memberService.getById("manager");
			service.updateTheme(theme,dto.getMemnum(),dto);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	
	// 그 주의 테마와 라운드 수 얻기.
	@GetMapping("/info")
	public Map showTheme() {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			// manager가 갖고 있는 테마가 곧 그 주의 테마다. 테마 얻기
			OmemberDto dto = memberService.getById("manager");
			String theme = service.findByMemnum(dto.getMemnum()).getTheme();
			map.put("theme", theme);		
			
			// 라운드 수 얻기.
			int roundcnt = service.findRoundCnt();
			map.put("roundcnt", roundcnt);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		System.out.println("map : " + map);
		return map;
	}
	
	
	// 신청하기.
	@PostMapping("")
	public Map save(ObattleDto dto, @RequestParam("mf") MultipartFile mf) {
		Map map = new HashMap<>();
		System.out.println("dto : " + dto);
		System.out.println("mf : " + mf);
		boolean flag = true;
		try {
			// dto 저장 후.
			
			// 각 dto의 이미지들을 저장할 디렉토리를 만든다.
			File dir = new File(basepath + "battle" + dto.getRoundcnt());
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
	
	// 신청 유무 확인하기.
	@GetMapping("/chk/{memnum}")
	public Map chkApply(@PathVariable("memnum") int memnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			boolean chk = service.chkApply(memnum);
			map.put("chk", chk);
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 후보 두명 랜덤 뽑아서 보여주기.
	@GetMapping("/manager/random")
	public Map findCandidates() {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			OmemberDto dto = memberService.getById("manager");
			ArrayList<ObattleDto> list = service.findCandidates(dto.getMemnum());
			map.put("list", list);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag",flag);
		return map;
	}
	
	
	// 랜덤으로 뽑은 두명을 후보로 확정하기.
	// 두명을 제외한 신청자들을 삭제해야하므로 delete.
	@DeleteMapping("/manager/random/{num1}/{num2}")
	public Map deleteRandom(@PathVariable("num1")int num1, @PathVariable("num2")int num2) {
		Map map = new HashMap<>();
		boolean flag = true;
		System.out.println("num1 : " + num1);
		System.out.println("num2 : " + num2);
		try {
			// dir에 들어있는 이미지들을 삭제한다.
			ArrayList<ObattleDto> list = service.listNotCandidates(num1, num2);
			for(ObattleDto dto : list) {
				String fname = dto.getImg();
				File delFile = new File(fname);
				if(delFile != null && delFile.exists()) {
					delFile.delete();
				}
			}
			
			OmemberDto manager = memberService.getById("manager");
			// 두명을 제외하고 모두 삭제한다.
			service.deleteNotCandidates(num1, num2, manager.getMemnum());
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
			OmemberDto manager = memberService.getById("manager");
			ArrayList<ObattleDto> list = service.listCandidates(manager.getMemnum());
			map.put("list", list);
			map.put("len",list.size());
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
			// vote 테이블에서 투표 수가 더 많은 튜플을 추출 후 
			// 그 튜플에 있는 batnum을 뽑느다.
			Integer batnum = voteService.findWinner();
			if(batnum == null) {
				
			}else {
				// batnum을 명예의 전당으로 승급.
				ObattleDto dto = service.findWinner(batnum);
				// 명예의 전당으로 승급 후 투표 모두 삭제.
				voteService.deleteAll();
				// 패자 삭제 
				// (manager에서 memnum을 받아서 winner가 false인 것 중 manager를 제외한 것 삭제)
				OmemberDto manager = memberService.getById("manager");
				service.deleteLoser(manager.getMemnum());
				map.put("dto", dto);
			}
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
	
	// wincount 세기
	@GetMapping("/winCount/{memnum}")
	public Map winCount(@PathVariable("memnum") int memnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			int winCount = service.winCount(memnum);
			map.put("winCount", winCount);
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
				System.out.println("fname : " + fname);
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
