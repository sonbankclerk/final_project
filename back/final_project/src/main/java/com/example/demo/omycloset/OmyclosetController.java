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
	
	// 옷장에 옷 등록하기_이미지파일 받는 버전(POST)
	@PostMapping("")
	public Map addCloth(OmyclosetDto dto, MultipartFile f) {
		boolean flag = true;
		try {
			int closetnum = service.saveint(dto);
			int memnum = dto.getMemnum().getMemnum();
			String newpath = path + memnum + "/" + closetnum;
			System.out.println("폴더 경로: " + newpath);
			// springboot에 등록된 path 밑에 하위폴더 하나씩만 생성 가능?
			// 멤버번호로 하위폴더 하나 우선 만들고
			File dir1 = new File(path + memnum);
			dir1.mkdir();
			// 위에서 만든 멤버번호 폴더 밑에 옷장번호 하위폴더 하나 더 생성
			File dir2 = new File(newpath); // 하위폴더 경로 설정
			dir2.mkdir(); // 디렉토리 생성
			String img = "";
			String fname = f.getOriginalFilename();
			System.out.println("fname : " + fname);
			if(fname != null & !fname.equals("")) {
				newpath += "/" + fname; // C:/closet/memnum/clothnum/fname..
				System.out.println("파일 경로: " + newpath);
				File newfile = new File(newpath); // C:/closet/memnum/clothnum/fname.. 복사 생성
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
			dto.setClosetnum(closetnum); // num 덮어쓰기 안하면 img null인 것과 아래에서 완전체 둘 다 생성됨 why?
		} catch (Exception e) {
			flag = false;
		}
		int savenum = service.saveint(dto);
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("dto", savenum);
		return map;
	}
	
//	// 옷장 등록_이미지 파일 안받는 버전(기본 이미지 사용.. img컬럼에 경로만 저장).. 옷 이미지 없으면 등록 불가능하게 프론트에서 작업
//	@PostMapping("/nofile")
//	public Map addCloth2(OmyclosetDto dto) {
//		boolean flag = true;
//		OmyclosetDto dto2 = service.save(dto);
//		Map map = new HashMap<>();
//		map.put("dto", dto2);
//		return map;
//	}
	
	// 옷장에 등록한 옷 디테일 보여주기(GET(closetnum) / 검색하기)
	@GetMapping("/{closetnum}")
	public Map getByNum(@PathVariable("closetnum") int closetnum) {
		OmyclosetDto dto = service.getMyCloth(closetnum); // num(pk)으로 옷 불러오기
		Map map = new HashMap<>();
		map.put("dto", dto); // 보내기
		return map;
	}
	
	// 옷 이름, 사진 부분 수정하기(PUT / old & new 부분 수정)
	@PatchMapping("/editcloth/{closetnum}/{cloth}")
	public Map patchedit(@PathVariable("closetnum") int closetnum, @PathVariable("cloth") String cloth, MultipartFile newf) {
		boolean flag = true;
		OmyclosetDto dto = service.getMyCloth(closetnum);
		try {
			int memnum = dto.getMemnum().getMemnum();
			String img = "";
			String oldpath = dto.getImg(); // 원본(옛날옷)의 파일명
			String newfname = newf.getOriginalFilename(); // 받아온 옷(수정된)의 파일명
			if(newfname != null & !newfname.equals("")) { // 받아온 옷이 null이 아니라면,
				String newpath = path + memnum + "/" + closetnum + "/" + newfname; // C:/closet/memnum/closetnum/newfname.. 수정된 옷의 새로운 경로 지정
				File newfile = new File(newpath); // 새로운 경로 C:/closet/memnum/closetnum/newfname 복사 생성
				try {
					String delimg = oldpath; // 원본파일경로
					File oldfile = new File(delimg); // 원본파일 삭제 객체 생성
					oldfile.delete(); // 삭제
					newf.transferTo(newfile); // 수정 파일 업로드하기
					img = newpath; // 수정된 파일 새로운 경로
					dto.setImg(img); // dto에 새로 저장
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				dto.setImg(oldpath); // null이면 기존 옷 다시 저장
			}
		} catch (Exception e) {
			flag = false;
		}
		if(cloth != dto.getCloth()) {
			dto.setCloth(cloth);
		} else {
			dto.setCloth(dto.getCloth());
		}
		dto.setClosetnum(closetnum);
		OmyclosetDto newdto = service.save(dto);
		Map map = new HashMap<>();
		map.put("flag", flag);
		map.put("dto", newdto);
		return map;
	}
	
	// 옷 즐겨찾기 on, off 하기(PATCH(closetnum) / favorite = (0 or 1)일 때 (1 or 0)으로 수정)
	@PatchMapping("/{closetnum}")
	public Map favorite(@PathVariable("closetnum") int closetnum) {
		boolean flag = true;
		int check = 0;
		OmyclosetDto dto = service.getMyCloth(closetnum); // favorite 검사하기 위해 받아온 pk로 호출
		try {
			if(dto.getFavorite() == 0) { // favorite 0이면,
				service.updateFavoriteOn(closetnum); // 1로 수정(좋아요 on)
				check = 1;
			} else { // 0이 아니면
				service.updateFavoiteOff(closetnum); // 0으로 수정(좋아요 off)
				check = 0;
  			}
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("check", check);
		map.put("flag", flag);
		return map;
	}
	
	// 옷장 전체 리스트 뿌리기(GET)
	@GetMapping("")
	public Map getAll() {
		ArrayList<OmyclosetDto> list = service.getAllByOrder();
		Map map = new HashMap<>();
		map.put("list", list);
		System.out.println(list);
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
	
	// 옷장에 옷 등록할 때 이미지 클릭하는 부분 폴더에 저장해 놓은 기본 이미지 뜨게 하려고 만듬
	@GetMapping("/img/addimg")
	public ResponseEntity<byte[]> read_addimg(){
		String fname = "c:/closet/addimg/imageadd.png";
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
		System.out.println("result:" + result);
		return result;
	}
	
	// 옷 이미지 추출하기
	@GetMapping("/img/{memnum}/{closetnum}")
	public ResponseEntity<byte[]> read_img(@PathVariable("closetnum") int closetnum, @PathVariable("memnum") int memnum) {
		String fname = "";
		OmyclosetDto dto = new OmyclosetDto();
		dto = service.getMyCloth(closetnum);
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
		System.out.println("result:" + result);
		return result;
	}
	
	// 옷 삭제하기(Delete(closetnum))
	@DeleteMapping("/{closetnum}")
	public Map delete(@PathVariable("closetnum") int closetnum) {
		boolean flag = true;
		try {
			
			OmyclosetDto dto = service.getMyCloth(closetnum);
			int memnum = dto.getMemnum().getMemnum();
			String filePath = dto.getImg();
			String folderPath = path + memnum + "/" + closetnum;
			File oldfile = new File(filePath);
			File folder = new File(folderPath);
			oldfile.delete();
			folder.delete();
			service.delete(closetnum);
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		return map;
	}
}