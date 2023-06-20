package com.example.demo.ootw;


import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.member.Omember;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Oootw {
	@Id
	@SequenceGenerator(name="seq_ootw", sequenceName="seq_ootw", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_ootw")
	private int ootwnum;
	@ManyToOne
	@JoinColumn(name="memnum", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum;
	// entity 생성시 Date type은 'timestamp'가 default..
	// 날짜 db에 넣을 때 타입 안맞으면 400에러 뜸
	// vue에서 type date로 넣으면 2023-06-17 이런식으로 뜨는데 axios는 2023/06/17 형식 아니면 타입 안맞다고 판단함
	// 타입 최대한 맞추기 위해 컬럼타입 date로 지정하고, vue에서 substring으로 날짜 잘라서 '/' 넣어주고 axios로 넘겨줘야 함
	@Column(columnDefinition = "date")
	private Date odate;
	private String weather;
	private double temp;
	@Column(nullable = true)
	private String comments;
}
