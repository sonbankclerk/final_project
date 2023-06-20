package com.example.demo.omycloset;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
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

@DynamicInsert
@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Omycloset {
	
	@Id
	@SequenceGenerator(name="seq_closet", sequenceName="seq_closet", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_closet")
	private int closetnum;
	@ManyToOne
	@JoinColumn(name="memnum", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omember memnum;
	private String cloth;
	@Column(nullable = true)
	private String img;
	private String maintag;
	private String subtag;
	@ColumnDefault("0")
	private int favorite;
	
}