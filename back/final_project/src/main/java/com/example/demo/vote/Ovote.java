package com.example.demo.vote;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.battle.Obattle;
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
public class Ovote {
	// 기본키
	@Id
	@SequenceGenerator(name = "seq_vote", sequenceName = "seq_vote")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vote")
	private int votenum;
	
	// 외래키(멤버)
	@ManyToOne
	@JoinColumn(name = "memnum", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum;
	
	// 외래키(배틀신청)
	@ManyToOne
	@JoinColumn(name = "batnum", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Obattle batnum;
}
