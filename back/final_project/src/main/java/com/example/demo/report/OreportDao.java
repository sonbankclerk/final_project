package com.example.demo.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OreportDao extends JpaRepository<Oreport, Integer> {

	
}
