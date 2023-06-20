package com.example.demo.community;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.member.Omember;

@Repository
public interface OcommunityDao extends JpaRepository<Ocommunity, Integer> {
	
	//좋아요순 리스트 뿌리기(1~10)
	ArrayList<Ocommunity> findTop10ByOrderByBtnlikeDesc();
	
	//Omember - memnum으로 검색
	ArrayList<Ocommunity> findByMemnum(Omember mb);
	
	//태그별 검색
	ArrayList<Ocommunity> findByTagLike(String tag);
	
	// 좋아요 up
	@Query(value = "update ocommunity set btnlike = btnlike + 1 "
				+ "where commnum = :commnum", nativeQuery = true)
	void upBtn(@Param("commnum") int commnum);
	
	// 좋아요 down.
	@Query(value = "update ocommunity set btnlike = btnlike - 1 "
				+ "where commnum = :commnum", nativeQuery = true)
	void downBtn(@Param("commnum") int commnum);
}
