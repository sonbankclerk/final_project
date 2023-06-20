package com.example.demo.community;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.member.Omember;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ocommunity")
public class OcommunityController {
	@Autowired
	private OcommunityService service;

	@Value("${spring.servlet.multipart.location}")
	private String path; // C:/comm/

	// 전체목록 검색
	@GetMapping("")
	public Map getAll() {
		ArrayList<OcommunityDto> list = service.getAll();
		Map map = new HashMap();
		map.put("list", list);
		return map;
	}

	// 태그 3개
	@GetMapping("/{tag}")
	public Map getByTag(@PathVariable("tag") String tag) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<OcommunityDto> tags = service.getByTag(tag);
			map.put("tags", tags);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// member로 검색
	@GetMapping("/{memnum}")
	public Map getByMemnum(@PathVariable("memnum") Omember memnum) {
		Map map = new HashMap();
		boolean flag = true;
		try {

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return map;
	}

	// 게시물 생성.
	@PostMapping("")
	public Map save(OcommunityDto dto, MultipartFile[] mfArr) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			// 게시글 이미지들이 들어가있을 폴더
			File dir = new File(path + dto.getCommnum());
			dir.mkdir();

			// 이미지들 폴더에 생성하기.
			String[] imgs = new String[3];
			for (int i = 0; i < 3; i++) {
				MultipartFile mf = mfArr[i];

				// 배열로 받은 파일들 중 없을 수도 있으니 조건문.
				if (mf != null && !mf.isEmpty()) {
					// 파일 삽입.
					String fname = mf.getOriginalFilename();
					String newpath = dir.getAbsolutePath() + "//" + fname;
					File newFile = new File(newpath);
					mf.transferTo(newFile);
					imgs[i] = newpath;
				}
			}

			// 삽입된 파일이 있다면 dto 업데이트 후 다시 저장.
			dto.setImg1(imgs[0]);
			dto.setImg2(imgs[1]);
			dto.setImg3(imgs[2]);
			service.save(dto);

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 게시물 삭제하기 ( 신고당했을 때도 이용하기 )
	@DeleteMapping("")
	public Map delOcommunity(int num) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			service.delOcommunity(num);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
