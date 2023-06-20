package com.example.demo.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObookmarkDao extends JpaRepository<Obookmark, Integer> {

	
}
