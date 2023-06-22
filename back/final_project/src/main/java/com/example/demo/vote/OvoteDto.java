package com.example.demo.vote;

import com.example.demo.battle.Obattle;
import com.example.demo.member.Omember;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OvoteDto {
	private int votenum;
	private Omember memnum;
	private Obattle batnum;
}
