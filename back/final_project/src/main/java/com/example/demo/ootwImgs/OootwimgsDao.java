package com.example.demo.ootwImgs;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.omycloset.Omycloset;
import com.example.demo.ootw.Oootw;

import jakarta.transaction.Transactional;

public interface OootwimgsDao extends JpaRepository<Oootwimgs, Integer> {
	
	// 왜때문에 save 자동생성 안되는거죠?
	Oootwimgs save(int ootwimgsnum, Oootw ootwnum, Omycloset closetnum);
	
	// ootw 게시글 번호로 싹 긁어와서 closet num 사용하기
	ArrayList<Oootwimgs> findByFk(int ootwnum);

	// pk값 아닌 컬럼 매개변수 받아서 삭제하기
	@Transactional
	@Modifying
	@Query(value="delete from oootwimgs where ootwnum=:ootwnum", nativeQuery = true)
	void deleteByFk(@Param("ootwnum") int ootwnum);
	
}