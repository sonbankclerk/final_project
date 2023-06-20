package com.example.demo.ootw;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OootwDao extends JpaRepository<Oootw, Integer> {
	
	// 날짜 범위로 검색해서 리스트 뿌리기
	ArrayList<Oootw> findByDateBetween(Date odate1, Date odate2);
	
}