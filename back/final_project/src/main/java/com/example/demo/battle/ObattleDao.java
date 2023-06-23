package com.example.demo.battle;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ObattleDao extends JpaRepository<Obattle, Integer>{
	
	// 신청자 올리기 전에 roundcnt올리기위한 max(roundcnt)찾기
	@Transactional
	@Query(value = "select * "
				 + "from obattle "
				 + "where roundcnt = (select max(roundcnt) "
				 					+ "from obattle "
				 					+ "where winners = 1)", nativeQuery = true)
	List<Obattle> findMaxRoundcnt();
	
	// 메니저의 battleDto 확인하기.
	@Transactional
	@Query(value = "select * from obattle where memnum = :memnum", nativeQuery = true)
	Obattle findByMemnum(@Param(value = "memnum")int memnum);
	
	// 랜덤 두명 뽑기.
	@Transactional
	@Query(value = "select * "
				 + "from (select * "
				 		+ "from obattle "
				 		+ "where winners = 0 and memnum != :memnum "
				 		+ "order by dbms_random.value) "
		 		 + "where rownum <= 2", nativeQuery =true)
	List<Obattle> findCandidates(@Param("memnum") int memnum);

	// 랜덤 두명 확정 후 신청자들 삭제.
	@Transactional
	@Modifying
	@Query(value = "delete obattle where winners = 0 and batnum not in (:num1, :num2) and memnum != :memnum", nativeQuery = true)
	void deleteNotCandidates(@Param(value="num1") Long num1, @Param(value="num2") Long num2 , @Param(value = "memnum") Long memnum);
	
	// 대결 후보(두명) 리스트.
	@Transactional
	@Query(value = "select * "
				 + "from obattle "
				 + "where winners = 0 and memnum != :memnum", nativeQuery = true)
	List<Obattle> listCandidates(@Param("memnum") int memnum);
	
	// 후보를 제외한 나머지 신청자들 리스트.
	@Transactional
	@Query(value = "select * from obattle where winners = 0 and batnum not in (:num1, :num2)", nativeQuery = true)
	List<Obattle> listNotCandidates(@Param(value="num1") Long num1, @Param(value="num2") Long num2);
	
	// 랜덤 두명 중 패자 삭제.
	@Transactional
	@Modifying
	@Query(value = "delete obattle where winners = 0 and memnum != :memnum", nativeQuery = true)
	void deleteLoser(@Param(value = "memnum")int memnum);
	
	// 명예의 전당.
	@Transactional
	@Query(value = "select * "
				 + "from obattle "
				 + "where winners = 1 "
				 + "order by roundcnt desc", nativeQuery = true)
	List<Obattle> winnerList();

	// winCount 세기.
	@Transactional
	@Query(value = "select * from obattle where winners = 1 and memnum = :memnum", nativeQuery = true)
	List<Obattle> winCount(@Param(value = "memnum") int memnum);

	// 신청 유무 확인하기.
	@Transactional
	@Query(value = "select * from obattle where winners = 0 and memnum = :memnum", nativeQuery = true)
	List<Obattle> chkApply(@Param(value = "memnum") int memnum);
	
}
