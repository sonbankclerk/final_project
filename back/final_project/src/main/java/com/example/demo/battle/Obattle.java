package com.example.demo.battle;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.member.Omember;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Obattle {
		// 배틀 신청 기본키
		@Id
		@SequenceGenerator(name = "seq_obattle", sequenceName = "seq_obattle")
		@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "seq_obattleapplication")
		private int batnum;
		
		// 외래키(멤버)
		@ManyToOne
		@JoinColumn(name = "memnum", nullable = false)
		@OnDelete(action = OnDeleteAction.CASCADE)
		private Omember memnum;
		
		// 배틀 신청 사진
		private String img;
		// 배틀 테마(주제)
		private String theme;
		// 배틀 성별
		private String gender;
		// 우승 회차
		private int roundcnt;
		// 명예의 전당 유무
		private boolean winners;
		
		@PrePersist
		private void defaultVote() {
			this.winners = false;
		}
}
