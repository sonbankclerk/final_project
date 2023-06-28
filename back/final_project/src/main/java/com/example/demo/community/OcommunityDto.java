package com.example.demo.community;


import com.example.demo.member.Omember;

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
public class OcommunityDto {
	
	private int commnum;
	private Omember memnum;
	private String tag;
	
	
	
//	private int btngood;
//	private int btnwhat;
//	private int btnsad;
	
	private String img1;
	private String img2;
	private String img3;
	private int btnlike;
	//private MultipartFile[] f = new MultipartFile[3];
	
}
