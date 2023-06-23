package com.example.demo.battle;

import com.example.demo.member.Omember;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObattleDto {
	// 배틀 신청 기본키
	private int batnum;
	// 외래키(멤버)
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
}
