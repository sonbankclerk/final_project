package com.example.demo.battle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ObattleDao extends JpaRepository<Obattle, Integer>{
	
	// 관계자가 테마 바꾸기.
	// 관계자 roundcnt 바꾸기.
	// 현재 관계자의 roundcnt가 곧 현재의 roundcnt이다.
	@Transactional
	@Query(value = "update obattle set theme = :theme, roundcnt = :roundcnt "
				 + "where batnum = 1", nativeQuery = true)
	void updateTheme(@Param(value="theme") String theme, @Param(value = "roundcnt") int roundcnt);

	// 신청자 올리기 전에 roundcnt올리기위한 max(roundcnt)찾기
	@Transactional
	@Query(value = "select * "
				 + "from obattle "
				 + "where roundcnt = (select max(roundcnt) "
				 					+ "from obattle "
				 					+ "where winners = 1)", nativeQuery = true)
	List<Obattle> findMaxRoundcnt();
	
	// 투표 수 추가.
	@Transactional
	@Modifying
	@Query(value = "update obattle set vote = vote + 1 "
				 + "where batnum = :num", nativeQuery = true)
	void upCnt(@Param(value="num") int num);

	// 우승자 찾기.
	@Transactional
	@Query(value = "select * "
				 + "from (select * from obattle where winners = 0 order by vote desc) "
				 + "where rownum = 1", nativeQuery = true)
	List<Obattle> findWinner();
	
	// 우승자 명예의 전당 올리기.
	@Transactional
	@Modifying
	@Query(value = "update obattle set winners = 1 "
				 + "where batnum = :batnum", nativeQuery = true)
	void changeWinner(@Param("batnum") int batnum);
	
	// 랜덤 두명 뽑기.
	@Transactional
	@Query(value = "select * "
				 + "from (select * "
				 		+ "from obattle "
				 		+ "where winners = 0 "
				 		+ "order by dbms_random.value) "
		 		 + "where rownum <= 2", nativeQuery =true)
	List<Obattle> findCandidates();

	// 랜덤 두명 확정 후 신청자들 삭제.
	@Transactional
	@Query(value = "delete * "
			+ "from obattle "
			+ "where winners = 0 and batnum not in (:num1, :num2)", nativeQuery = true)
	void deleteNotCandidates(@Param(value="num1") int num1, @Param(value="num2") int num2);
	
	// 대결 후보(두명) 리스트.
	@Transactional
	@Query(value = "select * "
				 + "from obattle "
				 + "where winners = 0", nativeQuery = true)
	List<Obattle> listCandidates();
	
	// 후보를 제외한 나머지 신청자들 리스트.
	@Transactional
	@Query(value = "select * "
				 + "from obattle "
				 + "where winners = 0 and batnum not in (:num1, :num2)", nativeQuery = true)
	List<Obattle> listNotCandidates(@Param(value="num1") int num1, @Param(value="num2")int num2);
	
	// 랜덤 두명 중 패자 삭제.
	@Transactional
	@Query(value = "delete * from obattle where winners = 0", nativeQuery = true)
	void deleteLoser();
	
	// 명예의 전당.
	@Transactional
	@Query(value = "select * "
				 + "from obattle "
				 + "where winners = 1 "
				 + "order by roundcnt desc", nativeQuery = true)
	List<Obattle> winnerList();
}
