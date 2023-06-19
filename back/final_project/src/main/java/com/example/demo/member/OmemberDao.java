package com.example.demo.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OmemberDao extends JpaRepository<Omember, Integer> {
	Omember findById(String id);
	void findByPwd(String pwd);
}
