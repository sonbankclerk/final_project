package com.example.demo.likebnt;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.community.Ocommunity;
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
@NoArgsConstructor
@AllArgsConstructor
public class Olikebtn {
	
	@Id
	@SequenceGenerator(name="seq_olikebtn", sequenceName="seq_olikebtn", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_olikebtn")
	private int likenum;
	
	@ManyToOne
	@JoinColumn(name="commnum", nullable=false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Ocommunity commnum;
	
	@ManyToOne
	@JoinColumn(name="memnum", nullable=false) 
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum;
}
