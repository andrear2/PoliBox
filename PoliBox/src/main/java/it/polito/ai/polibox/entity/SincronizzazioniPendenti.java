package it.polito.ai.polibox.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sincronizzazionipendenti")
public class SincronizzazioniPendenti {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="Id")
	private Long id;
	
	@Column(name="u_id")
	private Long userId;
	
	@Column(name="id_disp")
	private Long dispId;
	
	@Column(name="Path")
	private String path;
	
	@Column(name="Type")
	private Integer type;


	public SincronizzazioniPendenti(Long userId, Long dispId, String path,
			Integer type) {
		super();
		this.userId = userId;
		this.dispId = dispId;
		this.path = path;
		this.type = type;
	}

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
		SincronizzazioniPendenti other = (SincronizzazioniPendenti) obj;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDispId() {
		return dispId;
	}

	public void setDispId(Long dispId) {
		this.dispId = dispId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	

}
