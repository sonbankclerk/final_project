package com.example.demo.report;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OreportService {
	@Autowired
	private OreportDao dao;
	
	// dto vo 변경하기
	private Object change(Object obj) {
		// 매개변수의 클래스를 확인한다.
		if(obj instanceof Oreport) {
			// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
			Oreport temp = (Oreport)obj;
			// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
			return new OreportDto(temp.getRepnum(),temp.getMemnum(),temp.getCommnum());
		}else {
			// 반대로 한다.
			OreportDto temp = (OreportDto) obj;
			return new Oreport(temp.getRepnum(),temp.getMemnum(),temp.getCommnum());
		}
	}
	

	// ArrayList Vo -> Dto 로 변경하기
	private ArrayList<OreportDto> changeList(ArrayList<Oreport> list){
		// 값을 넣은 후 리턴할 빈 dto list를 선언 및 생성한다.	
		ArrayList<OreportDto> listDto = new ArrayList<>();
		for(Oreport vo : list) {
			// 입력 값으로 받은 vo list에 있는 vo들을 dto로 변환한다.
			OreportDto dto = (OreportDto)change(vo);
			// method가 실행될 때 선언했던 dto list에 변경된 dto들을 삽입한다.
			listDto.add(dto);
		}
		// 완성된 dto list를 리턴한다.
		return listDto;
	}
	
	//신고 접수
	public OreportDto save(OreportDto dto) {
		Oreport rp = (Oreport)change(dto);
		OreportDto result = (OreportDto) change(dao.save(rp));
		return result;
	}
	
	//신고 접수 된 리스트 전체검색
	public ArrayList<OreportDto> getAll(){
		ArrayList<Oreport> list = (ArrayList<Oreport>) dao.findAll();
		ArrayList<OreportDto> list2 = changeList(list);
		return list2;
	}
	
	//신고 삭제
	public void delOreport(int repnum) {
		dao.deleteById(repnum);
	}
}
