 package com.example.demo.report;

import org.hibernate.annotations.OnDelete;

import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.community.Ocommunity;
import com.example.demo.member.Omember;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oreport { //신고
	
	@Id
	@SequenceGenerator(name="seq_oreport", sequenceName="seq_oreport", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_oreport")
	private int repnum;
	
	@ManyToOne
	@JoinColumn(name="memnum", nullable=false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum;
	
	@ManyToOne
	@JoinColumn(name="commnum", nullable=false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Ocommunity commnum;
}
