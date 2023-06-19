package com.example.demo.community;

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
import jakarta.persistence.PrePersist;
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
public class Ocommunity {
	
	@Id
	@SequenceGenerator(name="seq_ocommunity", sequenceName="seq_ocommunity", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_ocommunity")
	private int commnum;
	
	@ManyToOne
	@JoinColumn(name="memnum", nullable=false) // nullable=false -> not null
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum; // fk: Omember
	
	@Column(nullable = false)
	private String tag;
	
	private int btnlike;
	private int btngood;
	private int btnwhat;
	private int btnsad;
	
	//Ocommimgs 삭제하고 img1~3 추가
	@Column(nullable=true)
	private String img1;
	@Column(nullable=true)
	private String img2;
	@Column(nullable=true)
	private String img3;
	
	@PrePersist
	public void preprocess() { // 디폴트 값 : 0
		btnlike = 0;
		btngood = 0;
		btnwhat = 0;
		btnsad = 0;
	}
}
