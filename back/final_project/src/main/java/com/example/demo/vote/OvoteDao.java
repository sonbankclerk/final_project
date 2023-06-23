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
	@Query(value = "select * from ovote where batnum = :batnum", nativeQuery = true)
	List<Ovote> countVote(@Param("batnum") int batnum);

	// 투표 유무 확인.
	@Transactional
	@Query(value = "select * from ovote where memnum = :memnum", nativeQuery = true)
	List<Ovote> chkVote(@Param("memnum") int memnum);

	@Transactional
	@Query(value = "select batnum "
				 + "from ("
				 		 + "select count(*) as countvote, batnum "
				 		 + "from ovote "
				 		 + "group by batnum "
				 		 + "order by countvote desc"
				 		 + ") "
		 		 + "where rownum = 1", nativeQuery = true)
	Integer findWinner();
}
