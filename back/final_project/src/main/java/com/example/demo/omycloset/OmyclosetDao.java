package com.example.demo.omycloset;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OmyclosetDao extends JpaRepository<Omycloset, Integer> {
	
	// 대분류 카테고리(1, 2, 3, 4, 5) 리스트 검색하기
	ArrayList<Omycloset> findByMaintag(String maintag);
	
	// 소분류 카테고리(11, 12, 13, 14..., 51, 52, 53, 54, 55...)리스트 검색하기
	ArrayList<Omycloset> findBySubtag(String subtag);
	
	// 옷 이름 키워드 리스트 검색하기
	ArrayList<Omycloset> findByClothLike(String cloth);
	
	// 즐겨찾기 on.. pk받아와서 0 => 1로 수정
	@Transactional
	@Modifying
	@Query(value="update omycloset set favorite=1 where num=:num", nativeQuery=true)
	void updateFavoriteOn(@Param("num") int closetnum);
	
	// 즐겨찾기 off.. pk받아와서 1 => 0으로 수정
	@Transactional
	@Modifying
	@Query(value="update omycloset set favorite=0 where num=:num", nativeQuery=true)
	void updateFavoriteOff(@Param("num") int closetnum);
	
}