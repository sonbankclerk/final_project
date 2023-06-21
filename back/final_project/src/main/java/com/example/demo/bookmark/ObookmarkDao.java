package com.example.demo.bookmark;


import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.member.Omember;


@Repository
public interface ObookmarkDao extends JpaRepository<Obookmark, Integer> {

	//Omember - memnum으로 검색
		ArrayList<Obookmark> findByMemnum(Omember memnum);

}
