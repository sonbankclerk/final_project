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
			ObattleDto battleDto = service.findByMemnum(dto.getMemnum());
			if(battleDto == null) {
				map.put("changeTheme", false);
				return map;
			}else {
				String theme = battleDto.getTheme();
				map.put("theme", theme);		
				
				// 라운드 수 얻기.
				int roundcnt = service.findRoundCnt();
				map.put("roundcnt", roundcnt);
				
				// 만약 관계자가 새로운 테마로 바꾸지 않았다면 아직 테마가 변경되지 않았다고 알린다.
				// 명예의 전당에서 가장 최근에 있던 테마를 가지고와서
				// 현재 테마와 비교한다.
				ArrayList<ObattleDto> list = service.winnerList();
				if(list != null) {
					String lastTheme = list.get(0).getTheme();
					// 테마가 같다면 changeTheme = false가 된다.
					boolean changeTheme = !lastTheme.equals(theme);
					map.put("changeTheme", changeTheme);
				}
			}
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
			
			// len이 있는 이유는 아직 신청자의 수가 2명 이상이 안될 수도 있기 때문이다.
			// vue에서 len != 2라면 아직 신청자의 수가 충분하지 않다는 alert를 띄워준다.
			map.put("list", list);
			map.put("len", list.size());
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
			// 여기서 len으로 list의 길이를 보여주는 이유는
			// vue에서 오류가 뜨는 것을 방지하기 위함이다.
			// 오류가 뜬다면 아직 준비되지 않았다는 알림이 있는 notYet component로 이동한다.
			// list의 길이가 2일때만 제대로 작동하도록 vue에서 만들었다.
			// 혹시 아직 준비되지 않았다는 모습이 보이면 관계자가 직접 투표 후보를 만들어줘야 한다.
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
			
			// 자동으로 완성하기 위해 아래와 같은 로직을 짰다.
			// 처음 금주의 우승자를 클릭하면 투표 테이블에 데이터가 있을 것이니
			// batnum != null이다.
			// 따라서 else로 들어가게 된다.
			// 두번째 이후로는 batnum == null 이므로 명예의 전당에서 제일 최근에 있는 튜플을 추출한다.
			// ex) 명예의 전당은 roundcnt의 역순으로 정렬되어 있으므로 첫번째 값이 제일 최근에 값이 된다.
			if(batnum == null) {
				ArrayList<ObattleDto> list = service.winnerList();
				ObattleDto dto = list.get(0);
				
				map.put("dto", dto);
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
