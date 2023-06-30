package com.example.demo.member;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OmemberService {
	@Autowired
	private OmemberDao dao;
	
	// dto vo 변경하기
	private Object change(Object obj) {
			// 매개변수의 클래스를 확인한다.
			if(obj instanceof Omember) {
				// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
				Omember temp = (Omember)obj;
				// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
				return new OmemberDto(temp.getMemnum(),temp.getId(),temp.getPwd(),temp.getEmail(),temp.getGender(),temp.getNickname(),temp.getImg());
			}else {
				// 반대로 한다.
				OmemberDto temp = (OmemberDto) obj;
				return new Omember(temp.getMemnum(),temp.getId(),temp.getPwd(),temp.getEmail(),temp.getGender(),temp.getNickname(),temp.getImg());
			}
		}
		

		// ArrayList Vo -> Dto 로 변경하기
		private ArrayList<OmemberDto> changeList(ArrayList<Omember> list){
			// 값을 넣은 후 리턴할 빈 dto list를 선언 및 생성한다.	
			ArrayList<OmemberDto> listDto = new ArrayList<>();
			for(Omember vo : list) {
				// 입력 값으로 받은 vo list에 있는 vo들을 dto로 변환한다.
				OmemberDto dto = (OmemberDto)change(vo);
				// method가 실행될 때 선언했던 dto list에 변경된 dto들을 삽입한다.
				listDto.add(dto);
			}
			// 완성된 dto list를 리턴한다.
			return listDto;
		}
	
	
	// 회원가입, 내정보수정
	public OmemberDto save(OmemberDto dto) {
		Omember entity = dao.save((Omember)change(dto));
		return (OmemberDto)change(entity);
	}
	
	// 탈퇴
	public void delMember(int memnum) {
		dao.deleteById(memnum);
	}
	
	// 로그인, 아이디 유무 검색
	public OmemberDto getById(String id) {
		Omember m = dao.findById(id);
		if(m==null) {
			return null;
		}
		return (OmemberDto)change(m);
	}
	
	// 회원번호로 회원 조회
	public OmemberDto getByMemnum(int memnum) {
		Omember m = dao.findById(memnum).orElse(null);
		if(m == null) {
			return null;
		}
		return (OmemberDto)change(m);
	}
	
	// 이메일로 조회
	public OmemberDto getByEmail(String email) {
		Omember m = dao.findByEmail(email);
		if(m == null) {
			return null;
		}
		return (OmemberDto)change(m);
	}	
}
