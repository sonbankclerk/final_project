package com.example.demo.ootwImgs;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.omycloset.Omycloset;
import com.example.demo.ootw.Oootw;

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
public class Oootwimgs {
	
	@Id
	@SequenceGenerator(name="seq_ootwimg", sequenceName="seq_ootwimg", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_ootwimg")
	private int ootwimgsnum;

	@ManyToOne
	@JoinColumn(name="ootwnum", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Oootw ootwnum;
//	int ootwnum;
	
	@ManyToOne
	@JoinColumn(name="closetnum", nullable=false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Omycloset closetnum;
	
}