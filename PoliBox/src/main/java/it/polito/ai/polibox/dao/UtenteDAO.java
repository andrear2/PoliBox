package it.polito.ai.polibox.dao;

import java.util.List;

import it.polito.ai.polibox.entity.Utente;

public interface UtenteDAO {
	
	public void addUtente(Utente utente);
	public List<Utente> getUtenti();
	public void deleteUtente(Utente utente);

}
