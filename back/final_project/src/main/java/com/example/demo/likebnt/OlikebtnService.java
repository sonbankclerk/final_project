package com.example.demo.likebnt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.community.OcommunityDto;


@Service
public class OlikebtnService {
	@Autowired
	private OlikebtnDao dao;
	
	// (dto / vo) 변경하기
		private Object change(Object obj) {
			// 매개변수의 클래스를 확인한다.
			if (obj instanceof Olikebtn) {
				// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
				Olikebtn temp = (Olikebtn) obj;
				// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
				return new OlikebtnDto(temp.getLikenum(), temp.getCommnum(), temp.getMemnum());
						
			} else {
				// 반대로 한다.
				OlikebtnDto temp = (OlikebtnDto) obj;
				return new Olikebtn(temp.getLikebtn(), temp.getCommnum(), temp.getMemnum());
						
			}
		}


		// 등록
		public OlikebtnDto save(OlikebtnDto dto) {
			Olikebtn like = (Olikebtn) change(dto);
			OlikebtnDto result = (OlikebtnDto) change(dao.save(like));
			return result;
		}
		
		// 검색(좋아요를 눌렀는지 확인하기 위함)
		public OlikebtnDto getByMemnumAndCommnum(int memnum, int commnum) {
			Olikebtn entity = dao.findByMemnumAndCommnum(memnum, commnum);
			if(entity == null) {
				return null;
			}
			return (OlikebtnDto) change(entity);
		}
		
		// 삭제
		public void delOlikebtn(int likenum) {
			dao.deleteById(likenum);
		}
		
		
}
