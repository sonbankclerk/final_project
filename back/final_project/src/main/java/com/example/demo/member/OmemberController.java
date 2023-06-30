package com.example.demo.member;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.auth.JwtTokenProvider;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/members")
public class OmemberController {
	@Autowired
	private OmemberService service;

	@Autowired
	private JwtTokenProvider tokenprovider;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.servlet.multipart.location}")
	private String path;

	// 가입, 이미지업로드
	@PostMapping("")
	public Map join(@RequestParam("mf") MultipartFile mf, OmemberDto dto) {
//		OmemberDto d = service.save(dto);
		// Map map = new HashMap();
		Map map = new HashMap();
		boolean flag = true;

		try {
			dto = service.save(dto);
			File dir = new File(path + dto.getMemnum());
			dir.mkdir();

			String fname = mf.getOriginalFilename();
			String newpath = dir.getAbsolutePath() + "\\" + fname;
			File newFile = new File(newpath);

			mf.transferTo(newFile);

			dto.setImg(URLEncoder.encode(newpath, "utf-8"));
			OmemberDto result = service.save(dto);
			map.put("result", result);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}

		map.put("flag", flag);
		map.put("dto", dto);
		return map;
	}

	// 회원가입(이미지X)
	@PostMapping("/omem")
	public Map join(OmemberDto dto) {
		OmemberDto d = service.save(dto);
		Map map = new HashMap();
		map.put("d", dto);
		return map;
	}

	// 로그인
	@PostMapping("/login")
	public Map login(String id, String pwd) {
		Map map = new HashMap();
		boolean flag = false;
		OmemberDto dto = service.getById(id);
		if (dto != null && pwd.equals(dto.getPwd())) {
			String token = tokenprovider.generateJwtToken(dto);
			flag = true;

			map.put("memnum", dto.getMemnum());
			map.put("token", token);
		}
		map.put("flag", flag);
		return map;
	}

	// 수정(닉네임, 비밀번호)
	@PutMapping("")
	public Map edit(OmemberDto dto,@RequestParam(value = "mf", required = false) MultipartFile mf, @RequestHeader(name = "token", required = false) String token) {
		boolean flag = true;
		Map map = new HashMap();
		System.out.println("dto : " + dto);
		if (token != null) {
			try {
				int memnum = tokenprovider.getMemnumFromToken(token);
				if (memnum != dto.getMemnum()) {
					flag = false;
				}
			} catch (Exception e) {
				flag = false;
			}
		}
		if (flag) {
			OmemberDto old = service.getByMemnum(dto.getMemnum());
			old.setPwd(dto.getPwd());
			old.setNickname(dto.getNickname());
			
			try {
				if(mf != null) {
					//기존 이미지가 있는 경우 삭제
					String oldImgPath = old.getImg();
					if(oldImgPath != null && !oldImgPath.isEmpty()) {
						File oldImgFile = new File(URLDecoder.decode(oldImgPath, "utf-8"));
						oldImgFile.delete();
					}
					//새로운 이미지 저장
					File dir = new File(path + old.getMemnum());
					dir.mkdir();
					
					String fname = mf.getOriginalFilename();
					String newpath = dir.getAbsolutePath() + "\\" + fname;
					File newFile = new File(newpath);
					
					mf.transferTo(newFile);
					
					old.setImg(URLEncoder.encode(newpath, "utf-8"));
				}
				OmemberDto updateDto = service.save(old);
				map.put("dto", updateDto);
			}catch(Exception e) {
				e.printStackTrace();
				flag = false;
			}
		}
		map.put("flag", flag);
		return map;
	}

	// 탈퇴
	@DeleteMapping("/{memnum}")
	public Map del(@PathVariable("memnum") int memnum, @RequestHeader(name = "token", required = false) String token) {
		boolean flag = true;
		Map map = new HashMap();
		if (token != null) {
			try {
				int tempMemnum = tokenprovider.getMemnumFromToken(token);
				if (memnum != tempMemnum) {
					flag = false;
				}
			} catch (Exception e) {
				flag = false;
			}
		}
		if (flag) {
			service.delMember(memnum);
		}
		map.put("flag", flag);
		return map;
	}

	// 이미지파일 읽어오기
	@GetMapping("/imgs/{memnum}")
	public ResponseEntity<byte[]> readImg(@PathVariable("memnum") int memnum) {
		String fname = "";
		OmemberDto dto = service.getByMemnum(memnum);
		fname = dto.getImg();

		ResponseEntity<byte[]> result = null;
		try {
			if (fname != null && fname.length() != 0) {
				fname = URLDecoder.decode(fname, "utf-8");
				File f = new File(fname);
				HttpHeaders header = new HttpHeaders();
				header.add("Content-Type", Files.probeContentType(f.toPath()));
				result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 내 정보 보기.
	@GetMapping("/{memnum}")
	public Map getInfo(@PathVariable("memnum") int memnum, @RequestHeader(name = "token", required = false) String token) {
		Map map = new HashMap();
		boolean flag = true;
		try {
			OmemberDto dto = service.getByMemnum(memnum);
			int tempnum = tokenprovider.getMemnumFromToken(token);
			map.put("dto", dto);
			map.put("tempnum", tempnum);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		System.out.println(map);
		return map;
	}

	// 중복체크
	@GetMapping("/check/{id}")
	public Map get(@PathVariable("id") String id) {
		Map map = new HashMap();
		OmemberDto dto = null;
		boolean tf = true;
		try {
			dto = service.getById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dto != null) {
			tf = false;
		}

		System.out.println("tf : " + tf);
		map.put("tf", tf);
		return map;
	}

	// email 중복 체크
	@ResponseBody
	@GetMapping("/email/{email}")
	public Map getByEmail(@PathVariable("email") String email) {
		Map map = new HashMap<>();
		OmemberDto dto = service.getByEmail(email);
		if (dto != null) {
			// 이메일 존재하면 보내는 키값
			map.put("exist", "이미 존재하는 이메일입니다.");
		} else {
			Random random = new Random(); // 난수 생성을 위한 랜덤 클래스
			String key = ""; // 인증번호

			SimpleMailMessage message = new SimpleMailMessage(); // 이메일 제목, 내용 작업 메서드
			message.setTo(email); // 스크립트에서 보낸 메일을 받을 사용자 이메일 주소
			// 입력 키를 위한 코드
			// 인증키 예시 = AB1234
			for (int i = 0; i < 2; i++) {
				int index = random.nextInt(26) + 65; // A~Z까지 랜덤 알파벳 생성.. 0~25 + 65 = 65~90 => char 변환.. 알파벳 생성
				key += (char) index;
			}
			for (int i = 0; i < 4; i++) {
				int numIndex = random.nextInt(10); // 6자리 랜덤 정수를 생성 .. 0~9
				key += numIndex;
			}
			String mail = "\n인증번호 입력창에 입력 후 가입을 완료해주세요.";
			message.setSubject("회원가입을 위한 이메일 인증번호 전송 메일입니다."); // 이메일 제목
			message.setText("인증번호는 " + key + " 입니다." + mail); // 이메일 내용
			try {
				javaMailSender.send(message); // 이메일 전송
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 인증키 전송
			map.put("key", key);
		}
		return map;

	}
	
	// 이메일로 아이디 찾기
	@GetMapping("/email/find/{email}")
	public Map findId(@PathVariable("email") String email){
		Map map = new HashMap();
		boolean flag = true;
		try {
			OmemberDto dto = service.getByEmail(email);
			map.put("dto", dto);
		}catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 아이디,이메일로 비밀번호 랜덤 변경
//	@ResponseBody
	@PostMapping("/findPwd/{email}")
	public Map findPwd(@RequestParam("id") String id, @PathVariable("email") String email) {
		Map map = new HashMap<>();
		
		OmemberDto d = service.getByEmail(email);
		if(d != null && d.getId().equals(id) && d.getEmail().equals(email)) {
			Random random = new Random(); // 난수생성을 위한 랜덤 클래스
			String key = ""; // 인증번호
			
			SimpleMailMessage message = new SimpleMailMessage(); //이메일 제목, 내용 작업 메서드
			message.setTo(email); // 스크립트에서 보낸 메일을 받을 사용자 이메일 주소
			
			// 입력 키를 위한 코드
			for(int i=0; i<2; i++) {
				int index = random.nextInt(26) + 65; // A~Z 까지 랜덤 알파벳 생성, 0~25 + 65 = 65~90 => char변환(알파벳 생성)
				key += (char)index;
			}
			for(int i=0; i<1; i++) {
				int speIndex = random.nextInt(15) + 33; //1자리 랜덤 특수문자 생성, 0~14 + 33 = 33 ~ 47 => char변환(특수문자 생성)
				key += (char)speIndex;
			}
			for(int i=0; i<4; i++) {
				int numIndex = random.nextInt(10); // 6자리 랜덤 정수를 생성, 0~9
				key += numIndex;
			}
			
			OmemberDto old = service.getByEmail(email);
			old.setPwd(key);
			
			OmemberDto randomPwd = service.save(old);
			
			String mail = "\n 임시 비밀번호로 로그인하신 후 원하시는 비밀번호로 수정해서 이용하시기 바랍니다. ";
			message.setSubject("옷짱에서 임시비밀번호를 알려드립니다."); // 메일제목
			message.setText("임시비밀번호는 " + key + " 입니다. " + mail); // 메일내용
			try {
				javaMailSender.send(message); // 메일전송
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			// 인증키 전송, 비밀번호 변경
			map.put("key", key);
			map.put("randomPwd", randomPwd);
		}else {
			map.put("msg", "아이디 혹은 이메일이 틀렸습니다.");
		}
		return map;
	}
}
