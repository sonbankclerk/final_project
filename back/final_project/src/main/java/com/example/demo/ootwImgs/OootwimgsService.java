package com.example.demo.ootwImgs;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OootwimgsService {
	@Autowired
	private OootwimgsDao dao;
	
	// ootw에 등록되는 옷장 이미지 정보 저장
	// dao.save 왜 안먹히냐?
	public OootwimgsDto save(OootwimgsDto dto) {
		Oootwimgs entity = dao.save(new Oootwimgs(dto.getOotwimgsnum(), dto.getOotwnum(), dto.getClosetnum()));
		return new OootwimgsDto(entity.getOotwimgsnum(), entity.getOotwnum(), entity.getClosetnum());
	}
	
	// 게시글에 이미지 배열 담아서 뿌리기
	public ArrayList<OootwimgsDto> getMyImgs(int ootwnum) {
		ArrayList<Oootwimgs> list = (ArrayList<Oootwimgs>) dao.findByOotwnum(ootwnum);
		ArrayList<OootwimgsDto> list2 = new ArrayList<>();
		for(Oootwimgs o : list) {
			list2.add(new OootwimgsDto(o.getOotwimgsnum(), o.getOotwnum(), o.getClosetnum()));
		}
		return list2;
	}
	
	// 게시글에 등록된 옷장 이미지 정보 삭제하기(ootw num 받아서)
	public void delete(int ootwnum) {
		dao.deleteByFk(ootwnum); // fk(ootwnum) 받아서 삭제하기
	}
	
}