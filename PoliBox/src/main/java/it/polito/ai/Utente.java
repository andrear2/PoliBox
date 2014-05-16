package it.polito.ai;

import javax.persistence.*;
import javax.validation.constraints.*;

public class Utente {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Size (min=1, message="Il campo nome non può essere vuoto")
	private String nome;
	
	

}
