package com.example.demo.ootw;

import java.util.Date;

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
public class OootwDto {
	private int ootwnum;
	private Omember memnum;
	private Date odate;
	private String weather;
	private double temp;
	private String comments;
}