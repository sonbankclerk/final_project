package com.example.demo.omycloset;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/closets")
public class OmyclosetController {
	@Autowired
	private OmyclosetService service;
	@Value("${spring.servlet.multipart.location}")
	private String path;
	
	// 옷장에 옷 등록하기(POST)
	@PostMapping("")
	public Map addCloth(OmyclosetDto dto) {
		boolean flag = true;
		OmyclosetDto dto2;
		try {
			service.save(dto);
			int num = dto.getClosetnum(); // 게시글 번호로 closet 하위폴더 생성하기 위해 num 설정
			File dir = new File(path + num); // 하위폴더 경로 설정
			dir.mkdir(); // 디렉토리 생성
			MultipartFile f = dto.getF();
			String img = "";
			String fname = f.getOriginalFilename();
			if(fname != null & !fname.equals("")) {
				String newpath = path + num + "/" + fname; // C:/closet/num/fname..
				File newfile = new File(newpath); // C:/closet/num/fname 복사 생성
				try {
					f.transferTo(newfile); // 파일 업로드하기
					img = newpath; 
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			dto.setImg(img); // dto에 img경로 넣어주기
		} catch (Exception e) {
			flag = false;
		}
		dto2 = service.save(dto); // dto 저장
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("dto", dto2);
		return map;
	}
	
	// 옷장에 등록한 옷 디테일 보여주기(GET(closetnum) / 검색하기)
	@GetMapping("/{closetnum}")
	public Map getByNum(@PathVariable("closetnum") int closetnum) {
		OmyclosetDto dto = service.getMyCloth(closetnum); // num(pk)으로 옷 불러오기
		Map map = new HashMap<>();
		map.put("dto", dto); // 보내기
		return map;
	}
	
	// 옷 이름, 카테고리, 사진 부분 수정하기(PUT / old & new 부분 수정)
	@PutMapping("")
	public Map edit(OmyclosetDto dto) {
		boolean flag = true;
		OmyclosetDto dto2;
		// 전달받은 dto num으로 수정전 옷 정보 불러와서 old에 새로 저장
		OmyclosetDto old = service.getMyCloth(dto.getClosetnum());
		old.setCloth(dto.getCloth()); // old에 수정한 옷 이름 저장
		old.setMaintag(dto.getMaintag()); // old에 수정한 메인태그 저장
		old.setSubtag(dto.getSubtag()); // old에 수정한 서브태그 저장
		try {
			int num = dto.getClosetnum(); // 하위폴더명(번호) 호출 => c:/closet/'num'
			MultipartFile oldf = old.getF(); // 받아온 번호로 불러온 옛날 옷의 (파일)정보 
			MultipartFile newf = dto.getF(); // 받아온 수정된 파일
			String img = "";
			String oldfname = oldf.getOriginalFilename(); // 원본(옛날옷)의 파일명
			String newfname = newf.getOriginalFilename(); // 받아온 옷(수정된)의 파일명
			if(newfname != null & !newfname.equals("")) { // 받아온 옷이 null이 아니라면,
				String newpath = path + num + "/" + newfname; // C:/closet/num/newfname.. 수정된 옷의 새로운 경로 지정
				File newfile = new File(newpath); // 새로운 경로 C:/closet/num/newfname 복사 생성
				try {
					String delimg = path + num + "/" + oldfname; // 원본파일경로
					File oldfile = new File(delimg); // 원본파일 삭제 객체 생성
					oldfile.delete(); // 삭제
					newf.transferTo(newfile); // 수정 파일 업로드하기
					img = newpath; // 수정된 파일 새로운 경로
					old.setImg(img); // old에 저장
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				old.setImg(path + num +"/" + oldfname); // null이 아니라면 기존 옷 다시 저장
			}
		} catch (Exception e) {
			flag = false;
		}
		dto2 = service.save(old); // 덮어쓰기
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("dto", dto2);
		return map;
	}
	
	// 옷 즐겨찾기 on, off 하기(PATCH(closetnum) / favorite = (0 or 1)일 때 (1 or 0)으로 수정)
	@PatchMapping("/{closetnum}")
	public Map favorite(@PathVariable("closetnum") int closetnum) {
		boolean flag = true;
		OmyclosetDto dto = service.getMyCloth(closetnum); // favorite 검사하기 위해 받아온 pk로 호출
		try {
			if(dto.getFavorite() == 0) { // favorite 0이면,
				service.updateFavoriteOn(closetnum); // 1로 수정(좋아요 on)
			} else { // 0이 아니면
				service.updateFavoiteOff(closetnum); // 0으로 수정(좋아요 off)
  			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		return map;
	}
	
	// 옷장 전체 리스트 뿌리기(GET)
	@GetMapping("")
	public Map getAll() {
		ArrayList<OmyclosetDto> list = service.getAll();
		Map map = new HashMap<>();
		map.put("list", list);
		return map;
	}
	
	// 옷장 대분류 카테고리 리스트 뿌리기(GET(/maintags/maintag))
	@GetMapping("/maintags/{maintag}")
	public Map getByMaintag(@PathVariable("maintag") String maintag) {
		ArrayList<OmyclosetDto> list = service.getByMaintag(maintag); // maintag 받아서 뿌리기
		Map map = new HashMap<>();
		map.put("list", list);
		return map;
	}
	
	// 옷장 소분류 리스트 뿌리기(GET(/subtags/subtag))
	@GetMapping("/subtags/{subtag}")
	public Map getBySub(@PathVariable("subtag") String subtag) {
		// subtag 눌렀을 때 저장된 subtag 보내기 
		ArrayList<OmyclosetDto> list = service.getBySubtag(subtag); // 받아온 sub로 불러오기
		Map map = new HashMap<>();
		map.put("list", list);
		return map;
	}
	
	// 옷장 옷이름 검색해서 뿌리기(GET(/clothes/cloth))
	@GetMapping("/clothes/{cloth}")
	public Map getByCloth(@PathVariable("cloth") String cloth) {
		ArrayList<OmyclosetDto> list = service.getByCloth(cloth);
		Map map = new HashMap<>();
		map.put("list", list);
		return map;
	}
	
	// 옷 이미지 추출하기
	@GetMapping("/img/{closetnum}")
	public ResponseEntity<byte[]> read_img(@PathVariable("closetnum") int closetnum) {
		String fname = "";
		OmyclosetDto dto = service.getMyCloth(closetnum);
		fname = dto.getImg();
		// 응답 객체를 생성해서 반환. 응답 객체는 헤더와 바디. 헤더:목적지주소, 나의주소, 마임타입, 크기...
		// 바디. 전송할 데이터
		File f = new File(fname);
		HttpHeaders header = new HttpHeaders(); // HttpHeaders 객체 생성
		ResponseEntity<byte[]> result = null; // 선언
		try {
			header.add("Content-Type", Files.probeContentType(f.toPath()));// 응답 데이터의 종류를 설정
			// 응답 객체 생성
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	// 옷 삭제하기(Delete(closetnum))
	@DeleteMapping("/{closetnum}")
	public Map delete(@PathVariable("closetnum") int closetnum) {
		boolean flag = true;
		try {
			service.delete(closetnum);
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		return map;
	}
}