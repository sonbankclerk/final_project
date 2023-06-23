package com.example.demo.battle;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.member.Omember;
import com.example.demo.member.OmemberDto;


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
			return new ObattleDto(temp.getBatnum(),temp.getMemnum(),temp.getImg(),temp.getTheme(),temp.getGender(),temp.getRoundcnt(),temp.isWinners());
		}else {
			// 반대로 한다.
			ObattleDto temp = (ObattleDto) obj;
			return new Obattle(temp.getBatnum(),temp.getMemnum(),temp.getImg(),temp.getTheme(),temp.getGender(),temp.getRoundcnt(),temp.isWinners());
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
	public void updateTheme(String theme, long memnum, OmemberDto dto) {
		
		// 변경될 라운드 수 +1 계산 후 저장.
		int roundcnt = upRoundCnt();
		System.out.println("theme : " + theme);
		System.out.println("roundcnt : " + roundcnt);
		
		Obattle battle = dao.findByMemnum((int)memnum);
	
		// 메니저의 battleDto를 찾고 없으면 만들고
		if(battle == null) {
			Omember memberVo = new Omember();
			memberVo.setEmail(dto.getEmail());
			memberVo.setGender(dto.getGender());
			memberVo.setId(dto.getId());
			memberVo.setImg(dto.getImg());
			memberVo.setMemnum(dto.getMemnum());
			memberVo.setNickname(dto.getNickname());
			memberVo.setPwd(dto.getPwd());
			
			Obattle battleVo = new Obattle();
			battleVo.setGender(memberVo.getGender());
			battleVo.setImg(memberVo.getImg());
			battleVo.setMemnum(memberVo);
			battleVo.setRoundcnt(roundcnt);
			battleVo.setTheme(theme);
			battleVo.setWinners(false);
			
			dao.save(battleVo);
		}else {
			// 있으면 변경하여 저장한다.
			battle.setTheme(theme);
			battle.setRoundcnt(roundcnt);
			dao.save(battle);
		}
	}
	
	// 투표될 후보 추가.
	public ObattleDto save(ObattleDto dto) {
		Obattle vo = (Obattle)change(dto);
		ObattleDto result = (ObattleDto)change(dao.save(vo));
		return result;
	}
	
	// 변경될 라운드 수 알려주기.(or) 현재 라운드 수 알려주기.
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
		return upRoundCnt();
	}
	
	// 투표 후보들 두명 뽑기 ( 미확정 )
	public ArrayList<ObattleDto> findCandidates(int memnum){
		// 랜덤으로 두명 뽑기.
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.findCandidates(memnum);
		return changeList(list);
	}
	
	// 투표 후보 두명 보여주기 ( 확정된 후 )
	public ArrayList<ObattleDto> listCandidates(int memnum){
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.listCandidates(memnum);
		return changeList(list);
	}
	
	// 투표 완료 후 winner 뽑기.
	public ObattleDto findWinner(int batnum) {
		// winner 명예의 전당에 올리기.
		Obattle vo = dao.findById(batnum).orElse(null);
		
		// 투표의 승자가 없거나 데이터베이스 오류로 나오지 않을 경우.
		if(vo == null) {
			// 명예의 전당에서 제일 최신에 있는 사람을 뽑는다.
			ArrayList<ObattleDto> list = winnerList();
			
			// list가 null이면 null을 그렇지 않으면 최시에 있는 사람을 return 한다.
			if(list == null || list.isEmpty()) {
				return null;
			}else {
				return list.get(0);
			}
		}
		
		// 오류가 없으면 명예의 전당으로 올리기.
		vo.setWinners(true);
		dao.save(vo);
		
		return (ObattleDto)change(vo);
	}
	
	// 데이터베이스 패자 삭제
	public void deleteLoser(int memnum) {
		dao.deleteLoser(memnum);
	}


	// 랜덤으로 뽑은 두 후보가 확정된 후 나머지 신청자들 삭제.
	public void deleteNotCandidates(long num1, long num2, long managerMemnum) {
		dao.deleteNotCandidates(num1, num2, managerMemnum);
	}
	
	// 명예의 전당 리스트 뽑기.
	public ArrayList<ObattleDto> winnerList(){
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.winnerList();
		return changeList(list);
	}
	
	// dto 한명 보여주기 (manager)
	public ObattleDto findByMemnum(int num) {
		Obattle vo = dao.findByMemnum(num);
		System.out.println(vo);
		if(vo == null) {
			return null;
		}else {
			return (ObattleDto)change(vo);
		}
	}

	// dto 한명 보여주기 (img용)
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
	
	// wincount 세기.
	public int winCount(int memnum) {
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.winCount(memnum);
		return list.size();
	}
	
	// 신청 유무 확인하기.
	// 신청 유무 확인용.
	public boolean chkApply(int memnum) {
		ArrayList<Obattle> list = (ArrayList<Obattle>)dao.chkApply(memnum);
		// true 신청 가능, false 이미 신청해서 신청 불가능.
		return list.isEmpty();
	}
}
