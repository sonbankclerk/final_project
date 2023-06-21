package com.example.demo.battle;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObattleService {
	
	@Autowired
	private ObattleDao dao;
	
	// dto vo 변경하기
	private Object change(Object obj) {
		// 매개변수의 클래스를 확인한다.
		if(obj instanceof Obattle) {
			// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
			Obattle temp = (Obattle)obj;
			// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
			return new ObattleDto(temp.getBatnum(),temp.getMemnum(),temp.getVote(),temp.getImg(),temp.getTheme(),temp.getGender(),temp.getRoundcnt(),temp.isWinners());
		}else {
			// 반대로 한다.
			ObattleDto temp = (ObattleDto) obj;
			return new Obattle(temp.getBatnum(),temp.getMemnum(),temp.getVote(),temp.getImg(),temp.getTheme(),temp.getGender(),temp.getRoundcnt(),temp.isWinners());
		}
	}

	// ArrayList Vo -> Dto 로 변경하기
	private ArrayList<ObattleDto> changeList(ArrayList<Obattle> list){
		// 값을 넣은 후 리턴할 빈 dto list를 선언 및 생성한다.	
		ArrayList<ObattleDto> listDto = new ArrayList<>();
		for(Obattle vo : list) {
			// 입력 값으로 받은 vo list에 있는 vo들을 dto로 변환한다.
			ObattleDto dto = (ObattleDto)change(vo);
			// method가 실행될 때 선언했던 dto list에 변경된 dto들을 삽입한다.
			listDto.add(dto);
		}
		// 완성된 dto list를 리턴한다.
		return listDto;
	}
	
	
	// --------------------- * service start * -----------------------------------

	// 관계자가 테마 바꾸기.
	public void updateTheme(String theme) {
		int roundcnt = upRoundCnt();
		System.out.println("theme : " + theme);
		System.out.println("roundcnt : " + roundcnt);
		dao.updateTheme(theme,(long)roundcnt);
	}
	
	// 투표될 후보 추가.
	public ObattleDto save(ObattleDto dto) {
		Obattle vo = (Obattle)change(dto);
		ObattleDto result = (ObattleDto)change(dao.save(vo));
		return result;
	}
	
	// 변경될 라운드 수 알려주기.
	private int upRoundCnt() {
		// 최대 라운드 수 찾고 + 1 하기
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.findMaxRoundcnt();
		System.out.println(list);
		if(list == null || list.size() == 0) {
			return 1;
		}else {
			int roundcnt = list.get(0).getRoundcnt() + 1;
			return roundcnt;
		}
	}
	
	// 현재 라운드 수 확인하기.
	public int findRoundCnt() {
		Obattle vo = dao.findById(1).orElse(null);
		return vo.getRoundcnt();
	}
	
	// 투표 시 vote = vote + 1 
	public void upCnt(long num) {
		dao.upCnt(num);
	}
	
	// 투표 후보들 두명 뽑기 ( 미확정 )
	public ArrayList<ObattleDto> findCandidates(){
		// 랜덤으로 두명 뽑기.
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.findCandidates();
		return changeList(list);
	}
	
	// 투표 후보 두명 보여주기 ( 확정된 후 )
	public ArrayList<ObattleDto> listCandidates(){
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.listCandidates();
		return changeList(list);
	}
	
	// 투표 완료 후 winner 뽑기.
	public ObattleDto findWinner() {
		// winner 뽑고
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.findWinner();
		ObattleDto dto = null;
		if(list.size() != 0) {
			dto = (ObattleDto)change(list.get(0));
			// winner 명예의 전당에 올리기.
			dao.changeWinner((long)dto.getBatnum());
			// 패자 삭제
			dao.deleteLoser();
			System.out.println("listSize : " + "not 0");
		}
		list = (ArrayList<Obattle>)dao.winnerList();
		dto = (ObattleDto)change(list.get(0));
		System.out.println("listSize : " + "0");
		System.out.println("list : " + list);
		return dto;
	}

	// 랜덤으로 뽑은 두 후보가 확정된 후 나머지 신청자들 삭제.
	public void deleteNotCandidates(long num1, long num2) {
		dao.deleteNotCandidates(num1, num2);
	}
	
	// 명예의 전당 리스트 뽑기.
	public ArrayList<ObattleDto> winnerList(){
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.winnerList();
		return changeList(list);
	}
	
	// dto 한명 보여주기
	public ObattleDto findById(int num) {
		Obattle vo = dao.findById(num).orElse(null);
		System.out.println(vo);
		if(vo == null) {
			return null;
		}else {
			return (ObattleDto)change(vo);
		}
	}
	
	// 후보 두명을 제외한 나머지 신청자들 목록
	// 파일 삭제용.
	public ArrayList<ObattleDto> listNotCandidates(long num1, long num2){
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.listNotCandidates(num1, num2);
		return changeList(list);
	}
}
