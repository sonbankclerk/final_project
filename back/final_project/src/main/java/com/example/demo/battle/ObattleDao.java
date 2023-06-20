package com.example.demo.battle;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ObattleDao extends JpaRepository<Obattle, Integer>{
	
	// 투표 수 추가.
	@Query(value = "update obattle set vote = vote + 1"
				 + "where betnum = :num", nativeQuery = true)
	void upCnt(int num);

	// 우승자 찾기.
	@Query(value = "select * "
				 + "from (select * from obattle where winners = 0 order by vote desc)"
				 + "where rownum = 1", nativeQuery = true)
	Obattle findWinner();
	
	// 우승자 명예의 전당 올리기.
	@Query(value = "update obattle set winners = 1 "
				 + "where betnum = :num", nativeQuery = true)
	void changeWinner(int num);
	
	// 랜덤 두명 뽑기.
	@Query(value = "select * "
				 + "from (select *"
				 		+ "from obattle"
				 		+ "where winners = 0"
				 		+ "order by dbms_random.value)"
		 		 + "where rownum <= 2", nativeQuery =true)
	ArrayList<Obattle> findCandidates();

	// 랜덤 두명 확정 후 신청자들 삭제.
	@Query(value = "delete * "
			+ "from obattle"
			+ "where winners = 0 and betnum not in (:num1, :num2)", nativeQuery = true)
	void deleteNotCandidates(int num1, int num2);
	
	// 대결 후보(두명) 리스트.
	@Query(value = "select * "
				 + "from obattle"
				 + "where winners = 0", nativeQuery = true)
	ArrayList<Obattle> listCandidates();
	
	// 후보를 제외한 나머지 신청자들 리스트.
	@Query(value = "select * "
				 + "from obattle"
				 + "where winners = 0 and betnum not in (:num1, :num2)", nativeQuery = true)
	ArrayList<Obattle> listNotCandidates(int num1, int num2);
	
	// 랜덤 두명 중 패자 삭제.
	@Query(value = "delete * from obattle where winners = 0", nativeQuery = true)
	void deleteLoser();
	
	// 명예의 전당.
	@Query(value = "select *"
				 + "from obattle"
				 + "where winners = 1"
				 + "order by roundcnt desc", nativeQuery = true)
	ArrayList<Obattle> winnerList();
}
