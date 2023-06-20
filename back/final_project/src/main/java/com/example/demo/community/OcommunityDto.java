package com.example.demo.community;

import org.springframework.web.multipart.MultipartFile;

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
	private int btnlike;
	private int btngood;
	private int btnwhat;
	private int btnsad;
	
	private String img1;
	private String img2;
	private String img3;
	
}
