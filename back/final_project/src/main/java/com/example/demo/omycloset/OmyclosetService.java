package com.example.demo.omycloset;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.util.JSONPObject;

@Service
public class OmyclosetService {
	@Autowired
	private OmyclosetDao dao;
	
//	// dto vo 변경하기
//	private Object change(Object obj) {
//		// 매개변수의 클래스를 확인한다.
//		if (obj instanceof Omycloset) {
//			// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
//			Omycloset temp = (Omycloset) obj;
//			// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
//			return new OmyclosetDto(temp.getClosetnum(), temp.getMemnum(), temp.getCloth(), temp.getImg(),
//					null, temp.getMaintag(), temp.getSubtag(), temp.getFavorite());
//		} else {
//			// 반대로 한다.
//			OmyclosetDto temp = (OmyclosetDto) obj;
//			return new Omycloset(temp.getClosetnum(), temp.getMemnum(), temp.getCloth(), temp.getImg(),
//					temp.getMaintag(), temp.getSubtag(), temp.getFavorite());
//		}
//	}
//
//	// ArrayList Vo -> Dto 로 변경하기
//	private ArrayList<OmyclosetDto> changeList(ArrayList<Omycloset> list) {
//		// 값을 넣은 후 리턴할 빈 dto list를 선언 및 생성한다.
//		ArrayList<OmyclosetDto> listDto = new ArrayList<OmyclosetDto>();
//		for (Omycloset vo : list) {
//			// 입력 값으로 받은 vo list에 있는 vo들을 dto로 변환한다.
//			OmyclosetDto dto = (OmyclosetDto) change(vo);
//			// method가 실행될 때 선언했던 dto list에 변경된 dto들을 삽입한다.
//			listDto.add(dto);
//		}
//		// 완성된 dto list를 리턴한다.
//		return listDto;
//	}

	// 1. 내옷 등록
	// 2. 수정(옷이름 & 사진)
	public OmyclosetDto save(OmyclosetDto dto) {
		Omycloset entity = dao.save(new Omycloset(dto.getClosetnum(), dto.getMemnum(), dto.getCloth(), dto.getImg(),
				dto.getMaintag(), dto.getSubtag(), dto.getFavorite()));
		return new OmyclosetDto(entity.getClosetnum(), entity.getMemnum(), entity.getCloth(), entity.getImg(), null,
				entity.getMaintag(), entity.getSubtag(), entity.getFavorite());
//		return (OmyclosetDto)change(entity);
	}

	// 즐겨찾기 On
	public void updateFavoriteOn(int closetnum) {
		dao.updateFavoriteOn(closetnum);
	}

	// 즐겨찾기 Off
	public void updateFavoiteOff(int closetnum) {
		dao.updateFavoriteOff(closetnum);
	}
	
	// 내옷 검색(디테일 보여주기)
	public OmyclosetDto getMyCloth(int closetnum) {
		Omycloset entity = dao.findById(closetnum).orElse(null);
		if(entity == null) {
			return null;
		} else {
			return new OmyclosetDto(entity.getClosetnum(), entity.getMemnum(), entity.getCloth(), entity.getImg(), null,
					entity.getMaintag(), entity.getSubtag(), entity.getFavorite());
//			return (OmyclosetDto)change(entity);
		}
	}

	// 내옷 전체 리스트
	public ArrayList<OmyclosetDto> getAll() {
		ArrayList<Omycloset> list = (ArrayList<Omycloset>) dao.findAll();
		ArrayList<OmyclosetDto> list2 = new ArrayList<OmyclosetDto>();
		for (Omycloset o : list) {
			list2.add(new OmyclosetDto(o.getClosetnum(), o.getMemnum(), o.getCloth(), o.getImg(), null, o.getMaintag(),
					o.getSubtag(), o.getFavorite()));
		}
		return list2;
	}

	// 내옷 대분류 카테고리 검색
	public ArrayList<OmyclosetDto> getByMaintag(String maintag) {
		ArrayList<Omycloset> list = (ArrayList<Omycloset>) dao.findByMaintag(maintag);
		ArrayList<OmyclosetDto> list2 = new ArrayList<OmyclosetDto>();
		for(Omycloset o : list) {
			list2.add(new OmyclosetDto(o.getClosetnum(), o.getMemnum(), o.getCloth(), o.getImg(), null, o.getMaintag(),
					o.getSubtag(), o.getFavorite()));
		}
		return list2;
	}
	
	// 내옷 대분류 소분류 교집합 카테고리 검색
	public ArrayList<OmyclosetDto> getBySubtag(String subtag)	{
		ArrayList<Omycloset> list = (ArrayList<Omycloset>) dao.findBySubtag(subtag);
		ArrayList<OmyclosetDto> list2 = new ArrayList<OmyclosetDto>();
		for(Omycloset o : list) {
			list2.add(new OmyclosetDto(o.getClosetnum(), o.getMemnum(), o.getCloth(), o.getImg(), null, o.getMaintag(),
					o.getSubtag(), o.getFavorite()));
		}
		return list2;
	}

	// 내옷 키워드 검색 리스트
	public ArrayList<OmyclosetDto> getByCloth(String cloth){
		ArrayList<Omycloset> list = dao.findByClothLike("%" + cloth + "%");
		ArrayList<OmyclosetDto> list2 = new ArrayList<OmyclosetDto>();
		for(Omycloset o : list) {
			list2.add(new OmyclosetDto(o.getClosetnum(), o.getMemnum(), o.getCloth(), o.getImg(), null, o.getMaintag(),
					o.getSubtag(), o.getFavorite()));
		}
		return list2;
	}

	// 내옷 삭제
	public void delete(int closetnum) {
		dao.deleteById(closetnum);
	}

}