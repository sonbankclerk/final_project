package com.example.demo.likebnt;

import com.example.demo.community.Ocommunity;
import com.example.demo.member.Omember;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OlikebtnDto {

	private int likebtn;
	private Ocommunity commnum;
	private Omember memnum;

}
