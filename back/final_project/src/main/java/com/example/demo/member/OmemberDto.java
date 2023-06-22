package com.example.demo.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OmemberDto {
	private int memnum;
	private String id;
	private String pwd;
	private String email;
	private String gender;
	private String nickname;
	private String img;
//	private int wincnt;
}
