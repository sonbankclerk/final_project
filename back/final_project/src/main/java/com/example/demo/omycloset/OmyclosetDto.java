package com.example.demo.omycloset;

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
public class OmyclosetDto {
	private int closetnum;
	private Omember memnum;
	private String cloth;
	private String img;
//	private MultipartFile f;
	private String maintag;
	private String subtag;
	private int favorite;
}