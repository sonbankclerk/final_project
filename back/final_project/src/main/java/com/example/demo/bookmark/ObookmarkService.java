package com.example.demo.bookmark;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.report.Oreport;
import com.example.demo.report.OreportDto;


@Service
public class ObookmarkService {
	@Autowired
	private ObookmarkDao dao;
	
	// dto vo 변경하기
		private Object change(Object obj) {
			// 매개변수의 클래스를 확인한다.
			if(obj instanceof Oreport) {
				// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
				Obookmark temp = (Obookmark)obj;
				// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
				return new ObookmarkDto(temp.getBmnum(),temp.getCommnum(),temp.getMemnum());
			}else {
				// 반대로 한다.
				ObookmarkDto temp = (ObookmarkDto) obj;
				return new Obookmark(temp.getBmnum(),temp.getCommnum(),temp.getMemnum());
			}
		}
		

		// ArrayList Vo -> Dto 로 변경하기
		private ArrayList<ObookmarkDto> changeList(ArrayList<Obookmark> list){
			// 값을 넣은 후 리턴할 빈 dto list를 선언 및 생성한다.	
			ArrayList<ObookmarkDto> listDto = new ArrayList<>();
			for(Obookmark vo : list) {
				// 입력 값으로 받은 vo list에 있는 vo들을 dto로 변환한다.
				ObookmarkDto dto = (ObookmarkDto)change(vo);
				// method가 실행될 때 선언했던 dto list에 변경된 dto들을 삽입한다.
				listDto.add(dto);
			}
			// 완성된 dto list를 리턴한다.
			return listDto;
		}
		
		//북마크(즐겨찾기)
		public int save(ObookmarkDto dto) {
			Obookmark bm = (Obookmark)change(dto);
			return bm.getBmnum();
		}
		
		//북마크 전체검색
		public ArrayList<ObookmarkDto> getAll(){
			ArrayList<Obookmark> list = (ArrayList<Obookmark>) dao.findAll();
			ArrayList<ObookmarkDto> list2 = changeList(list);
			return list2;
		}
		
		//북마크 삭제
		public void delOreport(int bmnum) {
			dao.deleteById(bmnum);
		}
}
