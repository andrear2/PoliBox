package it.polito.ai.polibox.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name="Utenti")
public class Utente {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="u_id")
	private Long id;
	
	@Size(min=1, max=20, message="Il campo nome non può essere vuoto")
	private String nome;
	
	@Size(min=1, max=20, message="Il campo cognome non può essere vuoto")
	private String cognome;
	
	@Pattern(regexp="[A-Za-z0-9.+-_]+@[A-Za-z0-9.+-_]+\\.[A-Za-z]{2,4}", message="L'indirizzo email non è valido")
	private String email;
	
	@Size(min=6, max=20, message="La lunghezza della password deve essere compresa tra {min} e {max}")
	private String password;
	
	private String home_dir;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utente other = (Utente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHome_dir() {
		return home_dir;
	}

	public void setHome_dir(String home_dir) {
		this.home_dir = home_dir;
	}
}
