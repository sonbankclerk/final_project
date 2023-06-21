package com.example.demo.ootw;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OootwDao extends JpaRepository<Oootw, Integer> {
	
	// 날짜 범위로 검색해서 리스트 뿌리기
//	@Transactional
//	@Modifying
//	@Query(value="select * from oootw where odate between :odate1 and :odate2", nativeQuery = true)
//	ArrayList<Oootw> findByOdateBetween(@Param("odate1") Date odate1, @Param("odate2") Date odate2);
	
	ArrayList<Oootw> findByOdateBetween(Date odate1, Date odate2);
	
}