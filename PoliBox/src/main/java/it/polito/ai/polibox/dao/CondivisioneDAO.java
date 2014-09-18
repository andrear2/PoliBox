package it.polito.ai.polibox.dao;

import it.polito.ai.polibox.entity.Condivisione;

import java.util.List;

public interface CondivisioneDAO {

	public void addCondivisione(Condivisione condivisione);
	public List<Condivisione> getCondivisioniOwner(Long id);
	public List<Condivisione> getCondivisioni(Long id);
	public Condivisione getCondivisione(Long id);
	public Condivisione getCondivisioneWithoutTrans(Long id);
	public Condivisione getCondivisione(Long owner_id, Long u_id, String path);
	public void updateCondivisione(Condivisione condivisione);
	public void deleteCondivisione(Condivisione condivisione);
	public List<Condivisione> getActiveCondivisioni(String resource);
	
}
