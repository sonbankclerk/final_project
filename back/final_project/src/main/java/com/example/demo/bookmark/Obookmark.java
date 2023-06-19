package com.example.demo.bookmark;

import org.hibernate.annotations.OnDelete;

import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.member.Omember;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Obookmark {

	@Id
	@SequenceGenerator(name="seq_obookmark", sequenceName="seq_obookmark", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_obookmark")
	private int bmnum;
	
	@ManyToOne
	@JoinColumn(name="commnum", nullable=false)  //fk 설정
	@OnDelete(action = OnDeleteAction.CASCADE)
	private int commnum;
	
	@ManyToOne
	@JoinColumn(name="memnum", nullable=false)  //fk 설정
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum;
}
