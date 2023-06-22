package com.example.demo.vote;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OvoteDao extends JpaRepository<Ovote, Integer>{

	
	// 갯수 세기.
	@Transactional
	@Query(value = "select * from vote where batnum = :batnum")
	List<Ovote> countVote(@Param("batnum") int batnum);

	@Transactional
	@Query(value = "select * from vote where memnum = :memnum")
	List<Ovote> chkVote(@Param("memnum") int memnum);

}
