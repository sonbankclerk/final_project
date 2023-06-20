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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ocommunity {
	
	@Id
	@SequenceGenerator(name="seq_ocommunity", sequenceName="seq_ocommunity", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_ocommunity")
	private int commnum; // 게시글 번호
	
	@ManyToOne
	@JoinColumn(name="memnum", nullable=false) // nullable=false -> not null
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum; // fk: Omember 멤버번호
	
	@Column(nullable = false)
	private String tag;
	
	
	//기능 2개로 줄이는거 얘기하기 (긍정 1개 / 부정 1개)
	private int btnlike; //좋아요
	//private int btngood; //멋져요
	//private int btnwhat; //이게뭐에요?
	private int btnsad; //아쉬워요
	
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
		//btngood = 0; 
		//btnwhat = 0;
		btnsad = 0;
	}
}
