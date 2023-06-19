package com.example.demo.member;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@DynamicInsert // default값 적용 어노테이션
@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Omember {
	@Id
	@SequenceGenerator(name="seq_member", sequenceName="seq_member", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_member")
	private int memnum;
	
	private String id;
	private String pwd;
	private String email;
	@Column(nullable=false)
	private String gender;
	@Column(nullable=false)
	private String nickname;
	@Column(nullable=true)
	private String img;
	
	@ColumnDefault("0") //default값 설정
	private int wincnt;
}
