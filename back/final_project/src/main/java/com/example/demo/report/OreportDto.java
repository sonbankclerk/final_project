package com.example.demo.report;

import com.example.demo.community.Ocommunity;
import com.example.demo.member.Omember;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OreportDto {
	
	private int repnum;
	private Omember memnum;
	private Ocommunity commnum;
	
}
