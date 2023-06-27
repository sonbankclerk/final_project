package com.example.demo.ootw;

import java.util.ArrayList;
import java.util.Date;
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

import com.example.demo.omycloset.Omycloset;
import com.example.demo.omycloset.OmyclosetDto;
import com.example.demo.omycloset.OmyclosetService;
import com.example.demo.ootwImgs.OootwimgsDto;
import com.example.demo.ootwImgs.OootwimgsService;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/boards")
public class OootwController {
	@Autowired
	private OootwService service;
	@Autowired
	private OootwimgsService imgservice;
	@Autowired
	private OmyclosetService closetservice;
	
	// 게시글 작성.. POST
	@PostMapping("")
	public Map add(OootwDto dto, Integer[] closetnum) {
		// oootwdto = "ootwnum", memnum, odate, weather, temp, comments
		// oootwimgsdto = ootwimgsnum, "ootwnum", closetnum
		
		// 옷장 이미지 정보 제외한 것들 저장
		OootwDto odto = service.save(dto);
		int closetnum1 = closetnum[0];
		int closetnum2 = closetnum[1];
		int closetnum3 = closetnum[2];
		int closetnum4 = closetnum[3];
		int closetnum5 = closetnum[4];
		// int 옷장 번호로 옷장 정보 불러오기
		OmyclosetDto closetdto1 = closetservice.getMyCloth(closetnum1);
		OmyclosetDto closetdto2 = closetservice.getMyCloth(closetnum2);
		OmyclosetDto closetdto3 = closetservice.getMyCloth(closetnum3);
		OmyclosetDto closetdto4 = closetservice.getMyCloth(closetnum4);
		OmyclosetDto closetdto5 = closetservice.getMyCloth(closetnum5);
		// oootwimgs entity에 넣을 Omyclost, Oootw 멤버변수 구하기 위해 각자 생성자 생성
		Omycloset closetVo1 = new Omycloset();
		Omycloset closetVo2 = new Omycloset();
		Omycloset closetVo3 = new Omycloset();
		Omycloset closetVo4 = new Omycloset();
		Omycloset closetVo5 = new Omycloset();
		Oootw oootwVo1 = new Oootw();
		Oootw oootwVo2 = new Oootw();
		Oootw oootwVo3 = new Oootw();
		Oootw oootwVo4 = new Oootw();
		Oootw oootwVo5 = new Oootw();
		// 각각의 생성자에 위에서 저장한 dto의 fk들 저장
		closetVo1.setClosetnum(closetdto1.getClosetnum());
		oootwVo1.setOotwnum(odto.getOotwnum());
		closetVo2.setClosetnum(closetdto2.getClosetnum());
		oootwVo2.setOotwnum(odto.getOotwnum());
		closetVo3.setClosetnum(closetdto3.getClosetnum());
		oootwVo3.setOotwnum(odto.getOotwnum());
		closetVo4.setClosetnum(closetdto4.getClosetnum());
		oootwVo4.setOotwnum(odto.getOotwnum());
		closetVo5.setClosetnum(closetdto5.getClosetnum());
		oootwVo5.setOotwnum(odto.getOotwnum());
		// 마지막으로 Oootwimgs Dto에 각각 멤버변수 집어넣기
		OootwimgsDto imgdto1 = new OootwimgsDto();
		imgdto1.setClosetnum(closetVo1);
		imgdto1.setOotwnum(oootwVo1);
		OootwimgsDto imgdto2 = new OootwimgsDto();
		imgdto2.setClosetnum(closetVo2);
		imgdto2.setOotwnum(oootwVo2);
		OootwimgsDto imgdto3 = new OootwimgsDto();
		imgdto3.setClosetnum(closetVo3);
		imgdto3.setOotwnum(oootwVo3);
		OootwimgsDto imgdto4 = new OootwimgsDto();
		imgdto4.setClosetnum(closetVo4);
		imgdto4.setOotwnum(oootwVo4);
		OootwimgsDto imgdto5 = new OootwimgsDto();
		imgdto5.setClosetnum(closetVo5);
		imgdto5.setOotwnum(oootwVo5);
		// set을 토대로 OootwimgsDto 새로 저장
		OootwimgsDto finalImgDto = imgservice.save(imgdto1);
		finalImgDto = imgservice.save(imgdto2);
		finalImgDto = imgservice.save(imgdto3);
		finalImgDto = imgservice.save(imgdto4);
		finalImgDto = imgservice.save(imgdto5);
		Map map = new HashMap<>();
		map.put("dto", odto);
		return map;
	}
	
	// 디테일 뿌리기.. GET(/ootwnum)
	@GetMapping("/{ootwnum}")
	public Map getByNum(@PathVariable("ootwnum") int ootwnum) {
		OootwDto dto = service.getMyBoard(ootwnum);
		ArrayList<OootwimgsDto> list = imgservice.getMyImgs(ootwnum); // 게시글 번호(ootwnum)로 옷장 정보(..closetnum) 불러와서 담기
		System.out.println(list);
		Map map = new HashMap<>();
		map.put("dto", dto);
		map.put("list", list); // vue에서 list.closetnum.closetnum으로 closetnum 뽑아서 옷장 img src 뿌리기
		// vue created:function에서 list for문 돌리고, closetnum 있는만큼 뽑아서 closet controller에서 img src 뽑아오기
		return map;
	}
	
	// 전체 리스트 뿌리기.. 전체 리스트는 이미지 없이 보여줄거임 GET
	@GetMapping("")
	public Map getAll() {
		ArrayList<OootwDto> list = service.getAll();
		Map map = new HashMap<>();
		map.put("list", list);
		return map;
	}
	
	// 날짜 검색 리스트 뿌리기.. GET(/dates/odate)
	@GetMapping("/dates/{date1}/{date2}")
	public Map getByDate(@PathVariable("date1") String date1, @PathVariable("date2") String date2) {
		String odate1 = date1.substring(2, 4) + "/" + date1.substring(5, 7) + "/" + date1.substring(8, 10);
		String odate2 = date2.substring(2, 4) + "/" + date2.substring(5, 7) + "/" + date2.substring(8, 10);
		System.out.println(odate1);
		System.out.println(odate2);
		ArrayList<OootwDto> list = service.getByDateBetween(odate1, odate2);
		Map map = new HashMap<>();
		map.put("list", list);
		return map;
	}
	
	// 게시글 & 게시글 이미지 정보 삭제하기.. DELETE(/ootwnum)
	@DeleteMapping("/{ootwnum}")
	public Map delete(@PathVariable("ootwnum") int ootwnum) {
		boolean flag = true;
		try {
			service.delete(ootwnum); // 이미지 정보 제외한 게시글 정보 삭제
			imgservice.delete(ootwnum); // 게시글에 올린 옷장 이미지 정보(게시글 num으로 연결된 사진정보 전부) 삭제
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		return map;
	}
}