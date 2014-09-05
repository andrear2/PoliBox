package it.polito.ai.polibox.dao;

import java.util.List;

import it.polito.ai.polibox.entity.Utente;

public interface UtenteDAO {
	
	public void addUtente(Utente utente);
	public List<Utente> getUtenti();
	public Utente getUtente(String email, String password);
	public Utente getUtente(String email);
	public Utente getUtente(Long id);
	public void updateUtente(Utente utente);
	public void deleteUtente(Utente utente);

}
