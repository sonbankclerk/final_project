package com.example.demo.likebnt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OlikebtnDao extends JpaRepository<Olikebtn, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "select * from Olikebtn where memnum =: memnum and commnum =: commnum", nativeQuery = true)
	Olikebtn findByMemnumAndCommnum(@Param("memnum") int memnum, @Param("commnum") int commnum);
	//좋아요 검색(memnum&commnum) 받아서 좋아요를 눌렀었는지 확인하기 위함.
	
	@Query(value = "select count(*)"
				 + "from olikebtn"
				 + "where commnum = :commnum", nativeQuery = true)
	List<Integer> likeCount(int commnum);
}
