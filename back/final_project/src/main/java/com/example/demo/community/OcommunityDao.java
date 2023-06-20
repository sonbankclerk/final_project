package com.example.demo.community;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.member.Omember;

@Repository
public interface OcommunityDao extends JpaRepository<Ocommunity, Integer> {
	//전체검색은 기본으로 적용
	
	//좋아요순 리스트 뿌리기(1~10)
	ArrayList<Ocommunity> findTop10ByOrderByBtnlikeDesc();
	
	//Omember - id 로 검색하는거
	ArrayList<Ocommunity> findByMemnum(Omember mb);
	
	//태그별 검색
	ArrayList<Ocommunity> findByTagLike(String tag);
	
	// 좋아요 및 버튼 up.
	@Query(value = "update ocommunity set :str = :str + 1 "
				 + "where commnum = :num", nativeQuery = true)
	void upBtn(String str, int num);
}
