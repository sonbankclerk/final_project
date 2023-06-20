package com.example.demo.bookmark;

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
public class ObookmarkDto {

	private int bmnum;
	private int commnum;
	private Omember memnum;
}
