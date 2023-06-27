package com.example.demo.ootw;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OootwDao extends JpaRepository<Oootw, Integer> {
	
	// 날짜 범위로 검색해서 리스트 뿌리기
	@Transactional
	@Modifying
	@Query(value="select * from oootw where odate between :odate1 and :odate2 order by odate desc", nativeQuery = true)
	ArrayList<Oootw> findByOdateBetween(@Param("odate1") String odate1, @Param("odate2") String odate2);
	
	// 최신 날짜순 전체 리스트 조회
	@Transactional
	@Modifying
	@Query(value="select * from oootw order by odate desc", nativeQuery = true)
	ArrayList<Oootw> findAllListByOrder();
}