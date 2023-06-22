package com.example.demo.vote;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.battle.ObattleDto;
import com.example.demo.member.OmemberDto;


@Service
public class OvoteService {
	
	@Autowired
	private OvoteDao dao;
	
	// dto vo 변경하기
	private Object change(Object obj) {
		// 매개변수의 클래스를 확인한다.
		if(obj instanceof Ovote) {
			// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
			Ovote temp = (Ovote)obj;
			// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
			return new OvoteDto(temp.getVotenum(),temp.getMemnum(),temp.getBatnum());
		}else {
			// 반대로 한다.
			OvoteDto temp = (OvoteDto) obj;
			return new Ovote(temp.getVotenum(),temp.getMemnum(),temp.getBatnum());
		}
	}

	// ArrayList Vo -> Dto 로 변경하기
	private ArrayList<OvoteDto> changeList(ArrayList<Ovote> list){
		// 값을 넣은 후 리턴할 빈 dto list를 선언 및 생성한다.	
		ArrayList<OvoteDto> listDto = new ArrayList<>();
		for(Ovote vo : list) {
			// 입력 값으로 받은 vo list에 있는 vo들을 dto로 변환한다.
			OvoteDto dto = (OvoteDto)change(vo);
			// method가 실행될 때 선언했던 dto list에 변경된 dto들을 삽입한다.
			listDto.add(dto);
		}
		// 완성된 dto list를 리턴한다.
		return listDto;
	}
	
	
	// 투표 추가.
	public void save(OvoteDto dto) {
		Ovote vo = (Ovote)change(dto);
		dao.save(vo);
	}
	
	// 투표 수 count
	public int countVote(int batnum) {
		ArrayList<Ovote> list = (ArrayList<Ovote>) dao.countVote(batnum);
		return list.size();
	}
	
	// 투표가 끝난 후 모두 삭제.
	public void deleteAll() {
		dao.deleteAll();
	}
	
	// 투표 유무 확인
	public boolean chkVote(int memnum) {
		ArrayList<Ovote> list = (ArrayList<Ovote>)dao.chkVote(memnum);
		System.out.println("list : " + list);
		System.out.println("list's empty " + list.isEmpty());
		return list.isEmpty();
	}
}
